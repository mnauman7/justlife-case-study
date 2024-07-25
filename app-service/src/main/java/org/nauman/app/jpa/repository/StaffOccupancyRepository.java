package org.nauman.app.jpa.repository;

import org.nauman.app.jpa.entity.StaffOccupancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffOccupancyRepository extends JpaRepository<StaffOccupancyEntity, Integer> {
	
	@Modifying
	@Query("DELETE FROM StaffOccupancyEntity occ WHERE occ.appointmentId = :appointmentId")
	public void deleteStaffOccupancyByAppointmentId(Integer appointmentId);
}
