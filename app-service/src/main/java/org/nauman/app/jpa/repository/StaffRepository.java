package org.nauman.app.jpa.repository;

import java.util.List;

import org.nauman.app.jpa.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Integer> {
	
	public List<StaffEntity> findByIsActive(Boolean isActive);
}
