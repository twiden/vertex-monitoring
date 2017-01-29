package com.twiden.backend;

import com.twiden.backend.Storage;
import com.twiden.backend.Service;
import com.twiden.backend.ServiceNotFound;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class Backend extends AbstractVerticle {

    private Storage storage;

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/service").handler(this::handleGetServices);
        router.post("/service/").handler(this::handleCreateService);
        router.delete("/service/:serviceId").handler(this::handleDeleteService);
        router.patch("/service/:serviceId").handler(this::handleSetStatus);

        int listen_port = 8000;
        if (System.getenv("LISTEN_PORT") != null) {
            listen_port = Integer.parseInt(System.getenv("LISTEN_PORT"));
        }

        try {
            storage = new Storage();
        } catch (IOException e) {
            System.out.println("Could not access database");
            return;
        }

        System.out.println("Listening at http://localhost:" + listen_port);
        vertx.createHttpServer().requestHandler(router::accept).listen(listen_port);
    }

    private void handleGetServices(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        try {
            ArrayList<Service> services = storage.listServices();
            HashMap<String, ArrayList<Service>> obj = new HashMap<>();
            obj.put("services", services);
            response
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(obj));
        } catch (Throwable e) {
            e.printStackTrace();
            response.setStatusCode(500).end("500 " + e.toString()); // Do not return exception in production code
        }
    }

    private void handleCreateService(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        try {
            JsonObject service = routingContext.getBodyAsJson();
            String name = service.getString("name");
            String url = service.getString("url");
            String id = storage.createService(name, url);
            response.setStatusCode(201).end("CREATED " + id);
        } catch (Throwable e) {
            e.printStackTrace();
            response.setStatusCode(500).end("500 " + e.toString()); // Do not return exception in production code
        }
    }

    private void handleSetStatus(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        String id = routingContext.request().getParam("serviceId");
        try {
            JsonObject patch = routingContext.getBodyAsJson();
            String status = patch.getString("status");
            String timestamp = patch.getString("timestamp");
            storage.setStatus(id, status, timestamp);
            response.setStatusCode(200).end("UPDATED " + id);
        } catch (ServiceNotFound e) {
            response.setStatusCode(404).end("NOT FOUND " + id);
        } catch (Throwable e) {
            e.printStackTrace();
            response.setStatusCode(500).end("500 " + e.toString()); // Do not return exception in production code
        }
    }

    private void handleDeleteService(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        String id = routingContext.request().getParam("serviceId");
        try {
            storage.deleteService(id);
            response.setStatusCode(200).end("DELETED " + id);
        } catch (ServiceNotFound e) {
            response.setStatusCode(404).end("NOT FOUND " + id);
        } catch (Throwable e) {
            e.printStackTrace();
            response.setStatusCode(500).end("500 " + e.toString()); // Do not return exception in production code
        }
    }
}
