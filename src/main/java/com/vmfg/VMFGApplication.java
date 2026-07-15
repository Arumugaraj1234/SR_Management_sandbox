package com.vmfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@SpringBootApplication
@EnableScheduling
public class VMFGApplication {
	private static final Logger logger = LoggerFactory.getLogger(VMFGApplication.class);

	public static void main(String[] args) {
		try {

			logger.info("SmartRun Application Started");
			SpringApplication.run(VMFGApplication.class, args);
		} catch (Exception ex) {
			logger.error("SmartRun Application Start Exception--->" + ex);
		}
	}
}