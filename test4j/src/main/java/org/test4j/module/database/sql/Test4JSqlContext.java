package org.test4j.module.database.sql;

import java.util.List;
import java.util.Optional;

public class Test4JSqlContext {
    private static ThreadLocal<SqlList> sqlContext = new ThreadLocal<>();
    private static ThreadLocal<Boolean> hasRecord = new ThreadLocal<>();
    private static ThreadLocal<Boolean> isTestOp = new ThreadLocal<>();

    public static void setRecordStatus(boolean status) {
        hasRecord.set(status);
    }

    public static void setTestOpStatus(boolean status) {
        isTestOp.set(status);
    }

    public static void setNextRecordStatus() {
        hasRecord.set(false);
    }


    public static boolean needRecord() {
        return !isRecord() && !isTestOp();
    }

    public static SqlList getSqlContext() {
        return Optional.ofNullable(sqlContext.get())
                .orElseGet(() -> {
                    SqlList list = new SqlList();
                    sqlContext.set(list);
                    return list;
                });
    }

    public static void addSql(String sql, Object... parameters) {
        getSqlContext().add(new SqlContext(sql, parameters));
        setRecordStatus(true);
    }

    public static void addSql(String sql, List parameters) {
        getSqlContext().add(new SqlContext(sql, parameters));
        setRecordStatus(true);
    }

    public static void clean() {
        getSqlContext().clear();
    }

    private static boolean isRecord() {
        return hasRecord.get() == null ? false : hasRecord.get();
    }

    private static boolean isTestOp() {
        return isTestOp.get() == null ? false : isTestOp.get();
    }
}
