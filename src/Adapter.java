import java.io.*;
import java.util.*;

class NodeGraph {
    Map<Integer, VertexNode> vertices = new HashMap<>();
    NodeGraph(){};
    // Deep copy constructor
//    Graph(Graph other) {
//        // Create a new instance of each vertex
//        for (Map.Entry<Integer, Vertex> entry : other.vertices.entrySet()) {
//            int id = entry.getKey();
//            Vertex originalVertex = entry.getValue();
//            Vertex newVertex = new Vertex(id);
//            this.vertices.put(id, newVertex);
//
//            // Copy neighbors
//            for (Map.Entry<Vertex, Integer> neighborEntry : originalVertex.neighbors.entrySet()) {
//                Vertex originalNeighbor = neighborEntry.getKey();
//                int capacity = neighborEntry.getValue();
//                Vertex newNeighbor = this.vertices.computeIfAbsent(originalNeighbor.id, Vertex::new);
//                newVertex.neighbors.put(newNeighbor, capacity);
//            }
//        }
//    }

    NodeGraph(NodeGraph other) {
        other.vertices.forEach((id, originalVertex) -> {
            VertexNode newVertex = new VertexNode(id);
            this.vertices.put(id, newVertex);

            originalVertex.neighbors.forEach((originalNeighbor, capacity) -> {
                VertexNode newNeighbor = this.vertices.computeIfAbsent(originalNeighbor.id, VertexNode::new);
                newVertex.neighbors.put(newNeighbor, capacity);
            });
        });
    }




//    void addEdge(int uId, int vId, int capacity) {
//        Vertex u = vertices.computeIfAbsent(uId, Vertex::new);
//        Vertex v = vertices.computeIfAbsent(vId, Vertex::new);
//        u.neighbors.put(v, capacity);
//    }

    void addEdge(int uId, int vId, int capacity) {
        vertices.computeIfAbsent(uId, VertexNode::new)
                .neighbors
                .put(vertices.computeIfAbsent(vId, VertexNode::new), capacity);
    }

//    Vertex getRandomVertex() {
//        List<Vertex> vertexList = new ArrayList<>(vertices.values());
//        return vertexList.get(new Random().nextInt(vertexList.size()));
//    }

    VertexNode getRandomVertex() {
        return vertices.values().stream()
                .skip(new Random().nextInt(vertices.size()))
                .findFirst()
                .orElse(null);
    }

//    void getCapacities() {
//        for (Vertex u : vertices.values()) {
//            for (Map.Entry<Vertex, Integer> entry : u.neighbors.entrySet()) {
//                Vertex v = entry.getKey();
//                int capacity = entry.getValue();
//
//                System.out.println("Edge: " + u.id + " -> " + v.id + ", Capacity: " + capacity);
//            }
//        }
//    }

//    void getCapacities() {
//        vertices.values().forEach(u ->
//                u.neighbors.forEach((v, capacity) ->
//                        System.out.println("Edge: " + u.id + " -> " + v.id + ", Capacity: " + capacity)
//                )
//        );
//    }
}

//class Vertex {
//    int id;
//    Map<Vertex, Integer> neighbors = new HashMap<>();
//
//    Vertex(int id) {
//        this.id = id;
//    }
//
//    Vertex(Vertex original) {
//        this.id = original.id;
//        // Create a new map for neighbors and copy the entries
//        this.neighbors = new HashMap<>(original.neighbors.size());
//        for (Map.Entry<Vertex, Integer> entry : original.neighbors.entrySet()) {
//            Vertex neighborCopy = new Vertex(entry.getKey());
//            this.neighbors.put(neighborCopy, entry.getValue());
//        }
//    }
//}

class VertexNode {
    int id;
    Map<VertexNode, Integer> neighbors = new HashMap<>();

    VertexNode(int id) {
        this.id = id;
    }

    VertexNode(VertexNode original) {
        this.id = original.id;
        this.neighbors = original.neighbors.entrySet().stream()
                .collect(
                        HashMap::new,
                        (map, entry) -> map.put(new VertexNode(entry.getKey()), entry.getValue()),
                        HashMap::putAll
                );
    }
}

