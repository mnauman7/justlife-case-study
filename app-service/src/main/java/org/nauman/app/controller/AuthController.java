package org.nauman.app.controller;

import org.nauman.app.model.LoginDTO;
import org.nauman.app.model.LoginResponseDTO;
import org.nauman.app.service.AppointmentService;
import org.nauman.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *  This class deals with login and logout funtionalities of user
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthService authService;
	
	
	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	
    /**
     * @param loginDTO
     * @return LoginResponseDTO this contains a JWT token which is stored by frontend
     *                          and this JWT token is sent back to backend on each request to validate user
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDTO login(@RequestBody LoginDTO loginDTO) {
    	return authService.login(loginDTO);
    }

}
