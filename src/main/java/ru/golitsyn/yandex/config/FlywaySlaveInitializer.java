package ru.golitsyn.yandex.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */

@Configuration
public class FlywaySlaveInitializer {

  @Autowired
  private DataSource dataSource1;
  @Autowired
  @Qualifier("twoDb")
  private DataSource dataSource2;
  @Autowired
  @Qualifier("threeDb")
  private DataSource dataSource3;
//  //todo other datasources

  @PostConstruct
  public void migrateFlyway() {
	Flyway flyway = new Flyway();
	//todo if default config is not sufficient, call setters here
	flyway.setDataSource(dataSource1);
	flyway.setLocations("db/migration");
	flyway.setBaselineOnMigrate(true);
	flyway.migrate();
	//source 2
	flyway.setDataSource(dataSource2);
	flyway.setLocations("db/migration");
	flyway.setBaselineOnMigrate(true);
	flyway.migrate();

	//source 3
    flyway.setDataSource(dataSource3);
    flyway.setLocations("db/migration");
    flyway.setBaselineOnMigrate(true);
    flyway.migrate();
  }
}
