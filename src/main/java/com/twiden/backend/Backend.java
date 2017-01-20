package com.twiden.backend;
import io.vertx.core.AbstractVerticle;

public class Backend extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer().requestHandler(req -> req.response().end("Hello Backend")).listen(8080);
    }
}
