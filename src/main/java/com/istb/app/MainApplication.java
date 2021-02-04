
package com.istb.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {

		SpringApplication.run(MainApplication.class, args);

	}

}
