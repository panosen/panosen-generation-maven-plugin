package com.panosen;

import com.panosen.codedom.java.AccessModifiers;
import com.panosen.codedom.java.CodeClass;
import com.panosen.codedom.java.CodeFile;
import com.panosen.codedom.java.CodeMethod;
import com.panosen.dbschema.information_schema.Table;

public class RepositoryService {

    public String generate(RepositoryRequest request) {

        Table table = request.getTable();

        String tableNameUpperCamelCase = NameExtension.toUpperCamelCase(table.getTableName());
        String tableRepository = tableNameUpperCamelCase + "Repository";
        String tableEntity = tableNameUpperCamelCase + "Entity";

        CodeFile codeFile = new CodeFile();
        codeFile.setPackageName(request.getPackageName());

        codeFile.addSystemImport("java.io.IOException");
        codeFile.addMavenImport("com.panosen.orm.DalTableDao");
        codeFile.addMavenImport("org.springframework.stereotype.Component");
        codeFile.addProjectImport(request.getPackageName() + ".entity." + tableEntity);

        CodeClass codeClass = codeFile.addClass(tableRepository);
        codeClass.setAccessModifiers(AccessModifiers.Public);

        codeClass.addAttribute("Component");

        codeClass.setBaseClass("DalTableDao<" + tableEntity + ">");

        CodeMethod codeMethod = codeClass.addConstructor();
        codeMethod.setAccessModifiers(AccessModifiers.Public);
        codeMethod.setName(tableRepository);
        codeMethod.stepStatement("super(" + tableEntity + ".class);");
        codeMethod.addException("IOException");

        return codeFile.transformText();
    }
}
