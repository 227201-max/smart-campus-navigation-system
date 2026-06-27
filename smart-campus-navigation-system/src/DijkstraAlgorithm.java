package com.ccs3402.lab.smartcampusnavigationsystem.graphs;

import java.util.*;

public class DijkstraAlgorithm {

    public static Route findShortestPath(Graph graph, String start, String end) {

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Step 1: initialize distances
        for (String node : graph.getAdjList().keySet()) {
            dist.put(node, Integer.MAX_VALUE);
        }

        dist.put(start, 0);
        pq.add(new Node(start, 0));

        // Step 2: Dijkstra loop
        while (!pq.isEmpty()) {

            Node current = pq.poll();

            String currentName = current.name;

            if (currentName.equals(end)) break;

            for (Edge edge : graph.getAdjList().get(currentName)) {

                // skip blocked roads
                if (!edge.active) continue;

                int newDist = dist.get(currentName) + edge.weight;

                if (newDist < dist.get(edge.to)) {

                    dist.put(edge.to, newDist);
                    prev.put(edge.to, currentName);

                    pq.add(new Node(edge.to, newDist));
                }
            }
        }

        // Step 3: build path
        List<String> path = new ArrayList<>();

        String curr = end;

        if (!prev.containsKey(end) && !start.equals(end)) {
            return new Route(new ArrayList<>(), Integer.MAX_VALUE);
        }

        while (curr != null) {
            path.add(curr);
            curr = prev.get(curr);
        }

        Collections.reverse(path);

        return new Route(path, dist.get(end));
    }
}