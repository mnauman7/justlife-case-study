package org.nauman.app.jpa.repository;

import java.util.List;

import org.nauman.app.jpa.entity.TimeSlotsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlotsEntity, Integer> {
	
	public List<TimeSlotsEntity> findByIsActive(Boolean isActive);

}
