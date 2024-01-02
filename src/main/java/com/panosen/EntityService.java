package com.panosen;

import com.panosen.codedom.java.AccessModifiers;
import com.panosen.codedom.java.CodeClass;
import com.panosen.codedom.java.CodeFile;
import com.panosen.codedom.java.CodeProperty;
import com.panosen.codedom.java.engine.GenerateOptions;
import com.panosen.dbschema.information_schema.Column;

public class EntityService {

    public String generate(EntityRequest request) {

        String tableEntity = request.getTableNameUpperCamelCase() + "Entity";

        CodeFile codeFile = new CodeFile();
        codeFile.setPackageName(request.getPackageName());

        codeFile.addImport("java.sql.Timestamp");
        codeFile.addImport("java.sql.Types");
        codeFile.addImport("java.math.BigDecimal");

        codeFile.addImport("com.panosen.orm.annonation.*");

        {
            CodeClass codeClass = codeFile.addClass(tableEntity);
            codeClass.setAccessModifiers(AccessModifiers.Public);

            codeClass.addAttribute("Entity");
            codeClass.addAttribute("DataSource")
                    .addStringParam("name", request.getDatabaseName());
            codeClass.addAttribute("Table")
                    .addStringParam("name", request.getTableRealName());

            if (request.getTableColumnList() != null && !request.getTableColumnList().isEmpty()) {
                for (Column column : request.getTableColumnList()) {
                    String propertyName = NameExtension.toUpperCamelCase(column.getColumnName());
                    CodeProperty codeProperty = codeClass.addProperty(buildPropertyType(column.getDataType()), propertyName);
                    codeProperty.setSummary(column.getColumnComment());

                    boolean isPrimary = request.getTableStatisticsList() != null && !request.getTableStatisticsList().isEmpty()
                            && request.getTableStatisticsList().stream().anyMatch(v -> v.getColumnName().equals(column.getColumnName()) && v.getIndexName().equals("PRIMARY"));
                    if (isPrimary) {
                        codeProperty.addAttribute("Id");
                    }

                    codeProperty.addAttribute("Column")
                            .addStringParam("name", column.getColumnName());
                    codeProperty.addAttribute("Type")
                            .addPlainParam("type", buildType(column.getDataType()));
                }
            }
        }

        GenerateOptions generateOptions = new GenerateOptions();
        generateOptions.setConcision(request.isConcision());
        return codeFile.transformText(generateOptions);
    }

    private String buildPropertyType(String dataType) {
        switch (dataType) {
            case "int":
                return "Integer";
            case "bigint":
                return "Long";

            case "datetime":
            case "timestamp":
                return "Timestamp";

            case "varchar":
                return "String";

            case "text":
            case "mediumtext":
            case "longtext":
                return "String";

            case "decimal":
                return "BigDecimal";
            case "bit":
                return "Boolean";
            case "tinyint":
                return "Integer";

            default:
                return "error";
        }
    }

    private String buildType(String dataType) {
        switch (dataType) {
            case "int":
                return "Types.INTEGER";
            case "bigint":
                return "Types.BIGINT";

            case "datetime":
            case "timestamp":
                return "Types.TIMESTAMP";

            case "varchar":
                return "Types.VARCHAR";

            case "text":
            case "mediumtext":
            case "longtext":
                return "Types.LONGVARCHAR";

            case "decimal":
                return "Types.DECIMAL";
            case "bit":
                return "Types.BIT";
            case "tinyint":
                return "Types.TINYINT";

            default:
                return "error";
        }
    }
}
