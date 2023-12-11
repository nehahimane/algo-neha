//import java.util.List;
//
//public class RunMaxCapSimulation {
//    RunMaxCapSimulation(){}
//
//    public Result runMaxCapSimulation(Graph graph, Vertex source, Vertex sink, String type) {
//        List<Vertex> augmentingPath = MaxCapSimulation.maxCapDijkstra(graph, source, sink);
////        graph.getCapacities();
//        System.out.println("Shortest Augmenting Path from " + source.id + " to " + sink.id + ":");
//        for (Vertex vertex : augmentingPath) {
//            System.out.print(vertex.id + " -> ");
//        }
//        System.out.println("END");
//
//        // Statistics
//        int paths = 0;
//        int totalLength = 0;
//        int maxLength = 0;
//
//        // Run Ford-Fulkerson
//        int maxFlow = 0;
//        FordFulkerson f = new FordFulkerson();
//        while (augmentingPath.size() > 1) {
//            paths++;
//            totalLength += augmentingPath.size();
//            maxLength = Math.max(maxLength, augmentingPath.size());
//
//            int minCapacity = f.findMinCapacity(augmentingPath);
//            f.updateResidualGraph(augmentingPath, minCapacity);
//            //graph.getCapacities();
//            maxFlow += minCapacity;
//
//            augmentingPath = MaxCapSimulation.maxCapDijkstra(graph, source, sink);
//
//            System.out.println("Shortest Augmenting Path from " + source.id + " to " + sink.id + ":");
//            for (Vertex vertex : augmentingPath) {
//                System.out.print(vertex.id + " -> ");
//            }
//            System.out.println("END");
//        }
//
//        // Statistics
//        double meanLength = (double) totalLength / paths;
//        double meanProportionalLength = (double) totalLength / maxLength;
//        int totalEdges = graph.vertices.values().stream().mapToInt(v -> v.neighbors.size()).sum();
//
//        System.out.println("Maximum Flow: " + maxFlow);
//        System.out.println("Paths: " + paths);
//        System.out.println("Mean Length: " + meanLength);
//        System.out.println("Mean Proportional Length: " + meanProportionalLength);
//        System.out.println("Total Edges: " + totalEdges);
//
//        return new Result(type, maxFlow, paths, meanLength, meanProportionalLength, totalEdges);
//
//    }
//}

import java.util.*;

public class MaxCapacity {

    public SimulationMetrics runMaxCapSimulation(NodeGraph graph, VertexNode source, VertexNode sink, String type) {
        List<VertexNode> augmentingPath = maxCapDijkstra(graph, source, sink);

        System.out.println("Shortest Augmenting Path from " + source.id + " to " + sink.id + ":");
        for (VertexNode vertex : augmentingPath) {
            System.out.print(vertex.id + " -> ");
        }
        System.out.println("END");

        // Statistics
        int paths = 0;
        int totalLength = 0;
        int maxLength = 0;

        // Run Ford-Fulkerson
        int maxFlow = 0;
        FordFulkersonMaxFlow f = new FordFulkersonMaxFlow();
        while (augmentingPath.size() > 1) {
            paths++;
            totalLength += augmentingPath.size();
            maxLength = Math.max(maxLength, augmentingPath.size());

            int minCapacity = f.findMinCapacity(augmentingPath);
            f.updateResidualGraph(augmentingPath, minCapacity);
            // graph.getCapacities();
            maxFlow += minCapacity;

            augmentingPath = maxCapDijkstra(graph, source, sink);

            System.out.println("Shortest Augmenting Path from " + source.id + " to " + sink.id + ":");
            for (VertexNode vertex : augmentingPath) {
                System.out.print(vertex.id + " -> ");
            }
            System.out.println("END");
        }

        // Statistics
        double meanLength = (double) totalLength / paths;
        double meanProportionalLength = (double) totalLength / maxLength;
        int totalEdges = graph.vertices.values().stream().mapToInt(v -> v.neighbors.size()).sum();

        System.out.println("Maximum Flow: " + maxFlow);
        System.out.println("Paths: " + paths);
        System.out.println("Mean Length: " + meanLength);
        System.out.println("Mean Proportional Length: " + meanProportionalLength);
        System.out.println("Total Edges: " + totalEdges);

        return new SimulationMetrics(type, maxFlow, paths, meanLength, meanProportionalLength, totalEdges);
    }

    private List<VertexNode> maxCapDijkstra(NodeGraph graph, VertexNode source, VertexNode sink) {
        System.out.println("Inside dijkstra max cap");
        if (graph == null || source == null || sink == null) {
            throw new IllegalArgumentException("Input arguments cannot be null.");
        }

        Map<VertexNode, VertexNode> predecessors = new HashMap<>();
        Map<VertexNode, Integer> maxCapacities = new HashMap<>();
        PriorityQueue<VertexNode> queue = new PriorityQueue<>(Comparator.comparingInt(maxCapacities::get).reversed());

        for (VertexNode vertex : graph.vertices.values()) {
            maxCapacities.put(vertex, Integer.MIN_VALUE);
            predecessors.put(vertex, null);
        }

        int maxCapacity = Integer.MIN_VALUE;
        maxCapacities.put(source, Integer.MAX_VALUE);
        queue.add(source);

        while (!queue.isEmpty()) {
            VertexNode currentVertex = queue.poll();
            if (currentVertex.id == sink.id) {
                sink = currentVertex;
                break;
            }
            for (Map.Entry<VertexNode, Integer> entry : currentVertex.neighbors.entrySet()) {
                VertexNode neighbor = entry.getKey();
                int capacity = entry.getValue();

                // Consider only edges with non-zero capacity
                if (capacity > 0) {
                    int maxCap = Math.min(maxCapacities.get(currentVertex), capacity);

                    Integer cap = maxCapacities.get(neighbor);
                    if (cap == null || maxCap > cap) {
                        maxCapacities.put(neighbor, maxCap);
                        predecessors.put(neighbor, currentVertex);
                        queue.add(neighbor);
                    }
                }
            }
        }

        Integer sinkCap = maxCapacities.get(sink);
        if (sinkCap == null || sinkCap == Integer.MIN_VALUE) {
            // The sink is not reachable from the source
            return Collections.emptyList();
        }

        // Store the maximum capacity of the critical edge
        maxCapacity = maxCapacities.get(sink);

        List<VertexNode> path = new ArrayList<>();
        for (VertexNode currentVertex = sink; currentVertex != null; currentVertex = predecessors.get(currentVertex)) {
            path.add(currentVertex);
        }
        Collections.reverse(path);

        // Print or use maxCapacity as needed
        System.out.println("Maximum Capacity: " + maxCapacity);

        return path;
    }
}
