package com.xw.springbootwebsocket.config;

import com.xw.springbootwebsocket.vertx.VertxServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertxConfig {

    @Bean
    @ConditionalOnMissingBean(VertxServer.class)
    public VertxServer vertxBootstrap() {
        return new VertxServer();
    }

}
