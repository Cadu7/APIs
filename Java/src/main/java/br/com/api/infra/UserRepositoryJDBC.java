package br.com.api.infra;

import br.com.api.config.Log;
import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.domain.model.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;



public class UserRepositoryJDBC implements IUserRepository {
    
    public UserRepositoryJDBC() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Properties appProps = new Properties();
        String appConfigPath = rootPath + "application.properties";
        try(FileInputStream inStream = new FileInputStream(appConfigPath)){
            appProps.load(inStream) ;
        }catch (IOException e){
            e.printStackTrace();
        }
        URL = appProps.get("database.url").toString();
        USER = appProps.get("database.user").toString();
        PASSWORD = appProps.get("database.password").toString();
        TABLE_NAME = appProps.get("database.schema").toString() + ".user";
    }
    
    private final String URL;
    private final String USER;
    private final String PASSWORD;
    private final String TABLE_NAME;
    
    @Override
    public User findById(Long id) {
        User user = null;
        try (Statement stmt = DriverManager.getConnection(URL, USER, PASSWORD).createStatement()) {
            String sql = String.format("SELECT * FROM %s WHERE id = \'%s\'", TABLE_NAME, id);
            Log.info("Executando statment");
            ResultSet resultSet = stmt.executeQuery(sql);
            if (resultSet.next()){
                user = new User();
                user.setId(id);
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException sqle) {
            Log.error("Erro na busca de usuario no banco");
            sqle.printStackTrace();
        }
        return user;
    }
    
    @Override
    public void persist(User user) {
        try (Statement stmt = DriverManager.getConnection(URL, USER, PASSWORD).createStatement()) {
            String sql = String.format("INSERT INTO %s (name, password) VALUES (\'%s\',\'%s\')", TABLE_NAME, user.getName(), user.getPassword());
            stmt.execute(sql);
        } catch (SQLException sqle) {
            Log.error("Erro na insercao de usuario no banco");
            sqle.printStackTrace();
        }
    }
    
    @Override
    public void deleteById(Long id) {
        try (Statement stmt = DriverManager.getConnection(URL, USER, PASSWORD).createStatement()) {
            String sql = String.format("DELETE FROM %s WHERE id = \'%s\'", TABLE_NAME, id);
            stmt.execute(sql);
        } catch (SQLException sqle) {
            Log.error("Erro na deleção de usuario no banco");
            sqle.printStackTrace();
        }
    }
    
    @Override
    public void update(User user) {
        try (Statement stmt = DriverManager.getConnection(URL, USER, PASSWORD).createStatement()) {
            String sql = String.format("UPDATE %s SET name = \'%s\', password = \'%s\' WHERE id = \'%s\'", TABLE_NAME, user.getName(),user.getPassword(),user.getId());
            stmt.execute(sql);
        } catch (SQLException sqle) {
            Log.error("Erro na atualização de usuario no banco");
            sqle.printStackTrace();
        }
    }
}
