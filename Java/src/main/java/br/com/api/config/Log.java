package br.com.api.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    
    private static final DateTimeFormatter FORMMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    
    public static void info(Object msg) {
        System.out.println(LocalDateTime.now().format(FORMMATER) + " INFO - " + msg);
    }
    
    public static void error(Object msg) {
        System.out.print(LocalDateTime.now().format(FORMMATER));
        System.err.println(" ERROR - " + msg);
    }
}
