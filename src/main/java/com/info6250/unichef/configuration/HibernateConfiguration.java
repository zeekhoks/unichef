package com.info6250.unichef.configuration;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfiguration {

    @Value("${custom.datasource.url}")
    private String db_url;

    @Value("${custom.datasource.username}")
    private String db_username;

    @Value("${custom.datasource.password}")
    private String db_password;

    @Value("${custom.datasource.driver-class-name}")
    private String db_class;

    @Value("${custom.hibernate.set-hbm-ddl}")
    private String hbm;

    @Value("${custom.hibernate.set-hbm-ddl-value}")
    private String hbm_value;

    @Value("${custom.hibernate.set-dialect}")
    private String dialect;

    @Value("${custom.hibernate.set-dialect-value}")
    private String dialect_value;


    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.info6250.unichef.model");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(db_class);
        dataSource.setUrl(db_url);
        dataSource.setUsername(db_username);
        dataSource.setPassword(db_password);

        return dataSource;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                hbm, hbm_value);
        hibernateProperties.setProperty(
                dialect, dialect_value);
        return hibernateProperties;
    }


}
