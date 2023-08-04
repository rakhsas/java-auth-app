package com.example.demo.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.exceptions.JWTVerificationException;


import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter {
	@Autowired private MyUserDetailService userDetailsService;
	@Autowired private JwtUtil jwtUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			String authHeader = request.getHeader("Authorization");
			if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
				String jwt = authHeader.substring(7);
				if (jwt == null || jwt.isBlank()) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Jwt Token in Bearer Header");
				}else {
					try {
						String username = jwtUtil.validateToken(jwt);
						UserDetails userDetails = userDetailsService.loadUserByUsername(username);
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
						userDetails.getPassword(), userDetails.getAuthorities());
						if (SecurityContextHolder.getContext().getAuthentication() == null) {
							SecurityContextHolder.getContext().setAuthentication(authToken);
						}
					} catch(JWTVerificationException e)
					{
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Jwt Token");
					}
				}
			}
		filterChain.doFilter(request, response);
	}

}
