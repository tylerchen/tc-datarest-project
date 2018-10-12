/*******************************************************************************
 * Copyright (c) Sep 30, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest.core.service;

import org.iff.datarest.core.DBConnectionHoler;
import org.iff.datarest.core.DataSourceFactory;
import org.iff.datarest.core.model.DataSourceModel;
import org.iff.datarest.core.model.DescColumnModel;
import org.iff.datarest.core.model.DescTableModel;
import org.iff.datarest.core.model.QueryStatementModel;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.*;
import org.iff.infra.util.jdbc.dialet.Dialect;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;
import java.util.Map.Entry;

/**
 * <pre>
 * 1. get table description.
 * 2. getQuery
 * 3. findQuery
 * 4. pageFindQuery
 * 5. updateQuery
 * 6. deleteQuery
 * 7. getDataSource
 * 8. findDataSource
 * 9. pageFindDataSource
 * 10. updateDataSource
 * 11. deleteDataSource
 * 12. testDataSource
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 30, 2016
 */
@SuppressWarnings("unchecked")
public class ManagementService {

    public DescTableModel getTableDescByDataSourceId(String datasourceId, String table) {
        DataSourceModel dataSourceModel = getDataSourceModelById(datasourceId);
        DataSource ds = DataSourceFactory.getOrCreate(dataSourceModel);
        return getTableDesc(ds, table);
    }

    public DescTableModel getTableDescByDataSourceName(String datasourceName, String table) {
        DataSourceModel dataSourceModel = getDataSourceModelByName(datasourceName);
        DataSource ds = DataSourceFactory.getOrCreate(dataSourceModel);
        return getTableDesc(ds, table);
    }

