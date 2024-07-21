package org.nauman.app.controller;

import org.nauman.app.model.LoginDTO;
import org.nauman.app.model.LoginResponseDTO;
import org.nauman.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDTO login(@RequestBody LoginDTO loginDTO) {
    	return authService.login(loginDTO);
    }

}
