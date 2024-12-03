package dev.fpsaraiva.api_assembleia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiAssembleiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAssembleiaApplication.class, args);
	}

}
