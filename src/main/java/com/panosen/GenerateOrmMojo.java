package com.panosen;

import com.panosen.dbschema.SchemaRepository;
import com.panosen.dbschema.information_schema.Column;
import com.panosen.dbschema.information_schema.Statistics;
import com.panosen.dbschema.information_schema.Table;
import com.panosen.generation.FileBase;
import com.panosen.generation.Pack;
import com.panosen.generation.PlainFile;
import com.panosen.orm.PropertiesLoader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Mojo(name = "generate-orm")
public class GenerateOrmMojo extends AbstractMojo {

    private final SchemaRepository schemaRepository = new SchemaRepository();

    private final RepositoryService repositoryService = new RepositoryService();
    private final EntityService entityService = new EntityService();

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private File sourceDirectory;

    @Parameter(required = true, readonly = true)
    private String packageName;

    @Parameter(required = true, readonly = true)
    private List<Database> databases;

    public void execute() throws MojoExecutionException {
        try {
            executeNow();
        } catch (Exception e) {
            throw new MojoExecutionException("", e);
        }
    }

    private void executeNow() throws Exception {

        getLog().info("sourceDirectory = " + sourceDirectory.getAbsolutePath());

        String prefix = Paths.get("", this.packageName.split("\\.")).toString();
        getLog().info(prefix);

        Pack pack = new Pack();
        for (Database database : databases) {
            String dbPackageName = this.packageName;
            String dbPath = prefix;
            if (this.databases.size() > 1) {
                dbPackageName = this.packageName + "." + database.getName();
                dbPath = Paths.get(prefix, database.getName()).toString();
            }

            handleDatabase(pack, dbPackageName, dbPath, database);
        }

        flush(pack);
    }

    public void handleDatabase(Pack pack, String dbPackageName, String dbPath, Database database) throws Exception {

        DataSource dataSource = buildDataSource(database);

        List<Table> tableList = schemaRepository.getTables(dataSource, database.getName());
        List<Column> columnList = schemaRepository.getColumns(dataSource, database.getName());
        List<Statistics> statisticsList = schemaRepository.getStatistics(dataSource, database.getName());

        String entityPackageName = dbPackageName + ".entity";

        for (Table table : tableList) {

            // repository/StudentRepository.java
            {
                String tableNameUpperCamelCase = NameExtension.toUpperCamelCase(table.getTableName());

                String path = Paths.get(dbPath, tableNameUpperCamelCase + "Repository.java").toString();

                RepositoryRequest repositoryRequest = new RepositoryRequest();
                repositoryRequest.setPackageName(dbPackageName);
                repositoryRequest.setTable(table);

                String content = repositoryService.generate(repositoryRequest);

                pack.add(path, content);
            }

            // repositoryentity/StudentEntity.java
            {
                String tableNameUpperCamelCase = NameExtension.toUpperCamelCase(table.getTableName());

                String path = Paths.get(dbPath, "entity", tableNameUpperCamelCase + "Entity.java").toString();

                List<Column> tableColumnList = columnList.stream()
                        .filter(v -> v.getTableName().equals(table.getTableName()))
                        .collect(Collectors.toList());
                List<Statistics> tableStatisticsList = statisticsList.stream()
                        .filter(v -> v.getTableName().equals(table.getTableName()))
                        .collect(Collectors.toList());

                EntityRequest entityRequest = new EntityRequest();
                entityRequest.setPackageName(entityPackageName);
                entityRequest.setDatabaseName(database.getName());
                entityRequest.setTable(table);
                entityRequest.setTableColumnList(tableColumnList);
                entityRequest.setTableStatisticsList(tableStatisticsList);

                String content = entityService.generate(entityRequest);

                pack.add(path, content);
            }
        }
    }

    private DataSource buildDataSource(Database database) throws IOException {
        Properties builtInProperties = PropertiesLoader.loadBuiltInProperties();

        Properties properties = new Properties();
        properties.putAll(builtInProperties);

        properties.put("url", database.getUrl());
        properties.put("username", database.getUsername());
        properties.put("password", database.getPassword());

        PoolConfiguration poolConfiguration = DataSourceFactory.parsePoolProperties(properties);

        return new org.apache.tomcat.jdbc.pool.DataSource(poolConfiguration);
    }

    private void flush(Pack pack) throws IOException {
        for (FileBase fileBase : pack.getFileBaseList()) {

            String filePath = Paths.get(sourceDirectory.getAbsolutePath(), fileBase.getFilePath()).toString();
            getLog().info("file = " + filePath);

            File file = new File(filePath);
            getLog().info(filePath);

            switch (fileBase.getContentType()) {
                case String:
                    writeToFile(file, ((PlainFile) fileBase).getContent());
                    break;
                case Bytes:
                    break;
                default:
                    break;
            }
        }
    }

    private void writeToFile(File file, String content) throws IOException {

        File folderFile = file.getParentFile();
        if (!folderFile.exists()) {
            boolean createFolderSuccess = folderFile.mkdirs();
            if (!createFolderSuccess) {
                return;
            }
        }

        if (!file.exists()) {
            boolean createFileSuccess = file.createNewFile();
            if (!createFileSuccess) {
                return;
            }
        }

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, false))) {
            out.write(content);
        }
    }
}
