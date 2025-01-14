package org.test4j.module.database.environment;

import static org.test4j.module.core.internal.IPropItem.*;
import static org.test4j.module.database.utility.DataSourceType.*;

import java.sql.SQLException;
import java.util.*;

import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.environment.types.DerbyEnvironment;
import org.test4j.module.database.environment.types.MySqlEnvironment;
import org.test4j.module.database.environment.types.OracleEnvironment;
import org.test4j.module.database.environment.types.SqlServerEnvironment;
import org.test4j.module.database.utility.DataSourceType;
import org.test4j.tools.commons.ConfigHelper;

public final class DBEnvironmentFactory {
    private static Map<String, DBEnvironment> environments = new HashMap<String, DBEnvironment>();

    private static DBEnvironment createDBEnvironment(String dataSourceName) {
        String typeProperty = ConfigHelper.getDataSourceKey(dataSourceName, PROP_KEY_DATASOURCE_TYPE);
        DataSourceType dataSourceType = databaseType(typeProperty);
        String schema = ConfigHelper.getDataSourceKey(dataSourceName, PROP_KEY_DATASOURCE_SCHEMA);

        DBEnvironment environment = newInstance(dataSourceType, dataSourceName, schema);
        environments.put(dataSourceName, environment);
        return environment;
    }

    private static DBEnvironment newInstance(DataSourceType dataSourceType, String dataSourceName, String dataSourceSchema) {
        if (dataSourceType == null) {
            throw new RuntimeException("DatabaseType can't be null.");
        }
        switch (dataSourceType) {
            case MYSQL:
            case H2DB:
                return new MySqlEnvironment(dataSourceName, dataSourceSchema);
            case ORACLE:
                return new OracleEnvironment(dataSourceName, dataSourceSchema);
            case SQLSERVER:
                return new SqlServerEnvironment(dataSourceName, dataSourceSchema);
            case DERBYDB:
                return new DerbyEnvironment(dataSourceName, dataSourceSchema);
            default:
                throw new RuntimeException("unsupport database type:" + dataSourceType.name());
        }
    }

    public static DBEnvironment getDefaultDBEnvironment() {
        String defaultDataSource = ConfigHelper.getDefaultDataSource();
        return getDBEnvironment(defaultDataSource);
    }

    /**
     * 从test4j配置中取指定的数据源
     *
     * @param dataSourceName
     * @return
     */
    public static DBEnvironment getDBEnvironment(String dataSourceName) {
        return Optional.ofNullable(environments.get(dataSourceName))
                .orElseGet(() -> createDBEnvironment(dataSourceName));
    }

    /**
     * 结束所有可能的事务
     *
     * @throws SQLException
     */
    public static void closeDBEnvironment() {
        for (Map.Entry<String, DBEnvironment> environment : environments.entrySet()) {
            try {
                environment.getValue().commit();
            } catch (Throwable e) {
                MessageHelper.warn("commit transactional error: " + e.getMessage());
            }
        }
    }
}
