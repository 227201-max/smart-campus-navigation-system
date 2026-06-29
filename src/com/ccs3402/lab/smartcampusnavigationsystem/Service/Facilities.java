package com.ccs3402.lab.smartcampusnavigationsystem.Service;

import java.util.ArrayList;
import java.util.List;

public class Facilities {

    private String location;
    private List<String> services = new ArrayList<>();

    public Facilities(String location) {
        this.location = location;
    }

    public void addService(String service) {
        services.add(service);
    }

    public String getLocation() {
        return location;
    }

    public List<String> getServices() {
        return services;
    }
}
