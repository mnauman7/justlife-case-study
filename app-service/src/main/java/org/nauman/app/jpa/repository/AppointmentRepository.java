package org.nauman.app.jpa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.nauman.app.jpa.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {
	
	public List<AppointmentEntity> findByUserId(Integer userId);
	
	@Modifying
	@Query("UPDATE AppointmentEntity ae SET ae.startTimeSlotId = :startTimeSlotId, ae.appointmentDate = :appointmentDate"
			+ " WHERE ae.appointmentId = :appointmentId")
	public void updateDateAndTimeOfAppointment(Integer startTimeSlotId, LocalDate appointmentDate, Integer appointmentId);
	
	
	@Query("SELECT ap FROM AppointmentEntity ap LEFT JOIN FETCH ap.staff st "
			+ " WHERE ap.appointmentId = :appointmentId")
	public Optional<AppointmentEntity> findAppointmentWithStaffByAppointmentId(Integer appointmentId);
	
	
	@Query("SELECT ap FROM AppointmentEntity ap LEFT JOIN FETCH ap.timeSlots ts "
			+ " WHERE ap.userId = :userId")
	public List<AppointmentEntity> findAppointmentWithTimeSlotByUserId(Integer userId);
}