    protected DescTableModel getTableDesc(DataSource ds, String table) {
        //		DataSource ds = DataSourceFactory.create("test", "iff", "iff",
        //				"jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
        //				"com.mysql.jdbc.Driver", "select 1", 3, 10);
        Map<String, DescTableModel> table2Desc = new LinkedHashMap<String, DescTableModel>();
        Connection conn = null;
        try {
            String catalog = "";
            String schema = "";
            conn = ds.getConnection();
            {
                DatabaseMetaData dbmd = conn.getMetaData();
                ResultSet rs = dbmd.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"});
                while (rs.next()) {
                    catalog = rs.getString("TYPE_CAT");
                    schema = rs.getString("TYPE_SCHEM");
                    String name = rs.getString("TABLE_NAME");
                    String type = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");
                    if (StringUtils.equalsIgnoreCase(name, table)) {
                        table2Desc.put(table, DescTableModel.create(catalog, schema, name, type, remarks));
                    }
                }
                rs.close();
            }
            {
                DatabaseMetaData dbmd = conn.getMetaData();
                for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                    ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, entry.getKey());
                    Map<String, String> pkMap = new HashMap<String, String>();
                    while (rs.next()) {
                        String idName = rs.getString("COLUMN_NAME");
                        pkMap.put(StringUtils.upperCase(idName), idName);
                        System.out.println("table=" + entry.getKey() + ", pk=" + idName);
                    }
                    rs.close();

                    rs = dbmd.getColumns(catalog, schema, entry.getKey(), "%");
                    while (rs.next()) {
                        String colName = rs.getString("COLUMN_NAME");
                        Integer sqlType = rs.getInt("DATA_TYPE");
                        Integer size = rs.getInt("COLUMN_SIZE");
                        Object o = rs.getObject("DECIMAL_DIGITS");
                        Integer digit = null;
                        if (o != null) {
                            digit = ((Number) o).intValue();
                        }
                        int nullable = rs.getInt("NULLABLE");
                        String defaultValue = rs.getString("COLUMN_DEF");
                        String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                        String remark = rs.getString("REMARKS");
                        colName = StringUtils.upperCase(colName);
                        entry.getValue()
                                .add(DescColumnModel.create(colName, SqlTypeMapping.getMybatisJavaType(sqlType), size,
                                        digit, defaultValue, remark,
                                        StringUtils.equalsIgnoreCase(isAutoIncrement, "NO") ? "N" : "Y",
                                        nullable == 0 ? "N" : "Y", pkMap.containsKey(colName) ? "Y" : "N"));
                        System.out.println("table=" + entry.getKey() + ", COL=" + colName + ",Type=" + sqlType
                                + ",size=" + size + ",digit=" + digit + ",nullable=" + (nullable == 0 ? "N" : "Y")
                                + ",defaultValue=" + defaultValue + ",isAutoIncrement=" + isAutoIncrement + ",desc="
                                + remark);
                    }
                    rs.close();
                }
            }
            for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                System.out.println(entry.getValue());
            }
        } catch (Exception e) {
            Exceptions.runtime("getTableDesc error!", e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return table2Desc.get(table);
    }

    public Map<String, DescTableModel> findAllTableDescByDataSourceId(String datasourceId) {
        DataSourceModel dataSourceModel = getDataSourceModelById(datasourceId);
        DataSource ds = DataSourceFactory.getOrCreate(dataSourceModel);
        return findAllTableDesc(ds);
    }

    public Map<String, DescTableModel> findAllTableDescByDataSourceName(String datasourceName) {
        DataSourceModel dataSourceModel = getDataSourceModelByName(datasourceName);
        DataSource ds = DataSourceFactory.getOrCreate(dataSourceModel);
        return findAllTableDesc(ds);
    }

    protected Map<String, DescTableModel> findAllTableDesc(DataSource ds) {
        //		DataSource ds = DataSourceFactory.create("test", "iff", "iff",
        //				"jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
        //				"com.mysql.jdbc.Driver", "select 1", 3, 10);
        Map<String, DescTableModel> table2Desc = new LinkedHashMap<String, DescTableModel>();
        Connection conn = null;
        try {
            String catalog = "";
            String schema = "";
            conn = ds.getConnection();
            {
                DatabaseMetaData dbmd = conn.getMetaData();
                ResultSet rs = dbmd.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
                while (rs.next()) {
                    catalog = rs.getString("TYPE_CAT");
                    schema = rs.getString("TYPE_SCHEM");
                    String name = rs.getString("TABLE_NAME");
                    String type = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");
                    table2Desc.put(name, DescTableModel.create(catalog, schema, name, type, remarks));
                }
                rs.close();
            }
            {
                DatabaseMetaData dbmd = conn.getMetaData();
                for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                    ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, entry.getKey());
                    Map<String, String> pkMap = new HashMap<String, String>();
                    while (rs.next()) {
                        String idName = rs.getString("COLUMN_NAME");
                        pkMap.put(StringUtils.upperCase(idName), idName);
                        System.out.println("table=" + entry.getKey() + ", pk=" + idName);
                    }
                    rs.close();

                    rs = dbmd.getColumns(catalog, schema, entry.getKey(), "%");
                    while (rs.next()) {
                        String colName = rs.getString("COLUMN_NAME");
                        Integer sqlType = rs.getInt("DATA_TYPE");
                        Integer size = rs.getInt("COLUMN_SIZE");
                        Object o = rs.getObject("DECIMAL_DIGITS");
                        Integer digit = null;
                        if (o != null) {
                            digit = ((Number) o).intValue();
                        }
                        int nullable = rs.getInt("NULLABLE");
                        String defaultValue = rs.getString("COLUMN_DEF");
                        String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                        String remark = rs.getString("REMARKS");
                        colName = StringUtils.upperCase(colName);
                        entry.getValue()
                                .add(DescColumnModel.create(colName, SqlTypeMapping.getMybatisJavaType(sqlType), size,
                                        digit, defaultValue, remark,
                                        StringUtils.equalsIgnoreCase(isAutoIncrement, "NO") ? "N" : "Y",
                                        nullable == 0 ? "N" : "Y", pkMap.containsKey(colName) ? "Y" : "N"));
                        System.out.println("table=" + entry.getKey() + ", COL=" + colName + ",Type=" + sqlType
                                + ",size=" + size + ",digit=" + digit + ",nullable=" + (nullable == 0 ? "N" : "Y")
                                + ",defaultValue=" + defaultValue + ",isAutoIncrement=" + isAutoIncrement + ",desc="
                                + remark);
                    }
                    rs.close();
                }
            }
            for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                System.out.println(entry.getValue());
            }
        } catch (Exception e) {
            Exceptions.runtime("findAllTableDesc error!", e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return table2Desc;
    }

    public QueryStatementModel getQueryStatementModelById(String id) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            Map<String, Object> result = queryRunner.query("select * from DS_QUERY_STMT where ID=?", new MapHandler(),
                    new Object[]{id});
            return BeanHelper.copyProperties(QueryStatementModel.class, result);
        } catch (Exception e) {
            Exceptions.runtime("getQueryStatementModelById error!", e);
        }
        return null;
    }

    public QueryStatementModel getQueryStatementModelByName(String name) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            Map<String, Object> result = queryRunner.query("select * from DS_QUERY_STMT where NAME=?", new MapHandler(),
                    new Object[]{name});
            return BeanHelper.copyProperties(QueryStatementModel.class, result);
        } catch (Exception e) {
            Exceptions.runtime("getQueryStatementModelByName error!", e);
        }
        return null;
    }

    public List<QueryStatementModel> findQueryStatementModel(QueryStatementModel model) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<Object>();
            {
                sql.append("select * from DS_QUERY_STMT where 1=1 ");
                if (StringUtils.isNotBlank(model.getId())) {
                    sql.append(" and ID=? ");
                    params.add(model.getId());
                }
                if (StringUtils.isNotBlank(model.getName())) {
                    sql.append(" and NAME like ? ");
                    params.add("%" + model.getName() + "%");
                }
                if (model.getCreateTime() != null) {
                    sql.append(" and CREATE_TIME >= ? ");
                    params.add(model.getCreateTime());
                }
                if (model.getUpdateTime() != null) {
                    sql.append(" and UPDATE_TIME >= ? ");
                    params.add(model.getUpdateTime());
                }
            }
            List<QueryStatementModel> models = new ArrayList<QueryStatementModel>();
            {
                List<Map<String, Object>> list = queryRunner.query(sql.toString(), new MapListHandler(),
                        params.toArray());
                for (Map<String, Object> map : list) {
                    models.add(BeanHelper.copyProperties(QueryStatementModel.class, map));
                }
            }
            return models;
        } catch (Exception e) {
            Exceptions.runtime("findQueryStatementModel error!", e);
        }
        return new ArrayList<QueryStatementModel>();
    }

    public Map<String, Object> pageFindQueryStatementModel(QueryStatementModel model, int currentPage, int pageSize) {
        try {
            DataSource dataSource = DataSourceFactory.getSystemDS();
            QueryRunner queryRunner = new QueryRunner(dataSource);
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<Object>();
            {
                sql.append("select * from DS_QUERY_STMT where 1=1 ");
                if (StringUtils.isNotBlank(model.getId())) {
                    sql.append(" and ID=? ");
                    params.add(model.getId());
                }
                if (StringUtils.isNotBlank(model.getName())) {
                    sql.append(" and NAME like ? ");
                    params.add("%" + model.getName() + "%");
                }
                if (model.getCreateTime() != null) {
                    sql.append(" and CREATE_TIME >= ? ");
                    params.add(model.getCreateTime());
                }
                if (model.getUpdateTime() != null) {
                    sql.append(" and UPDATE_TIME >= ? ");
                    params.add(model.getUpdateTime());
                }
            }
            if (dataSource instanceof BasicDataSource) {
                String countSql = "select count(*) from (" + sql.toString() + ") tmp_count";
                Number count = (Number) queryRunner.query(countSql, new ScalarHandler<Number>(1), params.toArray());
                BasicDataSource bds = (BasicDataSource) dataSource;
                String pageSql = Dialect.getInstanceByUrl(bds.getUrl()).getLimitString(sql.toString(),
                        Math.max((currentPage - 1) * pageSize, 0), pageSize);
                List<QueryStatementModel> models = new ArrayList<QueryStatementModel>();
                {
                    List<Map<String, Object>> list = queryRunner.query(pageSql, new MapListHandler(), params.toArray());
                    for (Map<String, Object> map : list) {
                        models.add(BeanHelper.copyProperties(QueryStatementModel.class, map));
                    }
                }
                return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount",
                        count.longValue(), "rows", models);
            }
        } catch (Exception e) {
            Exceptions.runtime("pageFindQueryStatementModel error!", e);
        }
        return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount", 0, "rows",
                new ArrayList<QueryStatementModel>());
    }

    public long addOrUpdateQueryStatementModel(QueryStatementModel model) {
        try {
            Connection connection = DBConnectionHoler.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            if (StringUtils.isBlank(model.getId())) {
                String sql = "insert into DS_QUERY_STMT( ID, NAME, SELECT_BODY, FROM_BODY, WHERE_BODY, DESCRIPTION, UPDATE_TIME, CREATE_TIME ) values (?, ?, ?, ?, ?, ?, ?, ?)";
                {
                    model.setId(StringHelper.uuid());
                    if (model.getCreateTime() == null) {
                        model.setCreateTime(new Date());
                    }
                    if (model.getUpdateTime() == null) {
                        model.setUpdateTime(new Date());
                    }
                }
                Map<?, ?> map = queryRunner.insert(connection, sql, new MapHandler(),
                        new Object[]{model.getId(), model.getName(), model.getSelectBody(), model.getFromBody(),
                                model.getWhereBody(), model.getDescription(), model.getCreateTime(),
                                model.getUpdateTime()});
                return 0;
            } else {
                {
                    model.setUpdateTime(new Date());
                }
                String sql = "update DS_QUERY_STMT set SELECT_BODY=?, FROM_BODY=?, WHERE_BODY=?, DESCRIPTION=?, UPDATE_TIME=? where ID=?";
                if (StringUtils.isBlank(model.getId())) {//TODO
                    sql = "update DS_QUERY_STMT set SELECT_BODY=?, FROM_BODY=?, WHERE_BODY=?, DESCRIPTION=?, UPDATE_TIME=? where NAME=?";
                }
                int count = queryRunner.update(connection, sql, model.getSelectBody(), model.getFromBody(),
                        model.getWhereBody(), model.getDescription(), model.getUpdateTime(),
                        StringUtils.defaultIfBlank(model.getId(), model.getName()));
                return count;
            }
        } catch (Exception e) {
            Exceptions.runtime("addOrUpdateQueryStatementModel error!", e);
        }
        return -1;
    }

    public long deleteQueryStatementModelById(String id) {
        try {
            Connection connection = DBConnectionHoler.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from DS_QUERY_STMT where ID=?";
            int count = queryRunner.update(connection, sql, id);
            return count;
        } catch (Exception e) {
            Exceptions.runtime("deleteQueryStatementModelById error!", e);
        }
        return -1;
    }

    public long deleteQueryStatementModelByName(String name) {
        try {
            Connection connection = DBConnectionHoler.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from DS_QUERY_STMT where NAME=?";
            int count = queryRunner.update(connection, sql, name);
            return count;
        } catch (Exception e) {
            Exceptions.runtime("deleteQueryStatementModelByName error!", e);
        }
        return -1;
    }

    public DataSourceModel getDataSourceModelById(String id) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            Map<String, Object> result = queryRunner.query("select * from DS_DATA_SOURCE where ID=?", new MapHandler(),
                    new Object[]{id});
            return BeanHelper.copyProperties(DataSourceModel.class, result);
        } catch (Exception e) {
            Exceptions.runtime("getDataSourceModelById error!", e);
        }
        return null;
    }

    public DataSourceModel getDataSourceModelByName(String name) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            Map<String, Object> result = queryRunner.query("select * from DS_DATA_SOURCE where NAME=?",
                    new MapHandler(), new Object[]{name});
            return BeanHelper.copyProperties(DataSourceModel.class, result);
        } catch (Exception e) {
            Exceptions.runtime("getDataSourceModelByName error!", e);
        }
        return null;
    }

    public List<DataSourceModel> findDataSourceModel(DataSourceModel model) {
        Assert.notNull(model, "DataSource is required!");
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<Object>();
            {
                sql.append("select * from DS_DATA_SOURCE where 1=1 ");
                if (StringUtils.isNotBlank(model.getId())) {
                    sql.append(" and ID=? ");
                    params.add(model.getId());
                }
                if (StringUtils.isNotBlank(model.getName())) {
                    sql.append(" and NAME like ? ");
                    params.add("%" + model.getName() + "%");
                }
                if (model.getCreateTime() != null) {
                    sql.append(" and CREATE_TIME >= ? ");
                    params.add(model.getCreateTime());
                }
                if (model.getUpdateTime() != null) {
                    sql.append(" and UPDATE_TIME >= ? ");
                    params.add(model.getUpdateTime());
                }
            }
            List<DataSourceModel> models = new ArrayList<DataSourceModel>();
            {
                List<Map<String, Object>> list = queryRunner.query(sql.toString(), new MapListHandler(),
                        params.toArray());
                for (Map<String, Object> map : list) {
                    models.add(BeanHelper.copyProperties(DataSourceModel.class, map));
                }
            }

            return models;
        } catch (Exception e) {
            Exceptions.runtime("findDataSourceModel error!", e);
        }
        return new ArrayList<DataSourceModel>();
    }

    public Map<String, Object> pageFindDataSourceModel(DataSourceModel model, int currentPage, int pageSize) {
        Assert.notNull(model, "DataSource is required!");
        try {
            DataSource dataSource = DataSourceFactory.getSystemDS();
            QueryRunner queryRunner = new QueryRunner(dataSource);
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<Object>();
            {
                sql.append("select * from DS_DATA_SOURCE where 1=1 ");
                if (StringUtils.isNotBlank(model.getId())) {
                    sql.append(" and ID=? ");
                    params.add(model.getId());
                }
                if (StringUtils.isNotBlank(model.getName())) {
                    sql.append(" and NAME like ? ");
                    params.add("%" + model.getName() + "%");
                }
                if (model.getCreateTime() != null) {
                    sql.append(" and CREATE_TIME >= ? ");
                    params.add(model.getCreateTime());
                }
                if (model.getUpdateTime() != null) {
                    sql.append(" and UPDATE_TIME >= ? ");
                    params.add(model.getUpdateTime());
                }
            }
            if (dataSource instanceof BasicDataSource) {
                String countSql = "select count(*) from (" + sql.toString() + ") tmp_count";
                Number count = (Number) queryRunner.query(countSql, new ScalarHandler<Number>(1), params.toArray());
                BasicDataSource bds = (BasicDataSource) dataSource;
                String pageSql = Dialect.getInstanceByUrl(bds.getUrl()).getLimitString(sql.toString(),
                        Math.max((currentPage - 1) * pageSize, 0), pageSize);

                List<DataSourceModel> models = new ArrayList<DataSourceModel>();
                {
                    List<Map<String, Object>> list = queryRunner.query(pageSql, new MapListHandler(), params.toArray());
                    for (Map<String, Object> map : list) {
                        models.add(BeanHelper.copyProperties(DataSourceModel.class, map));
                    }
                }
                return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount",
                        count.longValue(), "rows", models);
            }
        } catch (Exception e) {
            Exceptions.runtime("findDataSourceModel error!", e);
        }
        return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount", 0, "rows",
                new ArrayList<DataSourceModel>());
    }

    public long addOrUpdateDataSourceModel(DataSourceModel model) {
        Assert.notNull(model, "DataSource is required!");
        try {
            Connection connection = DBConnectionHoler.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            if (StringUtils.isBlank(model.getId())) {
                String sql = "insert into DS_DATA_SOURCE( ID, NAME, USER, PASSWORD, URL, DRIVER, VALIDATION_QUERY, INIT_CONNECTION, MAX_CONNECTION, DESCRIPTION, UPDATE_TIME, CREATE_TIME ) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                {
                    model.setId(StringHelper.uuid());
                    if (model.getCreateTime() == null) {
                        model.setCreateTime(new Date());
                    }
                    if (model.getUpdateTime() == null) {
                        model.setUpdateTime(new Date());
                    }
                }
                queryRunner.insert(connection, sql, new MapHandler(),
                        new Object[]{model.getId(), model.getName(), model.getUser(), model.getPassword(),
                                model.getUrl(), model.getDriver(), model.getValidationQuery(),
                                model.getInitConnection(), model.getMaxConnection(), model.getDescription(),
                                model.getUpdateTime(), model.getCreateTime()});
                return 0;
            } else {
                {
                    model.setUpdateTime(new Date());
                }
                String sql = "update DS_DATA_SOURCE set USER=?, PASSWORD=?, URL=?, DRIVER=?, VALIDATION_QUERY=?, INIT_CONNECTION=?, MAX_CONNECTION=?, DESCRIPTION=?, UPDATE_TIME=? where ID=?";
                if (StringUtils.isBlank(model.getId())) {//TODO
                    sql = "update DS_DATA_SOURCE set USER=?, PASSWORD=?, URL=?, DRIVER=?, VALIDATION_QUERY=?, INIT_CONNECTION=?, MAX_CONNECTION=?, DESCRIPTION=?, UPDATE_TIME=? where NAME=?";
                }
                int count = queryRunner.update(connection, sql, model.getUser(), model.getPassword(), model.getUrl(),
                        model.getDriver(), model.getValidationQuery(), model.getInitConnection(),
                        model.getMaxConnection(), model.getDescription(), model.getUpdateTime(),
                        StringUtils.defaultIfBlank(model.getId(), model.getName()));
                return count;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Exceptions.runtime("addOrUpdateDataSourceModel error!", e);
        }
        return -1;
    }

    public long deleteDataSourceModelById(String id) {
        Assert.notBlank(id, "DataSource id is required!");
        try {
            Connection connection = DBConnectionHoler.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from DS_DATA_SOURCE where ID=?";
            int count = queryRunner.update(connection, sql, id);
            return count;
        } catch (Exception e) {
            Exceptions.runtime("deleteDataSourceModelById error!", e);
        }
        return -1;
    }

    public long deleteDataSourceModelByName(String name) {
        Assert.notBlank(name, "DataSource name is required!");
        try {
            Connection connection = DBConnectionHoler.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from DS_DATA_SOURCE where NAME=?";
            int count = queryRunner.update(connection, sql, name);
            return count;
        } catch (Exception e) {
            Exceptions.runtime("deleteDataSourceModelByName error!", e);
        }
        return -1;
    }

    public boolean testDataSourceModelById(String id) {
        Assert.notBlank(id, "DataSource id is required!");
        try {
            DataSourceModel ds = getDataSourceModelById(id);
            DataSource source = DataSourceFactory.create(ds);
            QueryRunner queryRunner = new QueryRunner(source);
            Object result = queryRunner.query(ds.getValidationQuery(), new ScalarHandler<Object>(1));
            return result != null;
        } catch (Exception e) {
            Exceptions.runtime("testDataSourceModelById error!", e);
        }
        return false;
    }

    public boolean testDataSourceModelByName(String name) {
        Assert.notBlank(name, "DataSource name is required!");
        try {
            DataSourceModel ds = getDataSourceModelByName(name);
            DataSource source = DataSourceFactory.create(ds);
            QueryRunner queryRunner = new QueryRunner(source);
            Object result = queryRunner.query(ds.getValidationQuery(), new ScalarHandler<Object>(1));
            return result != null;
        } catch (Exception e) {
            Exceptions.runtime("testDataSourceModelById error!", e);
        }
        return false;
    }
}
