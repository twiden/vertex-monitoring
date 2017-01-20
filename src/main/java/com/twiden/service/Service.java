package com.twiden.service;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.AbstractVerticle;

public class Service extends AbstractVerticle {

    static boolean up;

    @Override
    public void start() {
        Service.up = true;
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/").handler(this::handleGet);
        router.put("/up/").handler(this::handleUp);
        router.put("/down/").handler(this::handleDown);

        int listen_port = 8080;
        if (System.getenv("LISTEN_PORT") != null) {
            listen_port = Integer.parseInt(System.getenv("LISTEN_PORT"));
        }

        System.out.println("Listening at http://localhost:" + listen_port);
        vertx.createHttpServer().requestHandler(router::accept).listen(listen_port);
    }

    private void handleGet(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        if (Service.up) {
            response.end("OK");
            System.out.println("Request recieved and responded OK");
        } else {
            response.close();
            System.out.println("Request recieved but pretended to be dead");
        }
    }

    private void handleUp(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        Service.up = true;
        response.end("OK, Up!");
    }

    private void handleDown(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        Service.up = false;
        response.end("OK, Down!");
    }
}
