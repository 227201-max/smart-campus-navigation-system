package com.ccs3402.lab.smartcampusnavigationsystem.graphs;

import java.util.*;

public class Graph {

    private Map<String, List<Edge>> adj = new HashMap<>();

    public void addEdge(String from, String to, int weight) {

        adj.putIfAbsent(from, new ArrayList<>());
        adj.putIfAbsent(to, new ArrayList<>());

        adj.get(from).add(new Edge(to, weight));
        adj.get(to).add(new Edge(from, weight)); // undirected graph
    }

    public void blockEdge(String from, String to) {

        for (Edge e : adj.get(from)) {
            if (e.to.equals(to)) {
                e.active = false;
            }
        }

        for (Edge e : adj.get(to)) {
            if (e.to.equals(from)) {
                e.active = false;
            }
        }
    }

    public Map<String, List<Edge>> getAdjList() {
        return adj;
    }

    public Set<String> getNodes() {
        return adj.keySet();
    }
}