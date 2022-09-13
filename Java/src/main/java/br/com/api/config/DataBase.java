package br.com.api.config;

import br.com.api.domain.model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class DataBase {
    
    public static void configure() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Properties appProps = new Properties();
        String appConfigPath = rootPath + "application.properties";
        try (FileInputStream inStream = new FileInputStream(appConfigPath)) {
            appProps.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = appProps.get("database.url").toString();
        String user = appProps.get("database.user").toString();
        String password = appProps.get("database.password").toString();
        String schema = appProps.get("database.schema").toString() + ".";
        
        try (Statement stmt = DriverManager.getConnection(url, user, password).createStatement()) {
            
            stmt.execute(createTableSQL(schema));
            
            Log.info("Criação de tabelas feita com sucesso");
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    private static String createTableSQL(String schemaName) {
        Class<User> userClass = User.class;
        
        
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS " + schemaName + userClass.getSimpleName().toLowerCase() + " (");
        
        List<Field> fields = Arrays.stream(userClass.getDeclaredFields()).collect(Collectors.toList());
        
        List<String> columns = new ArrayList<>();
        fields.forEach(field -> {
            String type = null;
            String columnName = field.getName();
            String fieldTypeName = field.getType().getSimpleName();
            
            if (fieldTypeName.equals("int") || fieldTypeName.equals("Integer") || fieldTypeName.equals("Long") || fieldTypeName.equals("long")) {
                type = "int";
            }
            if (fieldTypeName.equals("String")) {
                type = "varchar(255)";
            }
            
            if (columnName.equals("id")) {
                type = "SERIAL PRIMARY KEY";
            }
            
            columns.add(String.format(" %s %s ", columnName, type));
        });
        String column = String.join(",", columns);
        sql.append(column + ");");
        return sql.toString();
    }
    
}
