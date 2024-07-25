package org.nauman.app.service;

import org.nauman.app.jpa.projections.UserLoginView;
import org.nauman.app.jpa.repository.UserRepository;
import org.nauman.app.model.LoginDTO;
import org.nauman.app.model.LoginResponseDTO;
import org.nauman.app.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
	
	private UserRepository userRepository;
	
	@Autowired
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	/**
	 * Logins user and generates JWT token and sends it back in response in case of successful login.
	 * @param loginDTO
	 * @return LoginResponseDTO
	 */
	public LoginResponseDTO login(LoginDTO loginDTO) {
		UserLoginView userLogin = userRepository.findByEmail(loginDTO.getEmail());
		
		if (userLogin == null ||
				!loginDTO.getPassword().equals(userLogin.getPassword())) {
			throw new ResponseStatusException(HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()),
			          "Invalid credentials");
		}
		
		String token = JwtUtils.createJwt(userLogin.getId().toString());
		
		return new LoginResponseDTO(token,userLogin.getId());
	}
}
