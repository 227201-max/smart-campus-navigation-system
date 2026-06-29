# Smart Campus Navigation System

**CCS4202 - Design and Analysis of Algorithm**  
Second Semester, 2025/2026  
**Group: FIRST CALL**

---

## Table of Contents
- [1. Problem Illustration](#1-problem-illustration)
- [2. Algorithm Paradigm & Pseudocode](#2-algorithm-paradigm--pseudocode)
- [3. Program Demonstration](#3-program-demonstration)
- [4. Algorithm Analysis](#4-algorithm-analysis)
- [Team Members](#team-members)
- [References](#references)

---

## 1. Problem Illustration

### Problem Statement

Navigating a large university campus can be quite challenging, especially for new students, visitors, and staff who are unfamiliar with the campus layout. Similar issues were reported at the Universidad de Manila (UDM), where outdated printed maps, confusing building floor plans, and insufficient campus signage resulted in navigation delays and misunderstandings among campus users (Fernandez et al., 2025). Finding lecture halls, administrative offices, libraries, and other facilities often requires significant time and effort. Furthermore, temporary pathway closures due to maintenance work or campus events may make navigation even more difficult. Therefore, there is a need for a **Smart Campus Navigation System** that can determine the shortest and most efficient route between two campus locations.

**Our Solution:** A Smart Campus Navigation System that uses **Dijkstra's Algorithm** to find the shortest and most efficient route between two campus locations.

### Why It Is Important

- Helps students, visitors, and staff navigate the campus efficiently
- Reduces time spent searching for buildings and facilities
- Provides the shortest and fastest route between locations
- Supports better mobility within large campus environments
- Demonstrates a practical application of graph algorithms and algorithm analysis

According to Kurniawan et al. (2023), graph-based navigation systems are effective in optimizing route planning in complex environments by modeling locations as vertices and pathways as weighted edges. Their findings show that shortest-path algorithms such as Dijkstra's Algorithm significantly improve navigation efficiency and decision-making in spatial systems.

### UPM Campus Setting

*Universiti Putra Malaysia (UPM) - A large campus environment where students, visitors, and staff need to find buildings, lecture halls, and other facilities.*

### Campus Locations (Graph Nodes)

| Location | Description |
|----------|-------------|
| FSKTM | Faculty of Computer Science & IT |
| FBMK | Faculty of Modern Languages & Communication |
| DKAP | Dewan Kuliah Alam Pusat (Central Lecture Hall) |
| KAA | Kompleks Akademik A (Academic Complex) |
| UPM Main Gate | Main entrance of the campus |
| Guard House KC | Security checkpoint |
| KMR | Kolej Mohamad Rashid |
| Bistro | Campus bistro/cafeteria area |
| Canselori | Chancellor Complex |
| Faculty of Science | Science Faculty |
| SPE | School of Business and Economics |
| Library | University library |
| Dewan Besar | Main hall/event venue |
| PKSASS | Sports complex area |
| Dewan Kuliah | Lecture hall building |
| Foodtech Faculty | Food Technology Faculty |
| Bilik Kuliah | Additional classroom block |

### Graph Representation

The campus is modeled as a **weighted undirected graph**:
- **Vertices (Nodes)**: Campus locations/buildings
- **Edges (Paths)**: Pathways connecting locations
- **Weights**: Distance in meters

### Graph Edges (Campus Pathways)

| From | To | Distance (meters) |
|------|-----|-------------------|
| FBMK | KAA | 180 |
| FSKTM | DKAP | 80 |
| FSKTM | Guard House KC | 250 |
| FSKTM | UPM main gate | 80 |
| UPM main gate | KAA | 400 |
| UPM main gate | DKAP | 180 |
| FSKTM | Guard House KC | 200 |
| Guard House KC | FBMK | 500 |
| Guard House KC | KMR | 700 |
| KMR | Bistro | 160 |
| Bistro | Canselori | 300 |
| Canselori | Faculty of Science | 700 |
| Faculty of Science | SPE | 80 |
| SPE | Library | 350 |
| DKAP | Dewan Besar | 400 |
| Dewan Besar | PKSASS | 50 |
| Library | Bilik Kuliah | 280 |
| Dewan Besar | Dewan Kuliah | 850 |
| Dewan Kuliah | Foodtech Faculty | 100 |
| Bilik Kuliah | Foodtech Faculty | 600 |
| Guard House KC | Dewan Besar | 280 |
| PKSASS | SPE | 280 |

### Bus Stops

| Route | Distance (meters) |
|-------|-------------------|
| FSKTM - SPE | 500 |
| SPE - Library | 350 |
| FBMK - SPE | 450 |
| Library - Foodtech Faculty | 200 |

### Campus Facilities

| Location | Available Facilities |
|----------|---------------------|
| FSKTM | Printing, Cafe, Meeting Room, Toilets, Vending Machines, Bus Stop |
| FBMK | Printing Service, Bus Stop |
| KAA | Cafe, Printing Service, Convenience Store, Toilets, Bus Stop |
| DKAP | Convenience Store, Toilets, Vending Machines |
| Bistro | Convenience Store, Cafe, Printing Service, Bus Stop |
| Library | Printing Service, Convenience Store, Cafe, Toilets |

---

## 2. Algorithm Paradigm & Pseudocode

### Algorithm Paradigm Comparison

| Algorithm Paradigm | Strengths | Weaknesses | Suitability |
|-------------------|-----------|------------|-------------|
| Sorting | Efficient for organizing and searching data | Cannot determine optimal routes between locations | Low |
| Divide & Conquer | Breaks problem into smaller subproblems | Not designed for shortest path problems; cannot handle graph traversal efficiently | Low |
| Dynamic Programming | Stores and reuses previously computed results; finds shortest paths in general graphs | Requires higher memory; inefficient for large-scale graph navigation | Medium |
| Greedy | Fast and simple to implement | May not always produce a globally optimal solution; sensitive to path dead ends | Medium |
| **Graph Algorithms** | **Specifically designed for network and path finding problems; directly models locations and pathways** | **Performance can decrease as graph becomes very large and complex** | **High** |

### Why Dijkstra's Algorithm?

> "Graph algorithms are the most suitable solution because a university campus can be represented as a weighted graph, where buildings are vertices and pathways are edges. Dijkstra's algorithm is selected as the proposed solution because it uses a greedy approach to find the shortest path in a graph with non-negative edge weights." -- Gbadamosi et al. (2025)

**Dijkstra's Algorithm was selected because:**

1. It uses a **greedy approach** to find the shortest path
2. It works efficiently with **non-negative edge weights** (distances are always positive)
3. It guarantees the **optimal (shortest) solution**
4. It is well-suited for **sparse graphs** like campus maps
5. Gbadamosi et al. (2025) successfully applied Dijkstra's Algorithm at OAUSTECH, Nigeria, showing improved navigation efficiency and user satisfaction

### Pseudocode

```
Algorithm Dijkstra(Graph, Start, Destination, isBus, blockedEdges):

    // Step 1: Initialization
    for each vertex v in Graph:
        dist[v] <- INFINITY
        prev[v] <- NULL
    dist[Start] <- 0

    // Priority Queue ordered by distance
    PQ <- all vertices in Graph

    // Step 2: Main Loop (Greedy Selection)
    while PQ is not empty:
        u <- vertex in PQ with minimum dist[u]
        remove u from PQ

        if u == Destination:
            break

        // Check neighbors - Relaxation Step
        for each neighbor v of u:
            if edge(u,v) is in blockedEdges:
                continue

            alt <- dist[u] + weight(u, v)
            if alt < dist[v]:
                dist[v] <- alt
                prev[v] <- u
                PQ.update(v, dist[v])

    // Step 3: Path Reconstruction
    path <- empty list
    curr <- Destination
    while curr is not NULL:
        add curr to path
        curr <- prev[curr]
    reverse(path)

    return path, dist[Destination]
```

### How Dijkstra's Algorithm Works in Our System

**The algorithm consists of three main phases:**

**Phase 1 - Initialization:**
- Set the start node's distance to 0
- Set all other nodes' distance to infinity
- Add all nodes to the priority queue

**Phase 2 - Greedy Processing (Recurrence):**
- While the queue is not empty:
  - Extract the node with the smallest distance
  - Skip blocked edges (road maintenance)
  - For each neighbor, check if the path through the current node is shorter
  - If a shorter path is found, update the distance and predecessor

**Phase 3 - Path Construction:**
- Backtrack from the destination using the predecessor array
- Reverse the path to get the correct order

### The Greedy Optimization Function

The optimization function at each step is:
```
Relax(u, v):
    if dist[v] > dist[u] + weight(u, v):
        dist[v] = dist[u] + weight(u, v)
        prev[v] = u
```

This is the **greedy choice** - at each step, we locally optimize by choosing the minimum distance, which globally leads to the shortest path.

---

## 3. Program Demonstration

### System Architecture

The Smart Campus Navigation System operates through three integrated layers:

| Layer | Description |
|-------|-------------|
| **Input Layer** | User selects start and destination from dropdown menus; user can also mark roads as closed for maintenance, search for facilities, or choose bus routes |
| **Processing Layer** | System initializes distances, places nodes into a priority queue, and executes Dijkstra's algorithm to find the shortest path |
| **Output Layer** | System outputs the shortest route, total distance, estimated travel time, and available facilities |

### Java Source Code

#### Node.java - Priority Queue Node
```java
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
```

#### Edge.java - Graph Edge
```java
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
```

#### Graph.java - Graph Data Structure
```java
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
        for (Edge e : adj.get(from))
            if (e.to.equals(to)) e.active = false;
        for (Edge e : adj.get(to))
            if (e.to.equals(from)) e.active = false;
    }

    public Map<String, List<Edge>> getAdjList() { return adj; }
    public Set<String> getNodes() { return adj.keySet(); }
}
```

#### Route.java - Result Container
```java
package com.ccs3402.lab.smartcampusnavigationsystem.graphs;

import java.util.List;

public class Route {
    private List<String> path;
    private int totalDistance;

    public Route(List<String> path, int totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public List<String> getPath() { return path; }
    public int getTotalDistance() { return totalDistance; }
}
```

#### DijkstraAlgorithm.java - Core Algorithm
```java
package com.ccs3402.lab.smartcampusnavigationsystem.graphs;

import java.util.*;

public class DijkstraAlgorithm {

    // Original 3-parameter method
    public static Route findShortestPath(Graph graph, String start, String end) {
        return findShortestPath(graph, start, end, false, new HashSet<String>());
    }

    // Enhanced 5-parameter method with road blocking support
    public static Route findShortestPath(Graph graph, String start, String end, 
                                          boolean isBus, Set<String> blockedEdges) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Step 1: Initialize distances
        for (String node : graph.getAdjList().keySet())
            dist.put(node, Integer.MAX_VALUE);
        dist.put(start, 0);
        pq.add(new Node(start, 0));

        // Step 2: Dijkstra loop
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String currentName = current.name;
            if (currentName.equals(end)) break;

            for (Edge edge : graph.getAdjList().get(currentName)) {
                
                // Check if road is blocked
                String edgeKey1 = currentName + "-" + edge.to;
                String edgeKey2 = edge.to + "-" + currentName;
                if (blockedEdges.contains(edgeKey1) || blockedEdges.contains(edgeKey2)) {
                    continue;
                }
                
                if (!edge.active) continue;
                
                int newDist = dist.get(currentName) + edge.weight;
                if (newDist < dist.get(edge.to)) {
                    dist.put(edge.to, newDist);
                    prev.put(edge.to, currentName);
                    pq.add(new Node(edge.to, newDist));
                }
            }
        }

        // Step 3: Build path
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
```

#### Facilities.java - Facility Search
```java
package com.ccs3402.lab.smartcampusnavigationsystem.Service;

import java.util.*;

public class Facilities {
    private String location;
    private List<String> services;

    public Facilities(String location, List<String> services) {
        this.location = location;
        this.services = services;
    }

    public String getLocation() { return location; }
    public List<String> getServices() { return services; }
}
```

> **Full source code available in the `src/` directory.**

### Program Output Examples

**Example 1: FSKTM -> KAA (Walking Route)**
```
Shortest Route:

FSKTM -> UPM main gate -> KAA

Total Distance: 480 meters
Estimated Time: 6.86 minutes
```

**Example 2: FSKTM -> Library (Bus Route)**
```
Bus Route:

FSKTM -> SPE -> Library

Total Distance: 850 meters
Bus Travel Time: 2.83 minutes
Bus Speed: 300 meters/minute
```

**Example 3: Find Nearest Cafe from FSKTM**
```
Nearest Cafe: FSKTM

Route: FSKTM
Distance: 0 meters
Time: 0.00 minutes
```

**Example 4: Blocked Road Scenario**
```
! Road CLOSED for maintenance: FSKTM <-> UPM main gate

New Route:
FSKTM -> DKAP -> UPM main gate -> KAA

Total Distance: 660 meters
Estimated Time: 9.43 minutes
```

### System Features

- [x] Find shortest walking path between any two campus locations
- [x] Find shortest bus route between bus stops
- [x] Search for facilities (Cafe, Printing, Bus Stop, Convenience Store, etc.)
- [x] Find nearest facility from current location
- [x] Display total distance in meters
- [x] Calculate estimated walking time (70 meters/minute) and bus travel time (300 meters/minute)
- [x] Block roads for maintenance and find alternative routes dynamically
- [x] Interactive JavaFX user interface with dropdown selections
- [x] Real-time route recalculation when roads are blocked
- [x] Compare walking vs bus routes

---

## 4. Algorithm Analysis

### 4.1 Correctness Proof

We use the **Loop Invariant** technique to prove Dijkstra's Algorithm is correct.

#### Loop Invariant
> **When a vertex u is extracted from the priority queue, dist[u] equals the shortest path distance from the start vertex to u.**

#### Proof by Induction:

**Initialization:**
- Before the loop starts, the source vertex is assigned the distance of 0 (which is its shortest distance to itself)
- All other vertices are initialized with a distance of infinity indicating no paths are known yet.
- The loop invariant holds before the first iteration.

**Maintenance:**
- Assume the invariant holds for all vertices already extracted from the queue
- When we extract vertex u with minimum distance, we examine all its neighbors
- For each neighbor v, we check if going through u provides a shorter path
- If a shorter path is found, we update dist[v] accordingly.The updated vertex is then          inserted into the priority queue with its new tentative distance.
- The priority queue ensures we always process the vertex with the smallest tentative distance first

**Termination:**
- The algorithm terminates when the destination vertex is extracted from the queue (or the queue becomes empty)
- By the loop invariant, at this point dist[destination] is the shortest path distance from start to destination

#### Why the Greedy Choice Works

Dijkstra's algorithm makes a greedy choice at each step: it always selects the unvisited vertex with the smallest known distance. This greedy choice is safe because:

1. All edge weights are **non-negative** (distances cannot be negative)
2. Once a vertex is extracted from the priority queue, its shortest distance is finalized
3. Any other path to this vertex would have to go through an unvisited vertex with a larger or equal distance, making it impossible to be shorter

### 4.2 Time Complexity Analysis

Using **Binary Heap (PriorityQueue)** implementation:

| Case | Time Complexity | Explanation |
|------|----------------|-------------|
| **Best** | O((V + E) log V) | Priority queue operations dominate the running time |
| **Average** | O((V + E) log V) | Standard case with partial graph traversal |
| **Worst** | O((V + E) log V) | In the worst case, all reachable vertices and edges are processed before the destination is found |

Where:
- **V** = Number of vertices (campus locations)
- **E** = Number of edges (pathways between locations)
- **log V** = Time for extract-min and priority queue insertion operations on the binary heap

#### Detailed Breakdown:

| Operation | Cost | Frequency | Total |
|-----------|------|-----------|-------|
| Initialization | O(V) | 1 | O(V) |
| Extract-min from PQ | O(log V) | At most V times | O(V log V) |
| Insert updated node into PQ | O(log V) | At most E times | O(E log V) |
| **Total** | | | **O((V + E) log V)** |

> **Note:** With a Fibonacci Heap, the complexity can be improved to O(V log V + E), but for our campus graph (sparse graph where E ~ V), both implementations perform similarly.

### 4.3 Space Complexity

| Data Structure | Space |
|----------------|-------|
| Adjacency List | O(V + E) |
| Distance Map (dist) | O(V) |
| Predecessor Map (prev) | O(V) |
| Priority Queue | O(V + E) |
| **Total** | **O(V + E)** |


### 4.5 Asymptotic Analysis Summary

| Function | Growth Rate | Description |
|----------|-------------|-------------|
| T(V, E) | O((V + E) log V) | Overall time complexity |
| S(V, E) | O(V + E) | Overall space complexity |

**Key Properties:**
- **Complete**: Always finds the shortest path if one exists
- **Optimal**: Guarantees the minimum distance solution
- **Efficient for sparse graphs**: Our campus graph is sparse (E ~ O(V))
- **Greedy approach**: Local optimal choices lead to global optimum due to non-negative weights

---

## Team Members

| Name | Role | Responsibility |
|------|------|----------------|
| HASAN LABIBA ZARIIFA | Data Modeling and Graph Design | Campus graph structure, node/edge design |
| SINDYA A/P SOMU | Software Development and Implementation | JavaFX UI, coding, testing |
| KAYLA CALITA CHANDRA | Github and Quality Assurance Lead | GitHub Portfolio, documentation, QA |
| ZHANG YIZHOU | Algorithm Design & Complexity Analysis | Algorithm selection, pseudocode, correctness proof, time complexity analysis |

---

## References

1. Gbadamosi, et al. (2025). GIS-based Smart Campus Navigation System at Olusegun Agagu University of Science and Technology (OAUSTECH), Nigeria.

2. Kurniawan, et al. (2023). Graph-based navigation systems for optimizing route planning in complex environments.

3. Fernandez, et al. (2025). Navigation challenges at Universidad de Manila (UDM): Outdated maps and campus signage issues.

4. Cormen, T.H., Leiserson, C.E., Rivest, R.L., & Stein, C. (2009). *Introduction to Algorithms* (3rd Edition). MIT Press. Chapter 24: Single-Source Shortest Paths.

---

*Submitted in partial fulfillment of CCS4202 - Design and Analysis of Algorithm*  
*Universiti Putra Malaysia (UPM), 2025/2026*
