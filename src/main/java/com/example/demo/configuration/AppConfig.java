package com.example.demo.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.models.User;
import java.util.List;
import java.util.ArrayList;

@Configuration
public class AppConfig {
	@Bean
	public List<User> userList () {
		return new ArrayList<>();
	}

}
