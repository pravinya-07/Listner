package com.example.Listner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class ListenerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ListenerApplication.class, args);
		Logger log = Logger.getLogger("pkyc");
		log.info("Testing");
	}
}