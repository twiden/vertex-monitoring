package com.twiden.service;
import io.vertx.core.AbstractVerticle;

public class Service extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer().requestHandler(req -> req.response().end("Hello Service")).listen(8080);
    }
}
