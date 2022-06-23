package com.ndr.socialasteroids;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SocialAsteroidsApplication
{
	static Logger logger = LoggerFactory.getLogger(SocialAsteroidsApplication.class);

	public static void main(String[] args)
	{
		SpringApplication.run(SocialAsteroidsApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer()
	{
		return new WebMvcConfigurer()
		{
			@Override
			public void addCorsMappings(CorsRegistry registry)
			{
				registry.addMapping("/**")
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
					.allowedOrigins("*")
					.allowedHeaders("Date, Content-Type, Accept, X-Requested-With, Authorization, From, X-Auth-Token, Request-Id")
					.allowCredentials(true);
			}
		};
	}
}
