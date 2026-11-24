package com.krillion;

import org.springframework.boot.SpringApplication;

public class TestKrillionApplication {

	public static void main(String[] args) {
		SpringApplication.from(KrillionApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
