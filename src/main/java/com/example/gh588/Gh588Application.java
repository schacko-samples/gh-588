package com.example.gh588;

import java.util.function.Consumer;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Gh588Application {

	public static void main(String[] args) {
		SpringApplication.run(Gh588Application.class, args);
	}

	@Bean
	public Consumer<KStream<Integer, String>> process() {
		return input ->
				input.foreach((key, value) -> {
					System.out.println("Key: " + key + " Value: " + value);
				});
	}

}
