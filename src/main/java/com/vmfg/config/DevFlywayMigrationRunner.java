package com.vmfg.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Spring Boot 2.7.0's built-in FlywayAutoConfiguration is compiled against Flyway 8.x's
 * API and breaks (NoSuchMethodError) against Flyway 9.x+, so we call the Flyway 9.22.3
 * API directly instead of going through Spring's autoconfiguration. Dev/sandbox only.
 */
//@Component
@Profile("dev")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DevFlywayMigrationRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DevFlywayMigrationRunner.class);

	private final DataSource dataSource;

	public DevFlywayMigrationRunner(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void run(String... args) {
		logger.info("Running Flyway migrations against sandbox datasource");
		Flyway flyway = Flyway.configure()
				.dataSource(dataSource)
				.locations("classpath:db/migration")
				.baselineOnMigrate(true)
				.baselineVersion("0")
				.outOfOrder(false)
				.load();
		flyway.migrate();
	}
}
