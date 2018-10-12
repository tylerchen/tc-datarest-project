/*******************************************************************************
 * Copyright (c) Sep 29, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest;

import org.iff.datarest.core.DataSourceFactory;
import org.iff.datarest.core.model.DataSourceModel;
import org.iff.datarest.core.service.InitTableFactory;
import org.iff.netty.server.HttpServer;
import org.iff.netty.server.handlers.RestHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.iff.infra.util.Assert;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.StringHelper;

import javax.sql.DataSource;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * <pre>
 * Start the data rest.
 * properties:
 * server.ip The server binding ip
 * server.port The server binding port
 * server.restHandlers The server handlers
 * server.path.conf Data rest conf path
 * server.path.home Data rest home, auto set by shell script
 * server.path.log4j Data rest log4j configure
 * db.url database url
 * db.user database user
 * db.password database password
 * db.driver database driver
 * db.validationQuery database validation query
 * db.initConnection database init connection count
 * db.maxConnection database max connection count
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 29, 2016
 */
public class Main {
    public static void main(String[] args) {
        try {
            Properties props = null;
            {
                BeanHelper.setUsePOVOCopyHelper(true);
                props = loadConfig();
                for (Entry<Object, Object> entry : props.entrySet()) {
                    String key = entry.getKey().toString();
                    String value = entry.getValue().toString();
                    System.setProperty(key, value);
                }
                initLog4j(props);
                initSystemDataSource(props);
                initSystemTable(props);
            }
            List<RestHandler> list = initRestHandlers(props);
            Thread server = new Thread(new HttpServer(props, props.getProperty("server.ip"),
                    NumberUtils.toInt(props.getProperty("server.port")), list, "/"));
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * init handlers.
     * server.restHandlers MUST be set.
     *
     * @param prop
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jan 23, 2017
     */
    private static List<RestHandler> initRestHandlers(Properties prop) {
        List<RestHandler> list = new ArrayList<RestHandler>();
        {
            String nameStr = prop.getProperty("server.restHandlers");
            String[] names = StringUtils.split(nameStr, ",");
            for (String name : names) {
                try {
                    Class<?> clazz = Class.forName(StringUtils.trim(name));
                    list.add((RestHandler) clazz.newInstance());
                } catch (Exception e) {
                    Exceptions.runtime("load class error", e);
                }
            }
        }
        return list;
    }

    /**
     * load properties config.
     * Configuration Home: server.path.conf, default to: conf/conf.properties.
     *
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jan 23, 2017
     */
    public static Properties loadConfig() {
        String configFile = System.getProperty("server.path.conf");
        System.out.println("read config file from server.path.conf: " + configFile);
        if (StringUtils.isBlank(configFile)) {
            String homeDir = System.getProperty("server.path.home");
            if (StringUtils.isBlank(homeDir)) {
                homeDir = new File("").getAbsolutePath();
                System.out.println("read home dir from server.path.home[Project]: " + homeDir);
                configFile = StringHelper.pathConcat(homeDir, "src/main/resources/assembly/conf/conf.properties");
                System.out.println("read config file from server.path.conf: " + configFile);
            } else {
                System.out.println("read home dir from server.path.home: " + homeDir);
                configFile = StringHelper.pathConcat(homeDir, "conf/conf.properties");
                System.out.println("read config file from server.path.conf: " + configFile);
            }
        }
        {
            String homeDir = System.getProperty("server.path.home");
            if (StringUtils.isBlank(homeDir)) {
                homeDir = new File("").getAbsolutePath();
                System.setProperty("server.path.home", homeDir);
            }
        }
        Properties prop = new Properties();
        InputStream io = null;
        try {
            io = new FileInputStream(new File(configFile));
            BufferedReader br = new BufferedReader(new InputStreamReader(io, "UTF-8"));
            prop.load(br);
            prop.setProperty("server.path.home", System.getProperty("server.path.home"));
        } catch (Exception e) {
            Exceptions.runtime("can't load the config file:" + configFile, e);
        } finally {
            try {
                io.close();
            } catch (Exception e2) {
            }
        }
        {
            Set<Entry<Object, Object>> entrySet = System.getProperties().entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                prop.put(entry.getKey(), entry.getValue());
            }
        }
        return prop;
    }

    /**
     * init log4j.
     * Log4j Home: server.path.log4j, default to: conf/log4j.xml.
     *
     * @param prop
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jan 23, 2017
     */
    public static void initLog4j(Properties prop) {
        String log4j = prop.getProperty("server.path.log4j");
        System.out.println("read log4j config from server.path.log4j: " + log4j);
        if (StringUtils.isBlank(log4j)) {
            String homeDir = prop.getProperty("server.path.home");
            if (StringUtils.isBlank(homeDir)) {
                homeDir = new File("").getAbsolutePath();
                System.out.println("read home dir from server.path.home: " + homeDir);
                log4j = StringHelper.pathConcat(homeDir, "src/main/resources/assembly/conf/log4j.xml");
                System.out.println("read log4j config from server.path.log4j[Project]: " + log4j);
            } else {
                System.out.println("read home dir from server.path.home: " + homeDir);
                log4j = StringHelper.pathConcat(homeDir, "conf/log4j.xml");
                System.out.println("read log4j config from server.path.log4j: " + log4j);
            }
        }
        /*
         * BasicConfigurator.configure ()： 自动快速地使用缺省Log4j环境。
         * PropertyConfigurator.configure ( String configFilename) ：读取使用Java的特性文件编写的配置文件。
         * DOMConfigurator.configure ( String filename ) ：读取XML形式的配置文件。
         */
        DOMConfigurator.configure(log4j);
    }

    /**
     * init data source.
     *
     * @param prop
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jan 23, 2017
     */
    public static void initSystemDataSource(Properties prop) {
        Map<String, String> map = new HashMap<String, String>();
        for (Entry<Object, Object> entry : prop.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            if (key.startsWith("db.")) {
                map.put(key.substring(3), value);
            }
        }
        DataSourceModel model = BeanHelper.copyProperties(DataSourceModel.class, map);
        DataSource dataSource = DataSourceFactory.getOrCreate(model);
        Assert.notNull(dataSource, "Can't create system datasource: " + map);
    }

    /**
     * create or init database.
     *
     * @param prop
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jan 23, 2017
     */
    public static void initSystemTable(Properties prop) {
        String url = prop.getProperty("db.url");
        Assert.notBlank(url);
        String databaseType = "mysql";
        if (url == null || StringUtils.isBlank(url)) {
            databaseType = "mysql";
        } else if (url.startsWith("jdbc:mysql:")) {
            databaseType = "mysql";
        } else if (url.startsWith("jdbc:oracle:")) {
            databaseType = "oracle";
        } else if (url.startsWith("jdbc:sqlserver:")) {
            databaseType = "sqlserver";
        } else if (url.startsWith("jdbc:h2:")) {
            databaseType = "h2";
        } else if (url.startsWith("jdbc:db2:")) {
            databaseType = "db2";
        } else if (url.startsWith("jdbc:postgresql:")) {
            databaseType = "postgresql";
        }
        InitTableFactory.initByDatabaseType(databaseType);
    }

}
