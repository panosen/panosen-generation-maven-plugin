package com.panosen;

import com.panosen.codedom.java.*;
import com.panosen.dbschema.information_schema.Column;

public class EntityFieldsService {

    public String generate(EntityFieldsRequest request) {

        String tableEntity = request.getTableNameUpperCamelCase() + "Fields";

        CodeFile codeFile = new CodeFile();
        codeFile.setPackageName(request.getPackageName());

        {
            CodeClass codeClass = codeFile.addClass(tableEntity);
            codeClass.setAccessModifiers(AccessModifiers.Public);
            codeClass.setIsFinal(true);

            if (request.getTableColumnList() != null && !request.getTableColumnList().isEmpty()) {
                for (Column column : request.getTableColumnList()) {
                    String propertyName = NameExtension.toUpperCaseUnderLine(column.getColumnName());

                    CodeField codeField = codeClass.addField("String", propertyName);
                    codeField.setAccessModifiers(AccessModifiers.Public);
                    codeField.setIsStatic(true);
                    codeField.setIsFinal(true);
                    codeField.addStringValue(column.getColumnName());
                }
            }
        }

        return codeFile.transformText();
    }
}
