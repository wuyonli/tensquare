package com.tensquare.spit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication
@EnableCaching   // 开启springCache
public class SpitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpitApplication.class, args);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker(1, 1);
	}
	
}
