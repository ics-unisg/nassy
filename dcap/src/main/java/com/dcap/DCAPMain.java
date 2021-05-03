package com.dcap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class DCAPMain {

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}


	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(DCAPMain.class, args);
		Stream.of(ctx.getBeanDefinitionNames()).sorted().forEach(System.out::println);
		long l = Runtime.getRuntime().maxMemory();
		System.err.println(l);
	}

	public static void runni(String[] args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
		String hashedPassword = passwordEncoder.encode("Test");
		System.out.println(hashedPassword);
	}

}
