package com.ccs3402.lab.smartcampusnavigationsystem.graphs;

import java.util.*;

/**
 * ADAPTER: Makes old DijkstraAlgorithm.java work with new Main.java (5-param calls)
 * 
 * What changed:
 * - Added findShortestPath(Graph, String, String, boolean, Set<String>) method
 * - Handles road blocking via blockedEdges Set
 * - boolean isBus parameter is accepted but ignored (walking graph used for both)
 */

public class DijkstraAlgorithm {

    // Original 3-parameter method (kept for backward compatibility)
    public static Route findShortestPath(Graph graph, String start, String end) {
        return findShortestPath(graph, start, end, false, new HashSet<String>());
    }

    // NEW 5-parameter method (required by Main(1).java)
    // Parameters: graph, start, end, isBus, blockedEdges
    public static Route findShortestPath(Graph graph, String start, String end, 
                                          boolean isBus, Set<String> blockedEdges) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Initialize distances
        for (String node : graph.getAdjList().keySet())
            dist.put(node, Integer.MAX_VALUE);
        dist.put(start, 0);
        pq.add(new Node(start, 0));

        // Dijkstra loop
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String currentName = current.name;
            if (currentName.equals(end)) break;

            for (Edge edge : graph.getAdjList().get(currentName)) {
                
                // Check if road is blocked (NEW feature)
                String edgeKey1 = currentName + "-" + edge.to;
                String edgeKey2 = edge.to + "-" + currentName;
                if (blockedEdges.contains(edgeKey1) || blockedEdges.contains(edgeKey2)) {
                    continue;  // Skip blocked roads
                }
                
                if (!edge.active) continue;  // Skip inactive edges
                
                int newDist = dist.get(currentName) + edge.weight;
                if (newDist < dist.get(edge.to)) {
                    dist.put(edge.to, newDist);
                    prev.put(edge.to, currentName);
                    pq.add(new Node(edge.to, newDist));
                }
            }
        }

        // Build path
        List<String> path = new ArrayList<>();
        String curr = end;
        if (!prev.containsKey(end) && !start.equals(end))
            return new Route(new ArrayList<>(), Integer.MAX_VALUE);
        while (curr != null) {
            path.add(curr);
            curr = prev.get(curr);
        }
        Collections.reverse(path);
        return new Route(path, dist.get(end));
    }
}
