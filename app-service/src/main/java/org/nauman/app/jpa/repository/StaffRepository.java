package org.nauman.app.jpa.repository;

import java.time.LocalTime;
import java.util.List;

import org.nauman.app.jpa.entity.StaffEntity;
import org.nauman.app.jpa.projections.StaffBookingView;
import org.nauman.app.jpa.projections.StaffIdView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Integer> {
	
	public List<StaffEntity> findByIsActive(Boolean isActive);
	
	public List<StaffIdView> findStaffIdByVehicleIdAndIsActive(Integer vehicleId, Boolean isActive);
	
	/**
	 * 
	 * This method runs a query to check what staff are not occupied with other tasks.
	 * Staff are occupied for a time slot if an entry exists in StaffOccupancy table.
	 * If no entry exists in StaffOccupancy table for a time slot then staff is considered available for that time slot
	 * 
	 * Query written in native mysql query because JPQL does not support this type of query generation.
	 * This Query could be moved to criteria builder if JPA is required instead of native query.
	 * 
	 * @param date to check if staff is available on this date
	 * @param startTime to check if staff is available on this time
	 * @param endTime to check if staff is available on this time
	 * @return List<StaffBookingView> list of staff which are available in this time slot
	 */
	@Query(value = "SELECT DISTINCT s.staff_id as staffId, s.first_name as firstName, s.last_name as lastName, s.vehicle_id as vehicleId"
			+ " FROM Staff s"
			+ " LEFT JOIN ("
			+ "    SELECT DISTINCT s.staff_id"
			+ "    FROM Staff s"
			+ "    INNER JOIN StaffOccupancy so ON so.staff_id = s.staff_id"
			+ "    INNER JOIN TimeSlots ts ON ts.slot_id = so.time_slot_id"
			+ "    WHERE occupancy_date = :date AND start_time >= :startTime AND start_time <= :endTime"
			+ " ) occupied_staff ON s.staff_id = occupied_staff.staff_id"
			+ " WHERE occupied_staff.staff_id IS NULL", nativeQuery = true)
	public List<StaffBookingView> findAvailableStaffForBookingSlot(String date, LocalTime startTime, LocalTime endTime);
	
	
	/**
	 * Returns all the time slots and all the staff available for each time slot.
	 * 
	 * This method runs a query to check what staff are not occupied with other tasks.
	 * Staff are occupied for a time slot if an entry exists in StaffOccupancy table.
	 * If no entry exists in StaffOccupancy table for a time slot then staff is considered available for that time slot
	 * 
	 * Query written in native mysql query because JPQL does not support this type of query generation.
	 * This Query could be moved to criteria builder if JPA is required instead of native query.
	 * 
	 * @param date to check if staff is available on this date
	 * @param duration service hours for appointment
	 * @return List<StaffBookingView> list of staff which are available in this time slot
	 */
	@Query(value = "SELECT ts.slot_id, "
			+ " ts.slot_name, "
			+ " (SELECT GROUP_CONCAT(DISTINCT s.staff_id) "
			+ "			 FROM Staff s "
			+ "			 LEFT JOIN ( "
			+ "			    SELECT DISTINCT s.staff_id "
			+ "			    FROM Staff s "
			+ "			    INNER JOIN StaffOccupancy so ON so.staff_id = s.staff_id "
			+ "			    INNER JOIN TimeSlots ts ON ts.slot_id = so.time_slot_id "
			+ "			    WHERE occupancy_date = :date AND start_time >= ts.start_time AND start_time <= ts.start_time + INTERVAL :duration HOUR + INTERVAL 30 MINUTE "
			+ "			 ) occupied_staff ON s.staff_id = occupied_staff.staff_id "
			+ "			 WHERE occupied_staff.staff_id IS NULL "
			+ " ) as availableStaff "
			+ " FROM TimeSlots ts ", nativeQuery = true)
	public List<StaffBookingView> findAvailableStaffForAllBookingSlot(String date, Integer duration);
	
}
