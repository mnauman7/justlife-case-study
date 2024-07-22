package org.nauman.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.nauman.app.jpa.entity.UserEntity;
import org.nauman.app.jpa.repository.UserRepository;
import org.nauman.app.model.UserViewDTO;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;

	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private UserService userService;
	
	
	/*
	 * @Test public void testGetUserAsSuperUser() { UserEntity testUserEntity =
	 * prepareTestUserEntity();
	 * 
	 * //mocking user repository
	 * when(userRepository.findById(2)).thenReturn(Optional.of(testUserEntity));
	 * 
	 * //mocking model mapper UserViewDTO testUserDTO = prepareTestUserDTO();
	 * when(modelMapper.map(testUserEntity,
	 * UserViewDTO.class)).thenReturn(testUserDTO);
	 * 
	 * UserViewDTO responseUser = userService.getUserAsSuperUser(2);
	 * 
	 * //verify repository method was called verify(userRepository).findById(2);
	 * 
	 * assertNotNull(responseUser);
	 * 
	 * //check if fields have expected values
	 * assertEquals(responseUser.getFirstName(), "Super Nauman");
	 * assertEquals(responseUser.getLastName(), "Super Mohd");
	 * assertEquals(responseUser.getIsAdmin(), true); }
	 */
	
	private UserEntity prepareTestUserEntity() {
		UserEntity user = new UserEntity();
		user.setId(2);
		user.setFirstName("Nauman");
		user.setLastName("Mohd");
		user.setAddress("Street 16");
		user.setCity("London");
		user.setPhone("0123456789");
		user.setIsActive(true);
		user.setIsAdmin(false);
		
		return user;
	}
	
	private UserViewDTO prepareTestUserDTO() {
		UserViewDTO user = new UserViewDTO();
		user.setId(2);
		user.setFirstName("Nauman");
		user.setLastName("Mohd");
		user.setAddress("Street 16");
		user.setCity("London");
		user.setPhone("0123456789");
		user.setIsActive(true);
		user.setIsAdmin(false);
		
		return user;
	}

}
