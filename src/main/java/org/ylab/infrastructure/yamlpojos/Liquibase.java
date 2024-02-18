package org.ylab.infrastructure.yamlpojos;

public class Liquibase {
    private String changeLog;

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    @Override
    public String toString() {
        return "LiquibaseConfig{" +
                "changeLog='" + changeLog + '\'' +
                '}';
    }
}
