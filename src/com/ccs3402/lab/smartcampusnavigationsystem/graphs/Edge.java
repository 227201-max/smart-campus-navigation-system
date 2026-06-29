package com.ccs3402.lab.smartcampusnavigationsystem.graphs;

public class Edge {
    String to;
    int weight;
    boolean active = true; // road is open by default

    public Edge(String to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}

