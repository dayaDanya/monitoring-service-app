package org.ylab.infrastructure.yamlpojos;

import java.util.Map;

public class YmlPojo {

    private Datasource datasource;
    private Liquibase liquibase;

    public Datasource getDataSource() {
        return datasource;
    }

    public void setDataSource(Datasource dataSource) {
        this.datasource = dataSource;
    }

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public void setLiquibase(Liquibase liquibase) {
        this.liquibase = liquibase;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "dataSource=" + datasource +
                ", liquibase=" + liquibase +
                '}';
    }
    public static Datasource parseDataSource(Map<String, Object> dataSourceMap) {
        Datasource dataSource = new Datasource();
        dataSource.setDriver((String) dataSourceMap.get("driver"));
        dataSource.setPassword((String) dataSourceMap.get("password"));
        dataSource.setUrl((String) dataSourceMap.get("url"));
        dataSource.setUsername((String) dataSourceMap.get("username"));
        return dataSource;
    }

    public static Liquibase parseLiquibase(Map<String, Object> liquibaseMap) {
        Liquibase liquibase = new Liquibase();
        liquibase.setChangeLog((String) liquibaseMap.get("change-log"));
        return liquibase;
    }
}
