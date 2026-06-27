package com.ccs3402.lab.smartcampusnavigationsystem.graphs;

import java.util.List;

public class Route {

    private List<String> path;
    private int totalDistance;

    public Route(List<String> path, int totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public List<String> getPath() {
        return path;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
