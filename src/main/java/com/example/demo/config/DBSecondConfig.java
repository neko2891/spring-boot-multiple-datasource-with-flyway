/* (C) 2021 */
package com.example.demo.config;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.info.MigrationInfoDumper;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "secondEntityManager",
    transactionManagerRef = "secondTransactionManager",
    basePackages = "com.example.demo.db2.repo")
@EnableTransactionManagement
@Slf4j
public class DBSecondConfig {

  @Bean
  @ConfigurationProperties("datasource.second.jpa.properties")
  public Properties secondJpaProperties() {
    return new Properties();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean secondEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(secondDataSource(secondDataSourceProperties()));
    em.setJpaProperties(secondJpaProperties());
    em.setPackagesToScan(new String[] {"com.example.demo.db2.entity"});
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setPersistenceUnitName("demo");
    em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
    return em;
  }

  @Bean
  @ConfigurationProperties(prefix = "datasource.second.config")
  public DataSourceProperties secondDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource secondDataSource(DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder().build();
  }

  @Bean
  public PlatformTransactionManager secondTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(secondEntityManager().getObject());
    return transactionManager;
  }

  @Bean
  @ConfigurationProperties(prefix = "datasource.second.spring.flyway")
  public FlywayProperties secondFlywayProperties() {
    return new FlywayProperties();
  }

  @Bean
  FlywayMigrationInitializer secondFlywayInitializer(Flyway _flyway) {
    log.info(
        "================================================================================================");
    FlywayProperties props = secondFlywayProperties();
    Flyway flyway =
        Flyway.configure()
            .dataSource(props.getUrl(), props.getUser(), props.getPassword())
            .defaultSchema(props.getDefaultSchema())
            .schemas(props.getSchemas().stream().toArray(String[]::new))
            .locations(props.getLocations().stream().toArray(String[]::new))
            .load();

    FlywayMigrationStrategy strategy =
        new FlywayMigrationStrategy() {
          @Override
          public void migrate(Flyway flyway) {
            log.info("-----------------------------------------------------------------------");
            log.info("\n{}", MigrationInfoDumper.dumpToAsciiTable(flyway.info().all()));
            flyway.migrate();
            log.info("\n{}", MigrationInfoDumper.dumpToAsciiTable(flyway.info().all()));
            log.info("-----------------------------------------------------------------------");
          }
        };

    return new FlywayMigrationInitializer(flyway, strategy);
  }
}
