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
	
	//query not supported by JPQL
	@Query(value = "SELECT DISTINCT s.staff_id as staffId, s.first_name as firstName, s.last_name as lastName, s.vehicle_id as vehicleId"
			+ " FROM Staff s"
			+ " LEFT JOIN ("
			+ "    SELECT DISTINCT s.staff_id"
			+ "    FROM Staff s"
			+ "    INNER JOIN StaffOccupancy so ON so.staff_id = s.staff_id"
			+ "    INNER JOIN TimeSlots ts ON ts.slot_id = so.time_slot_id"
			+ "    WHERE occupancy_date = :date AND start_time BETWEEN :startTime AND :endTime"
			+ " ) occupied_staff ON s.staff_id = occupied_staff.staff_id"
			+ " WHERE occupied_staff.staff_id IS NULL", nativeQuery = true)
	public List<StaffBookingView> findAvailableStaffForBookingSlot(String date, LocalTime startTime, LocalTime endTime);
	
}
