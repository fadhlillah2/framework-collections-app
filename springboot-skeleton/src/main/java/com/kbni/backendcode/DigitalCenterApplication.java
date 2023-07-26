package com.kbni.backendcode;

import com.kbni.backendcode.configs.RSAKeyProperties;
import com.kbni.backendcode.configs.SwapiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RSAKeyProperties.class, SwapiProperties.class})
public class DigitalCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalCenterApplication.class, args);
	}

}
