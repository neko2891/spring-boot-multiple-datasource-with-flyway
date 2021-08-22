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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "entityManager",
    transactionManagerRef = "transactionManager",
    basePackages = "com.example.demo.db1.repo")
@EnableTransactionManagement
@Slf4j
public class DBPrimaryConfig {

  @Primary
  @Bean
  @ConfigurationProperties("datasource.primary.jpa.properties")
  public Properties jpaProperties() {
    return new Properties();
  }

  @Primary
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource(dataSourceProperties()));
    em.setJpaProperties(jpaProperties());
    em.setPackagesToScan(new String[] {"com.example.demo.db1.entity"});
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setPersistenceUnitName("demo");
    em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
    return em;
  }

  @Primary
  @Bean
  @ConfigurationProperties(prefix = "datasource.primary.config")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Primary
  @Bean
  public DataSource dataSource(DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder().build();
  }

  @Primary
  @Bean
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManager().getObject());
    return transactionManager;
  }

  @Primary
  @Bean
  @ConfigurationProperties(prefix = "datasource.primary.spring.flyway")
  public FlywayProperties flywayProperties() {
    return new FlywayProperties();
  }

  @Primary
  @Bean
  FlywayMigrationInitializer flywayInitializer(Flyway _flyway) {
    log.info(
        "================================================================================================");
    FlywayProperties props = flywayProperties();
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
