package com.twiden.backend;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
            append(id).
            append(name).
            append(status).
            append(url).
            append(lastCheck).
            toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Service)) {
            return false;
        }
        if (o == this) {
            return true;
        }

        Service that = (Service) o;

        return new EqualsBuilder().
            append(this.id, that.getId()).
            append(this.name, that.getName()).
            append(this.status, that.getStatus()).
            append(this.url, that.getUrl()).
            append(this.lastCheck, that.getLastCheck()).
            isEquals();
    }
}
