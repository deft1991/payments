package ru.golitsyn.yandex.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 * Создание конфигов для flyway, для пролития миграций в бд
 *
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */

@Configuration
public class FlywaySlaveInitializer {

  private static final String MIGRATION_LOCATION = "db/migration";

  private final DataSource dataSource1;
  private final DataSource dataSource2;
  private final DataSource dataSource3;

  @Autowired
  public FlywaySlaveInitializer(DataSource dataSource1,
      @Qualifier("twoDb") DataSource dataSource2,
      @Qualifier("threeDb") DataSource dataSource3) {
    this.dataSource1 = dataSource1;
    this.dataSource2 = dataSource2;
    this.dataSource3 = dataSource3;
  }

  @PostConstruct
  public void migrateFlyway() {
    Flyway flyway = getFlyWay(dataSource1, MIGRATION_LOCATION, true);
    flyway.migrate();

    flyway = getFlyWay(dataSource2, MIGRATION_LOCATION, true);
    flyway.migrate();

    flyway = getFlyWay(dataSource3, MIGRATION_LOCATION, true);
    flyway.migrate();
  }

  private Flyway getFlyWay(DataSource dataSource1, String location, boolean isBaselineOnMigrate) {
    Flyway flyway = new Flyway();
    flyway.setDataSource(dataSource1);
    flyway.setLocations(location);
    flyway.setBaselineOnMigrate(isBaselineOnMigrate);
    return flyway;
  }
}
