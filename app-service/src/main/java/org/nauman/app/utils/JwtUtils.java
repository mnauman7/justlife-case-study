package org.nauman.app.utils;

import java.util.Calendar;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils {

	// @Value("${jwt.token-validity-days}")
	private static Integer tokenValidMinutes = 10;

	// @Value("${jwt.issuer}")
	private static String issuer = "Nauman";
	
	private static String secret = "tytyNopopHHjjkkk";

	public static String createJwt(String subject) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, tokenValidMinutes);

		String jwt = JWT.create()
				.withSubject(subject)
				.withClaim("id", subject)
				.withNotBefore(new Date())
				.withExpiresAt(cal.getTime())
				.withIssuer(issuer)
				.sign(Algorithm.HMAC256(secret));

		return jwt;
	}
	
	
	public static String getTokenFromRequest(HttpServletRequest request) {
		
		String requestHeader = request.getHeader("Authorization");
		
		if (requestHeader != null && requestHeader.startsWith("Bearer ") && requestHeader.substring(7) != null
				&& !"null".equals(requestHeader.substring(7))) {
			return requestHeader.substring(7);
		}
		
		return null;
	}

	public static boolean validateJwt(String token) {
		try {
			JWTVerifier verifier = getJWTVerifier();

			// check if jwt signature is valid
			DecodedJWT verifiedJWT = verifier.verify(token);

			// check if jwt is expired
			Date expirationDate = verifiedJWT.getExpiresAt();
			return expirationDate.after(new Date());

		} catch (Exception e) {
			return false;
		}
	}

	private static JWTVerifier getJWTVerifier() {
		Algorithm algorithm = Algorithm.HMAC256(secret);
		return JWT.require(algorithm).withIssuer(issuer).build();
	}
	
	public static String getSubject(String token) {
		JWTVerifier verifier = getJWTVerifier();
		return verifier.verify(token).getSubject();
	}

}
