package org.nauman.app.jpa.repository;

import java.util.List;

import org.nauman.app.jpa.entity.AppointmentStaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentStaffRepository extends JpaRepository<AppointmentStaffEntity, Integer> {
	
	public List<AppointmentStaffEntity> findAllByAppointmentId(Integer appointmentId);
}
