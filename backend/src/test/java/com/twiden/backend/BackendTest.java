package com.twiden.backend;

import com.twiden.backend.Storage;
import com.twiden.backend.Service;
import java.io.IOException;
import java.util.ArrayList;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@RunWith(VertxUnitRunner.class)
public class BackendTest {

    private int port = 8000;
    private Vertx vertx;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        try {
            Storage storage = new Storage();
            storage.clearDatabase();

        } catch (IOException e) {
            fail("Could not access database");
        }
        vertx.deployVerticle(Backend.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testListingServicesWhenThereAreNone(TestContext context) {
        final Async async = context.async();
        vertx.createHttpClient().getNow(port, "localhost", "/service",
            response -> {
                response.handler(body -> {
                    context.assertEquals(parseServices(body.toString()).size(), 0);
                    async.complete();
            });
        });
    }

    @Test
    public void testListingOneServiceWithStatus(TestContext context) {
        createService("Pizza Service", "http://example.com/pizza");
        String service_id = listServices().get(0).getId();
        setStatus(service_id, "OK", "1914-06-28 13:37");
        final Async async = context.async();
        vertx.createHttpClient().getNow(port, "localhost", "/service",
            response -> {
                response.handler(body -> {
                    JSONArray services = parseServices(body.toString());
                    context.assertEquals(services.size(), 1);
                    JSONObject s = (JSONObject) services.get(0);

                    context.assertEquals(s.get("id"), service_id);
                    context.assertEquals(s.get("name"), "Pizza Service");
                    context.assertEquals(s.get("url"), "http://example.com/pizza");
                    context.assertEquals(s.get("status"), "OK");
                    context.assertEquals(s.get("lastCheck"), "1914-06-28 13:37");
                    async.complete();
            });
        });
    }

    @Test
    public void testAddingOneService(TestContext context) {
        final Async async = context.async();
        final String json = "{\"name\": \"Pizza Service\", \"url\": \"http://example.com/pizza\"}";

        vertx.createHttpClient().post(port, "localhost", "/service")
            .putHeader("content-type", "application/json")
            .putHeader("content-length", Integer.toString(json.length()))
            .handler(response -> {
                context.assertEquals(response.statusCode(), 201);
                ArrayList<Service> services = listServices();
                context.assertEquals(services.size(), 1);
                Service service = services.get(0);
                context.assertEquals((String) service.getName(), "Pizza Service");
                context.assertEquals((String) service.getUrl(), "http://example.com/pizza");
                context.assertEquals((String) service.getStatus(), "");
                context.assertEquals((String) service.getLastCheck(), "");
                async.complete();
            })
            .write(json)
            .end();
    }

    @Test
    public void testDeletingOneService(TestContext context) {
        final String json = "{\"name\": \"Pizza Service\", \"url\": \"http://example.com/pizza\"}";
        final String length = Integer.toString(json.length());
        createService("Pizza Service", "http://example.com/pizza");
        String service_id = listServices().get(0).getId();

        final Async async = context.async();
        vertx.createHttpClient().delete(port, "localhost", "/service/" + service_id)
            .handler(response -> {
                context.assertEquals(listServices().size(), 0);
                async.complete();

            }).end();
    }

    @Test
    public void testSettingServiceStatus(TestContext context) {
        final String json = "{\"status\": \"OK\", \"timestamp\": \"1914-06-28 13:37\"}";
        createService("Pizza Service", "http://example.com/pizza");
        String service_id = listServices().get(0).getId();
        final Async async = context.async();
        vertx.createHttpClient().request(HttpMethod.PATCH, port, "localhost", "/service/" + service_id)
            .putHeader("content-type", "application/json")
            .putHeader("content-length", Integer.toString(json.length()))
            .handler(response -> {
                context.assertEquals(response.statusCode(), 200);
                Service service = listServices().get(0);
                context.assertEquals((String) service.getStatus(), "OK");
                context.assertEquals((String) service.getLastCheck(), "1914-06-28 13:37");
                async.complete();
            })
            .write(json)
            .end();
    }

    private void createService(String name, String url) {
        try {
            new Storage().createService(name, url);
        } catch (Throwable e) {
            fail("Could not access database");
        }
    }

    private void setStatus(String id, String status, String timestamp) {
        try {
            new Storage().setStatus(id, status, timestamp);
        } catch (Throwable e) {
            fail("Could not access database");
        }
    }

    private ArrayList<Service> listServices() {
        try {
            return new Storage().listServices();
        } catch (Throwable e) {
            fail("Could not access database");
        }

        return new ArrayList<Service>();
    }

    private JSONArray parseServices(String body) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(body);
            JSONObject jsonObject =  (JSONObject) obj;
            return (JSONArray) jsonObject.get("services");
        } catch (Throwable e) {
            fail("Could not parse json");
        }

        return new JSONArray();
    }
}
