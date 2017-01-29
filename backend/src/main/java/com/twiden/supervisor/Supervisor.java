package com.twiden.supervisor;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ByteArrayEntity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Supervisor {

    public static void main(String[] args) {
        System.out.println("Supervisor!");

        int sleep_seconds = 60;
        if (System.getenv("SLEEP_SECONDS") != null) {
            sleep_seconds = Integer.parseInt(System.getenv("SLEEP_SECONDS"));
        }

        String backend_url = "http://localhost:8000/service/";
        if (System.getenv("BACKEND_URL") != null) {
            backend_url = System.getenv("BACKEND_URL");
        }

        System.out.println("Backend URL: " + backend_url);
        System.out.println("Sleep sleep seconds: " + sleep_seconds);

        boolean first_iteration = true;
        while (true) {
            if (!first_iteration) {
                try {
                    Thread.sleep(sleep_seconds * 1000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            first_iteration = false;
            healthCheck(backend_url);
        }
    }

    private static void healthCheck(String backend_url) {
        HashMap<String, String> services = loadServices(backend_url);
        Iterator<Map.Entry<String, String>> it = services.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String id = entry.getKey();
            String url = entry.getValue();
            boolean ok = checkServiceStatus(id, url);
            updateServiceStatus(id, backend_url, ok);
        }
    }

    private static HashMap<String, String> loadServices(String backend_url) {
        String backend_response;
        DefaultHttpClient httpclient = new DefaultHttpClient();

        try {
            HttpResponse r = httpclient.execute(new HttpGet(backend_url));
            HttpEntity entity = r.getEntity();
            backend_response = EntityUtils.toString(entity);
        } catch (IOException e) {
            System.out.println("Seems like the backend is not responding...");
            return new HashMap<String, String>();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        return parseServices(backend_response);
    }

    private static HashMap<String, String> parseServices(String backend_response) {
        HashMap<String, String> services = new HashMap<>();

        JSONObject obj =  (JSONObject) new JSONTokener(backend_response).nextValue();
        JSONArray remote_services = obj.getJSONArray("services");

        for (int i = 0; i < remote_services.length(); i++) {
            JSONObject service = remote_services.getJSONObject(i);
            String id = service.getString("id");
            String url = service.getString("url");
            services.put(id, url);
        }
        return services;
    }

    private static boolean checkServiceStatus(String id, String url) {
        boolean success = true;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpResponse ping = httpclient.execute(new HttpGet(url));
            if (ping.getStatusLine().getStatusCode() != 200) {
                success = false;
            }
        } catch (IOException e) {
            success = false;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return success;
    }

    private static void updateServiceStatus(String id, String backend_url, boolean success) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String now = df.format(new Date());
        try {
            HttpPatch patch = new HttpPatch(backend_url + id);
            String json = "{\"status\": \"" + (success ? "OK" : "FAIL") + "\", \"timestamp\": \"" + now + "\"}";
            HttpEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));
            patch.setEntity(entity);
            HttpResponse response = httpclient.execute(patch);
        } catch (IOException e) {
            System.out.println("Seems like the backend is not responding...");
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }
}
