/*******************************************************************************
 * Copyright (c) Sep 26, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest.core;

import org.iff.datarest.core.model.DataSourceModel;
import org.apache.commons.dbcp.BasicDataSource;
import org.iff.infra.util.Assert;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.MapHelper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 26, 2016
 */
public class DataSourceFactory {
    private static Map<String, DataSource> dataSourceCacheMap = new HashMap<String, DataSource>();

    public static DataSource get(String name) {
        Assert.notBlank(name);
        return dataSourceCacheMap.get(name);
    }

    public static DataSource getSystemDS() {
        DataSource dataSource = dataSourceCacheMap.get("_default_");
        if (dataSource == null) {
            dataSource = create("_default_", "iff", "iff",
                    "jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
                    "com.mysql.jdbc.Driver", "select 1", 3, 10);
        }
        return dataSource;
    }

    public static DataSource create(String name, String user, String password, String url, String driver,
                                    String validationQuery, int initConnection, int maxConnection) {
        Assert.notBlank(name);
        Assert.notBlank(user);
        Assert.notBlank(url);
        Assert.notBlank(driver);
        Assert.notBlank(validationQuery);
        Properties props = new Properties();
        {
            Map map = MapHelper.toMap(/**/
                    "username", user, /**/
                    "password", password, /**/
                    "url", url, /**/
                    "driverClassName", driver, /**/
                    "defaultAutoCommit", "false", /**/
                    "initialSize", String.valueOf(Math.max(initConnection, 3)), /**/
                    "maxActive", String.valueOf(Math.max(maxConnection, 3)), /**/
                    "maxWait", "60000", /**/
                    "validationQuery", validationQuery, /**/
                    "testWhileIdle", "true", /**/
                    "testOnBorrow", "false", /**/
                    "testOnReturn", "false" /**/
            );
            props.putAll(map);
        }
        try {
            DataSource dataSource = org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(props);
            {
                DataSource ds = dataSourceCacheMap.get(name);
                if (ds != null && ds instanceof BasicDataSource) {
                    ((BasicDataSource) ds).close();
                }
            }
            dataSourceCacheMap.put(name, dataSource);
            return dataSource;
        } catch (Exception e) {
            Exceptions.runtime("Error invoke BasicDataSourceFactory.createDataSource", e);
        }
        return null;
    }

    public static DataSource create(DataSourceModel model) {
        Assert.notNull(model);
        Assert.notBlank(model.getName());
        Assert.notBlank(model.getUser());
        Assert.notBlank(model.getUrl());
        Assert.notBlank(model.getDriver());
        Assert.notBlank(model.getValidationQuery());
        Properties props = new Properties();
        {
            Map map = MapHelper.toMap(/**/
                    "username", model.getUser(), /**/
                    "password", model.getPassword(), /**/
                    "url", model.getUrl(), /**/
                    "driverClassName", model.getDriver(), /**/
                    "defaultAutoCommit", "false", /**/
                    "initialSize", String.valueOf(Math.max(model.getInitConnection(), 3)), /**/
                    "maxActive", String.valueOf(Math.max(model.getMaxConnection(), 3)), /**/
                    "maxWait", "60000", /**/
                    "validationQuery", model.getValidationQuery(), /**/
                    "testWhileIdle", "true", /**/
                    "testOnBorrow", "false", /**/
                    "testOnReturn", "false" /**/
            );
            props.putAll(map);
        }
        try {
            DataSource dataSource = org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(props);
            {
                DataSource ds = dataSourceCacheMap.get(model.getName());
                if (ds != null && ds instanceof BasicDataSource) {
                    ((BasicDataSource) ds).close();
                }
            }
            dataSourceCacheMap.put(model.getName(), dataSource);
            return dataSource;
        } catch (Exception e) {
            e.printStackTrace();
            Exceptions.runtime("Error invoke BasicDataSourceFactory.createDataSource", e);
        }
        return null;
    }

    public static DataSource getOrCreate(DataSourceModel model) {
        DataSource dataSource = get(model.getName());
        if (dataSource == null) {
            dataSource = create(model);
        }
        return dataSource;
    }
}
