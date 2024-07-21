package org.nauman.app.controller;

import java.util.List;

import org.nauman.app.model.UserDTO;
import org.nauman.app.model.UserViewDTO;
import org.nauman.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/health")
	public String healthCheck() {
		return "User service is up";
	}
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserDTO userDTO) {
    	userService.createUser(userDTO);
    }
    
	@GetMapping("/{userId}")
	public UserViewDTO getUser(@PathVariable("userId") Integer userId) {
		return userService.getUser(userId);
	}
	
	@GetMapping("/{userId}/details")
	public UserDTO getUserDetails(@PathVariable("userId") Integer userId) {
		return userService.getUserDetails(userId);
	}
	
	@GetMapping
	public List<UserViewDTO> getAllUsers(@RequestParam(required = false) String search) {
		
		if (search == null) {
			return userService.getAllActiveUsers();
			
		} else {
			return userService.searchUsers(search);
		}
	}
	
	@PutMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateUser(@PathVariable("userId") Integer userId, @RequestBody UserDTO userDTO) {
		userService.updateUser(userDTO);
	}
	
	@PutMapping("/{userId}/active")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateActiveStatusOfUser(@PathVariable("userId") Integer userId, @RequestParam Boolean isActive) {
		userService.updatedActiveStatusOfUser(userId, isActive);
	}
}
