package org.nauman.app.jpa.repository;

import org.nauman.app.jpa.entity.StaffOccupancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffOccupancyRepository extends JpaRepository<StaffOccupancyEntity, Integer> {

}
