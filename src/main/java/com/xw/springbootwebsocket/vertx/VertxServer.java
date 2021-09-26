package com.xw.springbootwebsocket.vertx;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class VertxServer implements InitializingBean, DisposableBean {

    private int port = 5555;

    private Vertx vertx;

    @Override
    public void destroy() throws Exception {
        vertx.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new ConnectorVerticle(port));
    }

    protected Vertx getVertx() {
        return this.vertx;
    }

    public void send(String receiver, String content) {
        vertx.eventBus().send(receiver, content);
    }
}
