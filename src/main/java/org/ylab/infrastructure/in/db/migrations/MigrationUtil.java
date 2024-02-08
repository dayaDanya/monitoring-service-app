package org.ylab.infrastructure.in.db.migrations;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.ylab.infrastructure.in.db.ConnectionAdapter;

import java.sql.Connection;

/**
 * Утилита для выполнения миграций базы данных с использованием Liquibase.
 */
public class MigrationUtil {

    private ConnectionAdapter connectionAdapter;

    /**
     * Конструктор для инициализации переданных параметров подключения к базе данных.
     *
     *
     *
     */
    public MigrationUtil(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    /**
     * Выполняет миграции базы данных с использованием Liquibase.
     */
    public void migrate() {
        try {
            Connection connection = connectionAdapter.getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
