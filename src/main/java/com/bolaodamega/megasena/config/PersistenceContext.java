package com.bolaodamega.megasena.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = { "com.bolaodamega.megasena.domain", "com.bolaodamega.megasena.repository", "com.bolaodamega.megasena.service" })
@EnableTransactionManagement
public class PersistenceContext {

    @Bean(destroyMethod = "close")
    DataSource dataSource(Environment env) {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(env
                .getRequiredProperty("db.driver"));
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty("db.url"));
        dataSourceConfig.setUsername(env.getRequiredProperty("db.username"));
        dataSourceConfig.setPassword(env.getRequiredProperty("db.password"));
        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, Environment env) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean
                .setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean
                .setPackagesToScan("com.bolaodamega.megasena");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect",
                env.getRequiredProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.hbm2ddl.auto",
                env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.show_sql",
                env.getRequiredProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.format_sql",
                env.getRequiredProperty("hibernate.format_sql"));
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}