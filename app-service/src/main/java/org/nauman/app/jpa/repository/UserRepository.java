package org.nauman.app.jpa.repository;

import java.util.List;

import org.nauman.app.jpa.entity.UserEntity;
import org.nauman.app.jpa.projections.UserLoginView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	public List<UserEntity> findByIsActive(Boolean isActive);
	
	public List<UserEntity> findByFirstNameContainsOrLastNameContainsOrAddressContainsOrCityContainsOrTelephoneContains(
			String firstName, String lastName, String address, String city, String telephone);
	
	@Modifying
	@Query("UPDATE UserEntity u SET u.isActive = :isActive WHERE u.id = :userId")
	public void updateUserActiveStatus(Boolean isActive, Integer userId);
	
	public UserLoginView findByEmail(String email);
	
}
