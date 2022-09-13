package br.com.api.config;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

public class LogConfig {
    
    public void printStartUp(@Observes StartupEvent ev){
        String port = ConfigProvider.getConfig().getValue("quarkus.http.port",String.class);
        String appName = ConfigProvider.getConfig().getValue("quarkus.application.name",String.class);
        String db = "https://" + ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url",String.class).split("//")[1];
        Log.info(String.format("Starting the application %s on port %s",appName,port));
        Log.info(String.format("Connecting to database %s", db));
    }
    
    
}
