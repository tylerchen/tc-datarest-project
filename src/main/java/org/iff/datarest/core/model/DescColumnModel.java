package org.iff.datarest.core.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DescColumnModel implements Serializable {
    private String name;
    private String type;
    private Integer size;
    private Integer digit;
    private String defaultValue;
    private String remarks;
    private String isAutoIncrement;
    private String isNullable;
    private String isPrimaryKey;

    public DescColumnModel() {
        super();
    }

    public static DescColumnModel create(String name, String type, Integer size, Integer digit, String defaultValue,
                                         String remarks, String isAutoIncrement, String isNullable, String isPrimaryKey) {
        DescColumnModel descColumn = new DescColumnModel();
        descColumn.name = name;
        descColumn.type = type;
        descColumn.size = size;
        descColumn.digit = digit;
        descColumn.defaultValue = defaultValue;
        descColumn.remarks = remarks;
        descColumn.isAutoIncrement = isAutoIncrement;
        descColumn.isNullable = isNullable;
        descColumn.isPrimaryKey = isPrimaryKey;
        return descColumn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getDigit() {
        return digit;
    }

    public void setDigit(Integer digit) {
        this.digit = digit;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(String isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    @Override
    public String toString() {
        return "DescColumn [name=" + name + ", type=" + type + ", size=" + size + ", digit=" + digit + ", defaultValue="
                + defaultValue + ", remarks=" + remarks + ", isAutoIncrement=" + isAutoIncrement + ", isNullable="
                + isNullable + ", isPrimaryKey=" + isPrimaryKey + "]";
    }
}