package com.panosen;

import com.panosen.dbschema.information_schema.Column;
import com.panosen.dbschema.information_schema.Statistics;
import com.panosen.dbschema.information_schema.Table;

import java.util.List;

public class EntityRequest {

    private String packageName;

    private String databaseName;

    private Table table;

    private List<Column> tableColumnList;

    private List<Statistics> tableStatisticsList;

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

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
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
}
