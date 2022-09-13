package br.com.api.web.handler;

import br.com.api.config.Log;
import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.domain.model.Response;
import br.com.api.domain.model.User;
import br.com.api.web.controller.UserController;
import br.com.api.web.exception.InvalidRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserHandler {
    
    private final UserController userController;
    
    public UserHandler(IUserRepository userRepository) {
        userController = new UserController(userRepository);
    }
    
    public HttpHandler handle() {
        return (exchange -> {
            OutputStream out = exchange.getResponseBody();
            
            Map<Object, Object> params = fillParams(exchange.getRequestURI().getRawQuery());
            User user = readBodyRequest(exchange.getRequestBody());
            
            Response response;
            Log.info("Mapeando requisição");
            try {
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        Log.info("Direcionando para controller get");
                        response = userController.getUser(Long.valueOf(params.get("userId").toString()));
                        break;
                    case "POST":
                        Log.info("Direcionando para controller post");
                        response = userController.postUser(user);
                        break;
                    case "DELETE":
                        Log.info("Direcionando para controller delete");
                        response = userController.deleteUser(Long.valueOf(params.get("userId").toString()));
                        break;
                    case "PUT":
                        Log.info("Direcionando para controller update");
                        response = userController.putUser(Long.valueOf(params.get("userId").toString()), user);
                        break;
                    default:
                        Log.info("Metodo não encontrado");
                        response = new Response(405, "");
                        break;
                }
            } catch (InvalidRequestException e) {
                response = new Response(404, e.getErrors());
            }
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(response.getStatus(), response.getJson().length);
            out.write(response.getJson());
            out.flush();
        });
    }
    
    private static User readBodyRequest(InputStream requestBodyStream) throws IOException {
        if (requestBodyStream.available() == 0) {
            return null;
        }
        StringBuilder textBuilder = new StringBuilder();
        Log.info("Tranformando requesto body em objeto");
        try (Reader reader = new BufferedReader(new InputStreamReader(requestBodyStream, StandardCharsets.UTF_8))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            Log.error("Erro ao ler body");
            e.printStackTrace();
        }
        
        return new ObjectMapper().readValue(textBuilder.toString(), User.class);
    }
    
    public static Map<Object, Object> fillParams(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }
        
        List<String> split = List.of(query.split("&"));
        return split.parallelStream().collect(Collectors.toMap(s -> s.split("=")[0], s -> s.split("=")[1]));
    }
    
}
