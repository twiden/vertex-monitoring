package com.twiden.backend;

import org.json.simple.JSONObject;

public class Service {

    private final String id;
    private String name;
    private String status;
    private String url;
    private String lastCheck;

    public Service(String id, String name, String status, String url, String lastCheck) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.url = url;
        this.lastCheck = lastCheck;
    }

    public Service(JSONObject json) {
        this(
            (String) json.get("id"),
            (String) json.get("name"),
            (String) json.get("status"),
            (String) json.get("url"),
            (String) json.get("lastCheck")
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public String getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(String lastCheck) {
        this.lastCheck = lastCheck;
    }

    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("name", this.getName());
        obj.put("id", this.getId());
        obj.put("status", this.getStatus());
        obj.put("lastCheck", this.getLastCheck());
        obj.put("url", this.getUrl());
        return obj;
    }
}
