//package org.ylab.infrastructure.in.db.migrations;
//
//import jakarta.servlet.ServletContextEvent;
//import jakarta.servlet.ServletContextListener;
//import org.ylab.infrastructure.in.db.ConnectionAdapter;
//
///**
// * Класс реализующий ServletContextListener.
// *
// */
//public class MigrationExecutor implements ServletContextListener {
//    /**
//     * Метод выполнящий миграции при запуске приложения
//     * @param sce запуск
//     */
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        MigrationUtil migrationUtil = new MigrationUtil(new ConnectionAdapter());
//        migrationUtil.migrate();
//
//    }
//}
