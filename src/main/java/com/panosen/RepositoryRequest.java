package com.panosen;

import com.panosen.dbschema.information_schema.Table;

public class RepositoryRequest {

    private String packageName;

    private Table table;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
