package com.panosen;

import com.panosen.codedom.java.*;
import com.panosen.codedom.java.engine.JavaTypeConstant;
import com.panosen.dbschema.information_schema.Column;
import com.panosen.dbschema.information_schema.Table;

public class EntityService {

    public String generate(EntityRequest request) {

        Table table = request.getTable();

        String tableNameUpperCamelCase = NameExtension.toUpperCamelCase(table.getTableName());
        String tableEntity = tableNameUpperCamelCase + "Entity";

        CodeFile codeFile = new CodeFile();
        codeFile.setPackageName(request.getPackageName());

        codeFile.addSystemImport("java.sql.Timestamp");
        codeFile.addSystemImport("java.sql.Types");

        codeFile.addMavenImport("com.panosen.orm.annonation.*");

        {
            CodeClass codeClass = codeFile.addClass(tableEntity);
            codeClass.setAccessModifiers(AccessModifiers.Public);

            codeClass.addAttribute("Entity");
            codeClass.addAttribute("DataSource")
                    .AddStringParam("name", request.getDatabaseName());
            codeClass.addAttribute("Table")
                    .AddStringParam("name", table.getTableName());

            if (request.getTableColumnList() != null && !request.getTableColumnList().isEmpty()) {
                for (Column column : request.getTableColumnList()) {
                    String propertyName = NameExtension.toUpperCamelCase(column.getColumnName());
                    CodeProperty codeProperty = codeClass.addProperty(buildPropertyType(column.getDataType()), propertyName);

                    boolean isPrimary = request.getTableStatisticsList() != null && !request.getTableStatisticsList().isEmpty()
                            && request.getTableStatisticsList().stream().anyMatch(v -> v.getColumnName().equals(column.getColumnName()) && v.getIndexName().equals("PRIMARY"));
                    if (isPrimary) {
                        codeProperty.addAttribute("Id");
                    }

                    codeProperty.addAttribute("Column")
                            .AddStringParam("name", column.getColumnName());
                    codeProperty.addAttribute("Type")
                            .AddPlainParam("type", buildType(column.getDataType()));
                }
            }
        }

        return codeFile.transformText();
    }

    private String buildPropertyType(String dataType) {
        switch (dataType) {
            case "int":
                return JavaTypeConstant._INTEGER;
            case "bigint":
                return JavaTypeConstant._LONG;
            case "varchar":
                return JavaTypeConstant.STRING;
            case "datetime":
                return "Timestamp";
            default:
                return "";
        }
    }

    private String buildType(String dataType) {
        switch (dataType) {
            case "int":
                return "Types.INTEGER";
            case "bigint":
                return "Types.BIGINT";
            case "varchar":
                return "Types.VARCHAR";
            case "datetime":
                return "Types.TIMESTAMP";
            default:
                return "";
        }
    }
}
