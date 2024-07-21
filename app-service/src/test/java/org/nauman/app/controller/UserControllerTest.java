package org.nauman.app.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nauman.app.model.UserViewDTO;
import org.nauman.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
    @Autowired
    MockMvc mvc;
    
    @MockBean
	private UserService userService;
    
//    @Test
//    public void testGetUserAsSuperUser() throws Exception {
//		when(userService.getUserAsSuperUser(2)).thenReturn(prepareTestUserDTO());
//
//		mvc.perform(get("/user/2/super"))
//		.andExpect(status().isOk())
//		.andExpect(jsonPath("$.id").value(2))
//		.andExpect(jsonPath("$.firstName").value("Super Nauman"))
//		.andExpect(jsonPath("$.lastName").value("Super Mohd"))
//		.andExpect(jsonPath("$.isAdmin").value(true));
//    	 
//    }
    
	private UserViewDTO prepareTestUserDTO() {
		UserViewDTO user = new UserViewDTO();
		user.setId(2);
		user.setFirstName("Super Nauman");
		user.setLastName("Super Mohd");
		user.setAddress("Street 16");
		user.setCity("London");
		user.setTelephone("0123456789");
		user.setIsActive(true);
		user.setIsAdmin(true);
		
		return user;
	}
}
