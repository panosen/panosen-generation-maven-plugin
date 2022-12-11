package com.panosen;

public class RepositoryRequest {

    private String packageName;

    private String tableName;

    private String tableRealName;

    private String tableNameUpperCamelCase;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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
}
