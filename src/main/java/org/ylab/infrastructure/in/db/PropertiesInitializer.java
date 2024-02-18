package org.ylab.infrastructure.in.db;

import org.yaml.snakeyaml.Yaml;
import org.ylab.infrastructure.yamlpojos.YmlPojo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static org.ylab.infrastructure.yamlpojos.YmlPojo.parseDataSource;
import static org.ylab.infrastructure.yamlpojos.YmlPojo.parseLiquibase;

/**
 * Класс предоставляющий метод инициализации объекта Properties
 */
public class PropertiesInitializer {
    /**
     * Метод инициализирующий объект YmlPojo из application.yml
     * @return YmlPojo инициализированный объект
     */

    public static YmlPojo initialize() {
        Properties properties = new Properties();

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("application.yml");) {

            Yaml yaml = new Yaml();
            Map<String, Object> map = yaml.load(inputStream);
            YmlPojo ymlPojo = new YmlPojo();
            ymlPojo.setDataSource(parseDataSource((Map<String, Object>) map.get("datasource")));
            ymlPojo.setLiquibase(parseLiquibase((Map<String, Object>) map.get("liquibase")));
            return ymlPojo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
//    public static Yaml initialize(){
//        Yaml yaml = new Yaml();
//        try (InputStream in = Thread.currentThread().getContextClassLoader()
//                .getResourceAsStream("application.yaml")) {
//            yaml.load(in);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return yaml;
//    }
}
