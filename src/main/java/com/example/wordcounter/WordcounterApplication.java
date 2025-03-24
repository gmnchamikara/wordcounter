package com.example.wordcounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class WordcounterApplication {

	public static void main(String[] args) {

		SpringApplication.run(WordcounterApplication.class, args);

	}

}
