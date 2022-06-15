package com.ndr.socialasteroids;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialAsteroidsApplication
{
	static Logger logger = LoggerFactory.getLogger(SocialAsteroidsApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SocialAsteroidsApplication.class, args);
	}
}
