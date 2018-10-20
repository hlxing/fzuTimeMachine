package com.west2.fzuTimeMachine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.west2.fzuTimeMachine.dao")
public class FzuTimeMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(FzuTimeMachineApplication.class, args);
	}
}
