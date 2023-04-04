package fr.insy2s.commerce.shoponlineback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@SpringBootApplication
//@Import(MapperConfig.class)
public class ShopOnlineBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopOnlineBackApplication.class, args);
	}



}
