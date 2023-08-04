package com.example.demo.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;

// import com.example.demo.repository.UserRepo;


import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// @Autowired private UserRepo userRepo;
	@Autowired	private JWTFilter filter;
	@Autowired	private MyUserDetailService uds;

	@Override
	protected void	configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.cors().and().authorizeHttpRequests()
		.antMatchers("api/auth/**").permitAll()
		.antMatchers(AUTH_WHITE_LIST).permitAll()
		.antMatchers("api/user/**")
		.hasRole("USER")
		.and()
		.userDetailsService(uds)
		.exceptionHandling()
		.authenticationEntryPoint((req, res, authException) ->
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static final String[] AUTH_WHITE_LIST = {
		"v3/api-docs/**",
		"/swagger-ui/**",
		"v2/api-docs/**",
		"/swagger-resources/**"
	};
}
