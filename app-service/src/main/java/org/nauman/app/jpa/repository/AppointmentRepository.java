package org.nauman.app.jpa.repository;

import java.util.List;

import org.nauman.app.jpa.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {
	
	public List<AppointmentEntity> findByUserId(Integer userId);
	
}
