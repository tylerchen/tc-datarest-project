/*******************************************************************************
 * Copyright (c) Oct 9, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 9, 2016
 */
@SuppressWarnings("serial")
public class DataSourceModel implements Serializable {
    private String id;
    private String name;
    private String user;
    private String password;
    private String url;
    private String driver;
    private String validationQuery;
    private int initConnection;
    private int maxConnection;
    private String description;
    private Date createTime;
    private Date updateTime;

    public DataSourceModel() {
        super();
    }

    public static DataSourceModel create(String id, String name, String user, String password, String url,
                                         String driver, String validationQuery, int initConnection, int maxConnection, Date createTime,
                                         Date updateTime, String description) {
        DataSourceModel dataSourceModel = new DataSourceModel();
        dataSourceModel.id = id;
        dataSourceModel.name = name;
        dataSourceModel.user = user;
        dataSourceModel.password = password;
        dataSourceModel.url = url;
        dataSourceModel.driver = driver;
        dataSourceModel.validationQuery = validationQuery;
        dataSourceModel.initConnection = initConnection;
        dataSourceModel.maxConnection = maxConnection;
        dataSourceModel.createTime = createTime;
        dataSourceModel.updateTime = updateTime;
        dataSourceModel.description = description;
        return dataSourceModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public int getInitConnection() {
        return initConnection;
    }

    public void setInitConnection(int initConnection) {
        this.initConnection = initConnection;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
