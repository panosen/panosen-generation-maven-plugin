package com.panosen;

import com.panosen.dbschema.information_schema.Column;
import com.panosen.dbschema.information_schema.Statistics;
import com.panosen.dbschema.information_schema.Table;

import java.util.List;

public class EntityRequest {

    private String packageName;

    private String databaseName;

    private String tableName;

    private String tableRealName;

    private String tableNameUpperCamelCase;

    private List<Column> tableColumnList;

    private List<Statistics> tableStatisticsList;

    private boolean concision;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<Column> getTableColumnList() {
        return tableColumnList;
    }

    public void setTableColumnList(List<Column> tableColumnList) {
        this.tableColumnList = tableColumnList;
    }

    public List<Statistics> getTableStatisticsList() {
        return tableStatisticsList;
    }

    public void setTableStatisticsList(List<Statistics> tableStatisticsList) {
        this.tableStatisticsList = tableStatisticsList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableRealName() {
        return tableRealName;
    }

    public void setTableRealName(String tableRealName) {
        this.tableRealName = tableRealName;
    }

    public String getTableNameUpperCamelCase() {
        return tableNameUpperCamelCase;
    }

    public void setTableNameUpperCamelCase(String tableNameUpperCamelCase) {
        this.tableNameUpperCamelCase = tableNameUpperCamelCase;
    }

    public boolean isConcision() {
        return concision;
    }

    public void setConcision(boolean concision) {
        this.concision = concision;
    }
}
