package com.twiden.backend;

import com.twiden.backend.Storage;
import com.twiden.backend.Service;

import java.util.ArrayList;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class Backend extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/service").handler(this::handleGetServices);
        router.post("/service/").handler(this::handleCreateService);
        router.delete("/service/:serviceId").handler(this::handleDeleteService);

        int listen_port = 8000;
        if (System.getenv("LISTEN_PORT") != null) {
            listen_port = Integer.parseInt(System.getenv("LISTEN_PORT"));
        }

        System.out.println("Listening at http://localhost:" + listen_port);
        vertx.createHttpServer().requestHandler(router::accept).listen(listen_port);
    }

    private void handleGetServices(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        try {
            ArrayList<Service> services = new Storage().listServices();
            response
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(services));
        } catch (Throwable e) {
            e.printStackTrace();
            response.setStatusCode(500).end("500 " + e.toString());
        }
    }

    private void handleCreateService(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        try {
            JsonObject service = routingContext.getBodyAsJson();
            String name = service.getString("name");
            String url = service.getString("url");
            String id = new Storage().createSevice(name, url);
            response.setStatusCode(201).end("CREATED " + id);
        } catch (Throwable e) {
            e.printStackTrace();
            response.setStatusCode(500).end("500 " + e.toString());
        }
    }

    private void handleDeleteService(RoutingContext routingContext) {
        String serviceId = routingContext.request().getParam("serviceId");
        HttpServerResponse response = routingContext.response();
        response.end("Delete services handler");
    }
}
