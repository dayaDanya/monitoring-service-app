package org.ylab.infrastructure.in.db.migrations;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.ylab.infrastructure.in.db.ConnectionAdapter;


public class MigrationExecutor implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MigrationUtil migrationUtil = new MigrationUtil(new ConnectionAdapter());
        migrationUtil.migrate();

    }
}
