package org.pem.idmlight.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "version")
public class Version {
    private String id;
    private String updated;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String name) {
        this.updated = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}

