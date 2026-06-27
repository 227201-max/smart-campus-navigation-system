package com.ccs3402.lab.smartcampusnavigationsystem.graphs;

public class Node implements Comparable<Node> {

    String name;
    int dist;

    public Node(String name, int dist) {
        this.name = name;
        this.dist = dist;
    }

    @Override
    public int compareTo(Node other) {
        return this.dist - other.dist;
    }
}

