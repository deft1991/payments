package ru.golitsyn.yandex.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MultipleDBConfig {

  @Primary
  @Bean(name = "oneDb")
  @ConfigurationProperties(prefix = "spring.onedb")
  public DataSource oneDbDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "oneJdbcTemplate")
  public JdbcTemplate oneJdbcTemplate(@Qualifier("oneDb")
      DataSource dsPostgres) {
    return new JdbcTemplate(dsPostgres);
  }


  @Bean(name = "twoDb")
  @ConfigurationProperties(prefix = "spring.twodb")
  public DataSource twoDbDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "twoJdbcTemplate")
  public JdbcTemplate twoJdbcTemplate(@Qualifier("twoDb")
      DataSource dsPostgres) {
    return new JdbcTemplate(dsPostgres);
  }

  @Bean(name = "threeDb")
  @ConfigurationProperties(prefix = "spring.threedb")
  public DataSource threeDbDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "threeJdbcTemplate")
  public JdbcTemplate threeJdbcTemplate(@Qualifier("threeDb")
      DataSource dsPostgres) {
    return new JdbcTemplate(dsPostgres);
  }
}
