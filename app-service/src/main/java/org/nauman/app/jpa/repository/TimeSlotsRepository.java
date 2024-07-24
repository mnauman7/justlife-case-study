package org.nauman.app.jpa.repository;

import java.time.LocalTime;
import java.util.List;

import org.nauman.app.jpa.entity.TimeSlotEntity;
import org.nauman.app.jpa.projections.TimeSlotIdView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlotEntity, Integer> {

	public List<TimeSlotEntity> findAll();

	/**
	 * Methos used to bring all time slots entries in the time range
	 * 
	 * @param startTimeGreaterThan
	 * @param endTimeLessThan
	 * @return List<TimeSlotIdView> list of time slots
	 */
	public List<TimeSlotIdView> findSlotIdsByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(
			LocalTime startTimeGreaterThan, LocalTime endTimeLessThan);
	
	public TimeSlotIdView findSlotIdByStartTime(LocalTime starTime);
	
	public TimeSlotEntity findByStartTime(LocalTime starTime);

}
