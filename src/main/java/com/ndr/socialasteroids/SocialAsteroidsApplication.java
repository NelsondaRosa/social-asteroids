package com.ndr.socialasteroids;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialAsteroidsApplication
{
	static Logger logger = LoggerFactory.getLogger(SocialAsteroidsApplication.class);

	public static void main(String[] args)
	{
		SpringApplication.run(SocialAsteroidsApplication.class, args);
	}
	
	// @Bean
	// public WebMvcConfigurer corsConfigurer()
	// {
	// 	return new WebMvcConfigurer()
	// 	{
	// 		@Override
	// 		public void addCorsMappings(CorsRegistry registry)
	// 		{
	// 			registry.addMapping("/**")
	// 				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	// 				.allowedOrigins("http://localhost:3000")
	// 				.allowedHeaders("*")
	// 				.allowCredentials(true);
	// 		}
	// 	};
	// }
}
