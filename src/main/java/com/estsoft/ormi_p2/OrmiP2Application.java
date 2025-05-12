package com.estsoft.ormi_p2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OrmiP2Application {
	public static void main(String[] args) {
		SpringApplication.run(OrmiP2Application.class, args);

	}

}
