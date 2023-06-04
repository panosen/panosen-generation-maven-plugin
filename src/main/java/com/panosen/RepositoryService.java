package com.panosen;

import com.panosen.codedom.java.AccessModifiers;
import com.panosen.codedom.java.CodeClass;
import com.panosen.codedom.java.CodeFile;
import com.panosen.codedom.java.CodeMethod;
import com.panosen.dbschema.information_schema.Table;

public class RepositoryService {

    public String generate(RepositoryRequest request) {

        String tableRepository = request.getTableNameUpperCamelCase() + "Repository";
        String tableEntity = request.getTableNameUpperCamelCase() + "Entity";

        CodeFile codeFile = new CodeFile();
        codeFile.setPackageName(request.getPackageName());

        codeFile.addImport("java.io.IOException");
        codeFile.addImport("com.panosen.orm.DalTableDao");
        codeFile.addImport("org.springframework.stereotype.Component");
        codeFile.addImport(request.getPackageName() + ".entity." + tableEntity);

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
