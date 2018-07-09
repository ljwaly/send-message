package com.ljw.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class PublishServer {

	private static final Logger logger =  LoggerFactory.getLogger(PublishServer.class);
	
	public static void main(String[] args) {
		
		SpringApplication app =new SpringApplication(PublishServer.class);
		
		Environment environment = app.run(args).getEnvironment();
		logger.info("\n----------------------------------------------------------\n\t"
				+ "Application {} is running! Access URLs:\n\t" + // 大括号1
				"Local: \t\thttp://localhost:{}\n\t" + // 大括号2
				"{}\n\t" + // 大括号3
				"\n----------------------------------------------------------",

				environment.getProperty("spring.application.name"), // 大括号1对应的内容
				environment.getProperty("server.port"), // 大括号2对应的内容
				"Thank You!"// 大括号3对应的内容
				);
		
	}
	
}
