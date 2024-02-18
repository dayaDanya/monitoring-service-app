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

//    @Value("${liquibase.change-log}")
//    private String changeLog;
//
//    @Value("${datasource.url}")
//    private String url;
//    @Value("${datasource.username}")
//    private String username;
//    @Value("${datasource.password}")
//    private String password;
//    @Value("${datasource.driver}")
//    private String driver;
    private YmlPojo ymlPojo;
    @Autowired
    public LiquibaseConfig(YmlPojo ymlPojo) {
        this.ymlPojo = ymlPojo;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(changeLog);
        System.out.println("Liquibase created");
        return liquibase;
    }

}
