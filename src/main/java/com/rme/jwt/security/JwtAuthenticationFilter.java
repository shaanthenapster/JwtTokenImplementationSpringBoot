package com.rme.jwt.security;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rme.jwt.model.ApplicationUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	//{"usrename":"shaan" , "password" :"sanu001#"}
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			ApplicationUser applicationUser = 	new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(applicationUser.getUsername() , applicationUser.getPassword()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
			
		SecurityConstants sc = new SecurityConstants();
		ZonedDateTime expirationTimeUtc = ZonedDateTime.now(ZoneOffset.UTC).plus(sc.EXPIRATION_TIME,ChronoUnit.MILLIS);
		String token = Jwts.builder().setSubject(((User)authResult.getPrincipal()).getUsername()).setExpiration(Date.from(expirationTimeUtc.toInstant()))
				.signWith(SignatureAlgorithm.HS256 , sc.SECRET)
				.compact();
        response.getWriter().write(token);
		response.addHeader(sc.HEADER_STRING, sc.TOKEN_PREFIX + token);
	}
}
