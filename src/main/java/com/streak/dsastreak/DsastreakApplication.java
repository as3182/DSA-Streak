package com.streak.dsastreak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class DsastreakApplication implements CommandLineRunner  {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DsastreakApplication.class, args);

	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println(LocalDateTime.now());
	}
}
