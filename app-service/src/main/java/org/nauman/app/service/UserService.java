package org.nauman.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.nauman.app.jpa.entity.UserEntity;
import org.nauman.app.jpa.repository.UserRepository;
import org.nauman.app.model.UserDTO;
import org.nauman.app.model.UserViewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	private final Logger logs = LoggerFactory.getLogger(UserService.class);

	public void createUser(UserDTO userDTO) {
		
		try {
			UserEntity user = this.modelMapper.map(userDTO, UserEntity.class);
			
			if (user.getIsActive() == null) {
				user.setIsActive(true);
			}
			
			if (user.getIsAdmin() == null) {
				user.setIsAdmin(false);
			}
			
			userRepository.save(user);
			
		} catch (Exception e) {
			logs.error("create user failed", e);
		}
	}
	
	public void updateUser(UserDTO userDTO) {
		
		try {
			UserEntity user = this.modelMapper.map(userDTO, UserEntity.class);
			
			if (user.getIsActive() == null) {
				user.setIsActive(true);
			}
			
			if (user.getIsAdmin() == null) {
				user.setIsAdmin(false);
			}
			
			userRepository.save(user);
			
		} catch (Exception e) {
			logs.error("update user failed", e);
		}
	}
	
	public UserViewDTO getUser(Integer userId) {
		
		try {
			UserEntity user = userRepository.findById(userId).get();
			
			return this.modelMapper.map(user, UserViewDTO.class);
			
		} catch (Exception e) {
			logs.error("get user failed", e);
		}
		
		return null;
	}
	
	public UserDTO getUserDetails(Integer userId) {
		
		try {
			UserEntity user = userRepository.findById(userId).get();
			
			return this.modelMapper.map(user, UserDTO.class);
			
		} catch (Exception e) {
			logs.error("get user failed", e);
		}
		
		return null;
	}
	
	
	public List<UserViewDTO> searchUsers(String search) {
		
		try {
			List<UserEntity> userEntityList =
					userRepository.findByFirstNameContainsOrLastNameContainsOrAddressContainsOrCityContainsOrPhoneContains(
							search,search,search,search,search);
			
			List<UserViewDTO> userDTOList = modelMapper.map(userEntityList, new TypeToken<List<UserViewDTO>>() {}.getType());
			
			return userDTOList;
			
		} catch (Exception e) {
			logs.error("search user failed", e);
		}
		
		return new ArrayList<>();
	}
	
	public List<UserViewDTO> getAllActiveUsers() {
		
		try {
			List<UserEntity> userEntityList = userRepository.findByIsActive(true);
			
			List<UserViewDTO> userDTOList = modelMapper.map(userEntityList, new TypeToken<List<UserViewDTO>>() {}.getType());
			
			return userDTOList;
			
		} catch (Exception e) {
			logs.error("get all active users failed", e);
		}
		
		return new ArrayList<>();
	}
	
	
	@Transactional
	public void updatedActiveStatusOfUser(Integer userId, Boolean isActive) {
		
		try {
			userRepository.updateUserActiveStatus(isActive, userId);
		}catch(Exception e) {
			logs.error("get active status of user failed", e);
		}
	}
}