public class Adapter {

//    static Graph readGraphFromFile(String fileName) throws IOException {
//        Graph graph = new Graph();
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                int uId = Integer.parseInt(parts[0]);
//                for (int i = 1; i < parts.length; i++) {
//                    String[] edge = parts[i].split(":");
//                    int vId = Integer.parseInt(edge[0]);
//                    int capacity = Integer.parseInt(edge[1]);
//                    graph.addEdge(uId, vId, capacity);
//                }
//            }
//        }
//        return new Graph(graph);
//    }


    static NodeGraph readGraphFromFile(String fileName) throws IOException {
        NodeGraph graph = new NodeGraph();

        try (Scanner scanner = new Scanner(new FileInputStream(new File(fileName)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int uId = Integer.parseInt(parts[0]);

                for (int i = 1; i < parts.length; i++) {
                    String[] edge = parts[i].split(":");
                    int vId = Integer.parseInt(edge[0]);
                    int capacity = Integer.parseInt(edge[1]);
                    graph.addEdge(uId, vId, capacity);
                }
            }
        }

        return new NodeGraph(graph);
    }

    // Function to find the longest path in a graph using BFS
    static VertexNode findLongestPath(NodeGraph graph, VertexNode source) {
        Map<VertexNode, Integer> distance = new HashMap<>();
        Queue<VertexNode> queue = new LinkedList<>();
        queue.offer(source);
        distance.put(source, 0);

        VertexNode farthestNode = source;
        int maxDistance = 0;
        System.out.println(source.id);
        while (!queue.isEmpty()) {
            VertexNode current = queue.poll();
            for (VertexNode neighbor : current.neighbors.keySet()) {
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, distance.get(current) + 1);
                    queue.offer(neighbor);

                    if (distance.get(neighbor) > maxDistance) {
                        maxDistance = distance.get(neighbor);
                        farthestNode = neighbor;
                        System.out.println(farthestNode.id);
                    }
                }
            }
        }

        return farthestNode;
    }

    public static void main(String[] args) throws IOException {
        String fileName = "graph_adjacency_list_100_0.3_50.csv";
        NodeGraph graph = readGraphFromFile(fileName);

        // Select a random source and find the longest path to determine the sink
        VertexNode source = graph.getRandomVertex();
        VertexNode sink = findLongestPath(graph, source);

        System.out.println(source.id +", "+ sink.id);
        List<SimulationMetrics> results = new ArrayList<>();

        // 1. Shortest Augmenting Path (SAP)
        NodeGraph g1 =  readGraphFromFile(fileName);
        VertexNode sourceCopy1 = new VertexNode(source), sinkCopy1 = new VertexNode(sink);
        SAP rss = new SAP();
        SimulationMetrics resultSAP = rss.runSAPSimulation(g1, sourceCopy1, sinkCopy1, "SAP");
        results.add(resultSAP);
        System.out.println();
        System.gc();

        // 2. DFS-Like
        NodeGraph g2 =  readGraphFromFile(fileName);
        VertexNode sourceCopy2 = new VertexNode(source);
        VertexNode sinkCopy2 = new VertexNode(sink);
        SimulationMetrics resultDFSLike = new DFSLike().runDFSLikeSimulation(g2, sourceCopy2, sinkCopy2, "DFS-Like");
        results.add(resultDFSLike);
        System.gc();


        // 3. Maximum Capacity (MaxCap)
        NodeGraph g3 =  readGraphFromFile(fileName);
        VertexNode sourceCopy3 = new VertexNode(source);
        VertexNode sinkCopy3 = new VertexNode(sink);
        SimulationMetrics resultMaxCap = new MaxCapacity().runMaxCapSimulation(g3, sourceCopy3, sinkCopy3, "Max-Cap");
        results.add(resultMaxCap);
        System.gc();


        // 4. Random
        NodeGraph g4 =  readGraphFromFile(fileName);
        VertexNode sourceCopy4 = new VertexNode(source);
        VertexNode sinkCopy4 = new VertexNode(sink);
        SimulationMetrics resultRandom = new RandomAnalysis().runRandomSimulation(g4, sourceCopy4, sinkCopy4, "Random");
        results.add(resultRandom);

        // Display
        System.out.println();
        System.out.println(String.format("%-10s\t%-5s\t%-5s\t%-5s\t%-5s\t%-5s\t%-5s\t%-5s",
                "Algorithm", "n", "r", "upperCap", "paths", "ML", "MPL", "totalEdges"));
        for (SimulationMetrics result : results) {
            String s = result.toFormattedString(100, 0.2, 2);
            System.out.println(s);
        }

    }
}
