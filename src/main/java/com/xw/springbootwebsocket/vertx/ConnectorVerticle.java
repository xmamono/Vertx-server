package com.xw.springbootwebsocket.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectorVerticle extends AbstractVerticle {

    private final int port;

    public ConnectorVerticle(int port) {
        this.port = port;
    }

    @Override
    public void start() throws Exception {

        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        // cross-domain
        router.route().handler(BodyHandler.create());
        CorsHandler corsHandler = CorsHandler.create("*");
        corsHandler.allowedMethod(HttpMethod.GET);
        corsHandler.allowedMethod(HttpMethod.POST);
        corsHandler.allowedMethod(HttpMethod.PUT);
        corsHandler.allowedMethod(HttpMethod.DELETE);
        corsHandler.allowedHeader("Authorization");
        corsHandler.allowedHeader("Content-Type");
        corsHandler.allowedHeader("Access-Control-Allow-Origin");
        corsHandler.allowedHeader("Access-Control-Allow-Headers");
        router.route().handler(corsHandler);

        // 连接
        router.route("/connect").handler(new ConnectAcceptor(vertx));
        httpServer.requestHandler(router::accept).listen(port);
        log.info("Vertx running, listen in port : {}", port);
    }

    private static class ConnectAcceptor implements Handler<RoutingContext> {


        private final Vertx vertx;

        private ConnectAcceptor(Vertx vertx) {
            this.vertx = vertx;
        }

        @Override
        public void handle(RoutingContext routingContext) {
            ServerWebSocket upgrade = routingContext.request().upgrade();
            upgrade.closeHandler((close) -> {
                log.info("connect closed");
            });
            upgrade.textMessageHandler((msg) -> {
                log.info("receive msg : {}", msg);
            });
            vertx.eventBus().consumer("receiver", (Handler<Message<String>>) message -> upgrade.writeTextMessage(message.body()));
        }
    }
}
