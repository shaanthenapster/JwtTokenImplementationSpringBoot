package com.rme.jwt.security;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rme.jwt.model.ApplicationUser;
import com.rme.jwt.service.CustomUserDetailsService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private final CustomUserDetailsService customUserDetailsService;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
			CustomUserDetailsService customUserDetailsService) {
		super(authenticationManager);
		this.customUserDetailsService = customUserDetailsService;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
			                      HttpServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		    SecurityConstants sc = null;
			String header = req.getHeader(sc.HEADER_STRING);
			if(header == null || !header.startsWith(sc.TOKEN_PREFIX)) {
				chain.doFilter(req, resp);
				return;
			}
			 UsernamePasswordAuthenticationToken userNamePasswordAuth = getAuthenticationToken(req);
			 SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuth);
			 chain.doFilter(req, resp);
			
	}
			
			
private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
			     SecurityConstants sc = null;
				String token = request.getHeader(sc.HEADER_STRING);
				if(token == null)
					{
					return null;
					}
				String username = Jwts.parser().setSigningKey(sc.SECRET)
						.parseClaimsJws(token.replace(sc.TOKEN_PREFIX, ""))
						.getBody()
						.getSubject();
				
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
				ApplicationUser applicationUser = (ApplicationUser) customUserDetailsService.loadUserByUsername(username);
				if (username != null)
					return new UsernamePasswordAuthenticationToken(applicationUser, null, userDetails.getAuthorities());
				else
					return null;
			}      
}

