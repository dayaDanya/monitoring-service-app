package org.ylab.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.yaml.snakeyaml.Yaml;
import org.ylab.infrastructure.yamlpojos.Datasource;
import org.ylab.infrastructure.yamlpojos.Liquibase;
import org.ylab.infrastructure.yamlpojos.YmlPojo;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Map;

@Configuration
public class LiquibaseConfig {


    private YmlPojo ymlPojo;
    @Autowired
    public LiquibaseConfig(YmlPojo ymlPojo) {
        this.ymlPojo = ymlPojo;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(ymlPojo.getDataSource().getDriver());
        dataSource.setUrl(ymlPojo.getDataSource().getUrl());
        dataSource.setUsername(ymlPojo.getDataSource().getUsername());
        dataSource.setPassword(ymlPojo.getDataSource().getPassword());
        return dataSource;
    }
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(ymlPojo.getLiquibase().getChangeLog());
        System.out.println("Liquibase created");
        return liquibase;
    }

}
