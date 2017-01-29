package com.twiden.backend;

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

}
