package com.github.im2back.orchestrator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.EurekaInstanceConfig;

@Component
public class EurekaInstanceIdConfig {

    @SuppressWarnings("unused")
	private final EurekaInstanceConfig eurekaInstanceConfig;

    @Value("${spring.application.name}")
    private String appName;
    
    private int port;

    public EurekaInstanceIdConfig(EurekaInstanceConfig eurekaInstanceConfig) {
        this.eurekaInstanceConfig = eurekaInstanceConfig;
    }

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerInitialized(WebServerInitializedEvent event) {
        // Captura a porta real do servidor
        this.port = event.getWebServer().getPort();
      
        // Configura o Instance ID com a porta real
        String newInstanceId = appName + ":" + port;
        System.setProperty("eureka.instance.instanceId", newInstanceId);
        
        System.setProperty("instance.id",newInstanceId );
        
        System.out.println("Eureka Instance ID configurado como: " + newInstanceId);
    }
}

