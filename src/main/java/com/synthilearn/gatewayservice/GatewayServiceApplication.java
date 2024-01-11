package com.synthilearn.gatewayservice;

import com.synthilearn.loggingstarter.EnableLogging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
@EnableLogging
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@GetMapping("/test")
	public String get() {
		log.error("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		return "tuta";
	}

	@PostMapping("/test2")
	public TestClass post(@RequestBody TestClass testClass) {
		log.error("Body: " + testClass);
		return new TestClass("REsponse 1", "Reponse 2");
	}

	@Data
	@AllArgsConstructor
	static class TestClass {
		private String test1;
		private String test2;
	}
}
