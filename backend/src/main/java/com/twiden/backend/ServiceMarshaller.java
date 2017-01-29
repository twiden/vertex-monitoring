package com.twiden.backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.ArrayList;

public class ServiceMarshaller {

    public static JSONArray servicesToJSON(ArrayList<Service> services){
        JSONArray service_list = new JSONArray();
        for (Service service : services) {
            service_list.put(serviceToJSON(service));
        }
        return service_list;
    }

    public static ArrayList<Service> servicesFromJSON(JSONArray json) {
        ArrayList<Service> service_instances = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            service_instances.add(serviceFromJSON(json.getJSONObject(i)));
        }

        return service_instances;
    }

    private static Service serviceFromJSON(JSONObject json) {
        return new Service(
            json.getString("id"),
            json.getString("name"),
            json.getString("status"),
            json.getString("url"),
            json.getString("lastCheck")
        );
    }

    private static JSONObject serviceToJSON(Service s){
        JSONObject obj = new JSONObject();
        obj.put("name", s.getName());
        obj.put("id", s.getId());
        obj.put("status", s.getStatus());
        obj.put("lastCheck", s.getLastCheck());
        obj.put("url", s.getUrl());
        return obj;
    }
}
