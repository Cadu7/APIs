package br.com.api;

import br.com.api.config.DataBase;
import br.com.api.config.Log;
import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.infra.UserRepositoryJDBC;
import br.com.api.web.handler.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    
    private static final int SERVER_PORT = 8080;
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        Log.info("Iniciando aplicação API_JAVA_FRAMEWORKLESS");
        Log.info("Iniciando rotas");
        DataBase.configure();
        createContexts(server);
        server.setExecutor(null);
        Log.info("Iniciando Server na porta " + SERVER_PORT);
        server.start();
    }
    
    private static void createContexts(HttpServer server){
        IUserRepository userRepository = new UserRepositoryJDBC();
        
        server.createContext("/api/user", new UserHandler(userRepository).handle());
    }
    
}
