package org.nauman.app.jpa.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.nauman.app.jpa.entity.TimeSlotEntity;
import org.nauman.app.jpa.projections.TimeSlotIdView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlotEntity, Integer> {

	public List<TimeSlotEntity> findAll();

	/**
	 * Brings all time slots entries in the time range
	 * 
	 * @param startTimeGreaterThan
	 * @param endTimeLessThan
	 * @return List<TimeSlotIdView> list of time slots
	 */
	public List<TimeSlotIdView> findSlotIdsByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(
			LocalTime startTimeGreaterThan, LocalTime endTimeLessThan);
	
	public TimeSlotIdView findSlotIdByStartTime(LocalTime starTime);
	
	public TimeSlotEntity findByStartTime(LocalTime starTime);
	
	
	/**
	 * Brings all available time slots for the given staff 
	 * 
	 * @param staffIds
	 * @return List<TimeSlotEntity>
	 */
	@Query("SELECT ts FROM TimeSlotEntity ts "
			+ " WHERE ts.slotId NOT IN ( "
			+ " SELECT DISTINCT ts2.slotId FROM TimeSlotEntity ts2 INNER JOIN StaffOccupancyEntity so ON so.timeSlotId = ts.slotId "
			+ " WHERE so.staffId IN :staffIds AND so.occupancyDate = :date"
			+ ")")
	public List<TimeSlotEntity> findAvailableTimeSlotsForStaff(List<Integer> staffIds, LocalDate date);

}
