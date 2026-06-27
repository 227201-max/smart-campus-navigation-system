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
| Kolej Canselor | Residential college |
| Guard House KC | Security checkpoint |
| Engineering | Engineering Faculty |
| Hostel | Student accommodation |

### Graph Representation

The campus is modeled as a **weighted undirected graph**:
- **Vertices (Nodes)**: Campus locations/buildings
- **Edges (Paths)**: Pathways connecting locations
- **Weights**: Distance in meters

### Graph Edges (Campus Pathways)

| From | To | Distance (meters) |
|------|-----|-------------------|
| FBMK | KAA | 150 |
| FSKTM | DKAP | 80 |
| FSKTM | Kolej Canselor | 250 |
| FSKTM | UPM main gate | 60 |
| UPM main gate | KAA | 300 |
| UPM main gate | DKAP | 150 |
| FSKTM | Guard House KC | 80 |
| Guard House KC | FBMK | 250 |
| Engineering | Hostel | 500 |
| Engineering | Main Gate | 600 |

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
Algorithm Dijkstra(Graph, Start, Destination):

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
            if edge(u,v) is BLOCKED:
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
| **Input Layer** | User selects start and destination from dropdown menus; user can also mark roads as closed for maintenance |
| **Processing Layer** | System initializes distances, places nodes into a priority queue, and executes Dijkstra's algorithm to find the shortest path |
| **Output Layer** | System outputs the shortest route, total distance, and estimated travel time |

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
    public static Route findShortestPath(Graph graph, String start, String end) {
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
                if (!edge.active) continue; // skip blocked roads
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

#### Main.java - JavaFX Application
```java
package com.ccs3402.lab.smartcampusnavigationsystem.graphs.Application;

import com.ccs3402.lab.smartcampusnavigationsystem.graphs.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private Graph graph = new Graph();

    @Override
    public void start(Stage stage) {
        buildGraph();

        Label title = new Label("Smart Campus Navigation System");
        title.getStyleClass().add("title");

        ComboBox<String> startBox = new ComboBox<>();
        ComboBox<String> endBox = new ComboBox<>();
        startBox.getItems().addAll(graph.getNodes());
        endBox.getItems().addAll(graph.getNodes());

        Button findBtn = new Button("Find Shortest Route");
        TextArea output = new TextArea();
        output.setEditable(false);

        // Route finding action
        findBtn.setOnAction(e -> {
            String start = startBox.getValue();
            String end = endBox.getValue();
            Route result = DijkstraAlgorithm.findShortestPath(graph, start, end);

            if (result.getTotalDistance() == Integer.MAX_VALUE) {
                output.setText("No route available due to road maintenance.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Shortest Route:\n\n");
            for (int i = 0; i < result.getPath().size(); i++) {
                sb.append(result.getPath().get(i));
                if (i < result.getPath().size() - 1) sb.append(" -> ");
            }
            sb.append("\n\nTotal Distance: ").append(result.getTotalDistance()).append(" meters");
            double time = result.getTotalDistance() / 80.0;
            sb.append("\nEstimated Time: ").append(String.format("%.2f", time)).append(" minutes");
            output.setText(sb.toString());
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(title, startBox, endBox, findBtn, output);

        Scene scene = new Scene(root, 500, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Campus Navigation System");
        stage.setScene(scene);
        stage.show();
    }

    private void buildGraph() {
        graph.addEdge("FBMK", "KAA", 150);
        graph.addEdge("FSKTM", "DKAP", 80);
        graph.addEdge("FSKTM", "Kolej Canselor", 250);
        graph.addEdge("FSKTM", "UPM main gate", 60);
        graph.addEdge("UPM main gate", "KAA", 300);
        graph.addEdge("UPM main gate", "DKAP", 150);
        graph.addEdge("FSKTM", "Guard House KC", 80);
        graph.addEdge("Guard House KC", "FBMK", 250);
        graph.addEdge("Engineering", "Hostel", 500);
        graph.addEdge("Engineering", "Main Gate", 600);
    }

    public static void main(String[] args) { launch(); }
}
```

> **Full source code available in the `src/` directory.**

### Program Output Examples

**Example 1: FSKTM -> KAA (Shortest Route)**
```
Shortest Route:

FSKTM -> UPM main gate -> KAA

Total Distance: 360 meters
Estimated Time: 4.50 minutes
```

**Example 2: FSKTM -> Kolej Canselor**
```
Shortest Route:

FSKTM -> Kolej Canselor

Total Distance: 250 meters
Estimated Time: 3.12 minutes
```

**Example 3: Blocked Road Scenario**
```
! Road CLOSED for maintenance: UPM main gate <-> KAA

System automatically recalculates and finds alternative route.
```

### System Features

- [x] Find shortest path between any two campus locations
- [x] Display total distance in meters
- [x] Calculate estimated walking time (assuming 80 meters/minute walking speed)
- [x] Block roads for maintenance and find alternative routes dynamically
- [x] Interactive JavaFX user interface with dropdown selections
- [x] Real-time route recalculation when roads are blocked

---

## 4. Algorithm Analysis

### 4.1 Correctness Proof

We use the **Loop Invariant** technique to prove Dijkstra's Algorithm is correct.

#### Loop Invariant
> **When a vertex u is extracted from the priority queue, dist[u] equals the shortest path distance from the start vertex to u.**

#### Proof by Induction:

**Initialization:**
- Before the loop starts, the start vertex has distance 0 (which is its shortest distance to itself)
- All other vertices have distance infinity (no path known yet)
- The invariant holds.

**Maintenance:**
- Assume the invariant holds for all vertices already extracted from the queue
- When we extract vertex u with minimum distance, we examine all its neighbors
- For each neighbor v, we check if going through u provides a shorter path
- If a shorter path is found, we update dist[v] accordingly
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
| **Best** | O((V + E) log V) | Destination is reached early, fewer vertices need to be processed |
| **Average** | O((V + E) log V) | Standard case with partial graph traversal |
| **Worst** | O((V + E) log V) | All vertices and edges must be processed |

Where:
- **V** = Number of vertices (campus locations)
- **E** = Number of edges (pathways between locations)
- **log V** = Time for extract-min and decrease-key operations on the binary heap

#### Detailed Breakdown:

| Operation | Cost | Frequency | Total |
|-----------|------|-----------|-------|
| Initialization | O(V) | 1 | O(V) |
| Extract-min from PQ | O(log V) | At most V times | O(V log V) |
| Decrease-key in PQ | O(log V) | At most E times | O(E log V) |
| **Total** | | | **O((V + E) log V)** |

> **Note:** With a Fibonacci Heap, the complexity can be improved to O(V log V + E), but for our campus graph (sparse graph where E ~ V), both implementations perform similarly.

### 4.3 Space Complexity

| Data Structure | Space |
|----------------|-------|
| Adjacency List | O(V + E) |
| Distance Array (dist[]) | O(V) |
| Predecessor Array (prev[]) | O(V) |
| Priority Queue | O(V) |
| **Total** | **O(V + E)** |

### 4.4 Recurrence Relation

Let T(V, E) be the time complexity for a graph with V vertices and E edges.

```
T(V, E) = T(V-1, E') + O(log V + degree(u) * log V)

Base case: T(0, 0) = O(1)
```

Where at each step:
- One vertex u is extracted from the priority queue
- E' is the remaining edges after removing u's edges
- O(log V) for extract-min operation
- O(degree(u) * log V) for relaxing all adjacent edges

**Summing over all vertices:**
```
T(V, E) = O(1) + sum over all vertices [O(log V) + O(degree(u) * log V)]
        = O(V log V) + O(E log V)        [since sum of all degrees = 2E]
        = O((V + E) log V)
```

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
