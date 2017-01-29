package com.twiden.backend;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.Iterator;
import java.util.ArrayList;

public class ServiceMarshaller {

    public static ArrayList<Service> servicesToJSON(ArrayList<Service> services){
        JSONArray service_list = new JSONArray();
        for (Service service : services) {
            service_list.add(serviceToJSON(service));
        }
        return service_list;
    }

    public static ArrayList<Service> servicesFromJSON(JSONArray json) {
        ArrayList<Service> service_instances = new ArrayList<>();
        Iterator<JSONObject> iterator = json.iterator();

        while (iterator.hasNext()) {
            service_instances.add(serviceFromJSON(iterator.next()));
        }

        return service_instances;
    }

    private static Service serviceFromJSON(JSONObject json) {
        return new Service(
            (String) json.get("id"),
            (String) json.get("name"),
            (String) json.get("status"),
            (String) json.get("url"),
            (String) json.get("lastCheck")
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
