/*******************************************************************************
 * Copyright (c) Sep 29, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.iff.datarest.core.*;
import org.iff.datarest.core.model.DataSourceModel;
import org.iff.datarest.util.SessionHelper;
import org.iff.datarest.util.SystemHelper;
import org.iff.datarest.util.Tuple;
import org.iff.infra.util.*;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.netty.server.HttpServer;
import org.iff.netty.server.handlers.ActionHandler;

import javax.sql.DataSource;
import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import static org.iff.datarest.util.SystemHelper.*;

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
 * <pre>
 *     -> step 1 Main
 *        step 1.1 HttpServer
 *        step 1.1.1 HttpServerInitializer
 *        step 1.1.1.1 HttpServerInboundHandler
 *        step 1.1.1.1.1 ProcessContext
 *        step 1.1.1.1.1.1 RestHandler
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 29, 2016
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Properties props = null;
            {
                BeanHelper.setUsePOVOCopyHelper(true);
                // # step 1.0
                props = loadConfig();
                for (Entry<Object, Object> entry : props.entrySet()) {
                    String key = entry.getKey().toString();
                    String value = entry.getValue().toString();
                    System.setProperty(key, value);
                }
                // # step 1.1
                initLog4j(props);
                // # step 1.2
                //initSystemDataSource(props);
                // # step 1.3
                //initSystemTable(props);
            }
            // # step 1.4
            initApplication();
            // # step 1.5
            List<ActionHandler> list = initActionHandlers(props);
            // # step 1.6
            new HttpServer(props, props.getProperty(PROP_SERVER_IP),
                    NumberUtils.toInt(props.getProperty(PROP_SERVER_PORT)), list, "/").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * # step 1.0
     * load properties config.
     * Configuration Home: server.path.conf, default to: conf/conf.properties.
     *
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jan 23, 2017
     */
    public static Properties loadConfig() {
        File file = SystemHelper.find(PROP_SERVER_PATH_CONF, new String[]{PATH_CONF, PATH_CONF_DEV});
        if (file == null) {
            Exceptions.runtime("Main can't load the config file from : " + PATH_CONF + " or " + PATH_CONF_DEV, "FAIL-1001");
        }
        System.out.println("Main read config file from : " + file.getAbsolutePath());
        Properties prop = load(file);
        if (prop == null) {
            Exceptions.runtime("Main can't load the config file from : " + PATH_CONF + " or " + PATH_CONF_DEV, "FAIL-1001");
        }
        prop.setProperty(PROP_SERVER_PATH_HOME, System.getProperty(PROP_SERVER_PATH_HOME));
        {
            Set<Entry<Object, Object>> entrySet = System.getProperties().entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                prop.put(entry.getKey(), entry.getValue());
            }
        }
        SystemHelper.initProp(prop);
        return prop;
    }

    /**
     * # step 1.1
     * init log4j.
     * Log4j Home: server.path.log4j, default to: conf/log4j.xml.
     *
     * @param prop
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jan 23, 2017
     */
    public static void initLog4j(Properties prop) {
        File log4j = SystemHelper.find(PROP_SERVER_PATH_LOG4J, new String[]{PATH_CONF_LOG4J, PATH_CONF_LOG4J_DEV});
        if (log4j == null) {
            Exceptions.runtime(FCS.get("Main Can't find log4j file from {1} or {2}\nSpecify in parameter: {3}", PATH_CONF_LOG4J, PATH_CONF_LOG4J_DEV, PATH_CONF_LOG4J), "FAIL-1001");
        }
        System.out.println("Main read log4j config from : " + log4j.getAbsolutePath());
        /*
         * BasicConfigurator.configure ()： 自动快速地使用缺省Log4j环境。
         * PropertyConfigurator.configure ( String configFilename) ：读取使用Java的特性文件编写的配置文件。
         * DOMConfigurator.configure ( String filename ) ：读取XML形式的配置文件。
         */
        DOMConfigurator.configure(log4j.getAbsolutePath());
    }

    /**
     * # step 1.2
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
     * # step 1.3
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
        //InitTableFactory.initByDatabaseType(databaseType);
    }

    /**
     * # step 1.4
     * Application init put here
     *
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @date 2018-10-20
     * @since 2018-10-20
     */
    private static void initApplication() {
        EventBusHelper.me().init();
        EventBusHelper.me().regist(DomainEventService.EVENT_NAME, new DomainEventService());
        EventBusHelper.me().regist(DDLEventService.EVENT_NAME, new DDLEventService());
        EventBusHelper.me().regist(SystemEventService.EVENT_NAME, new SystemEventService());
        CacheHelper.init(new CacheHelper.MapCacheable());
        BeanHelper.setUsePOVOCopyHelper(true);
        {//fill query to cache
            File file = find("", new String[]{PATH_QUERY, PATH_QUERY_DEV});
            if (file != null) {
                Properties props = load(file);
                for (Map.Entry entry : props.entrySet()) {
                    CacheHelper.getCache().set((String) entry.getKey(), entry.getValue());
                }
            }
        }
        {
            SystemEventService.Tuple<Integer> tuple = SystemEventService.Tuple.createTableDataSourceModel();
            EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, tuple);
            MyBatisSqlSessionFactory.done(tuple.hasError());
            if (tuple.hasError()) {
                //Logger.warn("Main createTableDataSourceModel error!", tuple.error());
            }
        }
        {
            SystemEventService.Tuple<Integer> tuple = SystemEventService.Tuple.createTableDomainQueryModel();
            EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, tuple);
            MyBatisSqlSessionFactory.done(tuple.hasError());
            if (tuple.hasError()) {
                //Logger.warn("Main createTableDomainQueryModel error!", tuple.error());
            }
        }
        {
            SystemEventService.Tuple<Integer> tuple = SystemEventService.Tuple.createTableDomainParamModel();
            EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, tuple);
            MyBatisSqlSessionFactory.done(tuple.hasError());
            if (tuple.hasError()) {
                //Logger.warn("Main createTableDomainParamModel error!", tuple.error());
            }
        }
        {
            DataSourceModel dsm = new DataSourceModel().init("IFF_REST_SYSTEM", "DataSourceModel");
            {
                dsm.setId(IdHelper.uuid());
                dsm.setCreateTime(new Date());
                dsm.setUpdateTime(new Date());
                dsm.setDriver("com.mysql.jdbc.Driver");
                dsm.setInitConnection(3);
                dsm.setMaxConnection(10);
                dsm.setName("TEST-" + System.currentTimeMillis());
                dsm.setPassword("iff");
                dsm.setUrl("jdbc:mysql://localhost:3306/datarest?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false");
                dsm.setUser("iff");
                dsm.setValidationQuery("select 1");
                dsm.setDescription("test");
                dsm.setNamespace("IFF_REST_SYSTEM");
                dsm.setDdecryptKey("test");
            }

            Tuple.One<DataSourceModel, DataSourceModel> tuple = new Tuple.One<>(dsm);
            SessionHelper.callback(new SessionHelper.Callback<Tuple.One<DataSourceModel, DataSourceModel>, DataSourceModel>() {
                public DataSourceModel callback(Tuple.One<DataSourceModel, DataSourceModel> param) {
                    return param.first().save();
                }
            }, tuple);
            if (tuple.hasError()) {
                tuple.error().printStackTrace();
            } else {
                System.out.println(GsonHelper.toJsonString(dsm));
            }
        }
        {
            DataSourceModel dsm = new DataSourceModel();
            {
                //dsm.setNamespace("IFF_REST_SYSTEM");
                dsm.setName("TEST");
            }
            Page page = Page.pageable(10, 0, 0, null);
            page.addDescOrderBy("name");
            String sql = CacheHelper.get(key("DataSourceModel", "find", "IFF_REST_SYSTEM"));
            DomainEventService.Tuple<Page> tuple = DomainEventService.Tuple.queryPageByDsl("IFF_REST_SYSTEM", sql, MapHelper.toMap("vo", dsm, "page", page));
            SessionHelper.sync(DomainEventService.EVENT_NAME, tuple);
            if (tuple.hasError()) {
                tuple.error().printStackTrace();
            } else {
                System.out.println(GsonHelper.toJsonString(tuple.result()));
            }
        }
    }

    /**
     * # step 1.5
     * init handlers.
     * server.restHandlers MUST be set.
     *
     * @param prop
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jan 23, 2017
     */
    private static List<ActionHandler> initActionHandlers(Properties prop) {
        List<ActionHandler> list = new ArrayList<ActionHandler>();
        {
            String nameStr = prop.getProperty(PROP_SERVER_ACTION_HANDLERS);
            String[] names = StringUtils.split(nameStr, ",");
            for (String name : names) {
                try {
                    Class<?> clazz = Class.forName(StringUtils.trim(name));
                    list.add((ActionHandler) clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    Exceptions.runtime("load class error", e);
                }
            }
        }
        return list;
    }
}
