package com.twiden.backend;

public class Service {

    private final String id;
    private final String name;
    private final String status;
    private final String url;
    private final String lastCheck;

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

    public String getUrl() {
        return url;
    }

    public String getLastChecked() {
        return lastCheck;
    }
}
