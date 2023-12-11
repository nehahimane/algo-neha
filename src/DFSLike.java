import java.util.List;

import java.util.*;

class ExploreDFS {

    // Dijkstra's algorithm treating edges with non-zero capacity
    static List<VertexNode> dfsLikeDijkstra(NodeGraph graph, VertexNode source, VertexNode sink) {
        System.out.println("Inside dijkstra dfs like");

        if (graph == null || source == null || sink == null) {
            throw new IllegalArgumentException("Input arguments cannot be null.");
        }

        Map<VertexNode, VertexNode> predecessors = new HashMap<>();
        Map<VertexNode, Integer> distances = new HashMap<>();
        PriorityQueue<VertexNode> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (VertexNode vertex : graph.vertices.values()) {
            distances.put(vertex, Integer.MAX_VALUE / 2); // Avoid integer overflow
            predecessors.put(vertex, null);
        }

        int decreasingCounter = 0;
        distances.put(source, 0);
        queue.add(source);

        while (!queue.isEmpty()) {
            VertexNode currentVertex = queue.poll();
            if(currentVertex.id == sink.id) {
                sink = currentVertex;
            }
            // Continue exploration even if we reach the sink
            for (Map.Entry<VertexNode, Integer> entry : currentVertex.neighbors.entrySet()) {
                VertexNode neighbor = entry.getKey();
                int capacity = entry.getValue();

                // if v.d is infinity, decrease the key value for v from infinity to a decreasing
                // counter value in Q. If v.d is not infinity, do not change v’s key value.
                if (capacity > 0) {
                    Integer currentDistance = distances.get(neighbor);

                    if (currentDistance == null || currentDistance == Integer.MAX_VALUE / 2) {
                        distances.put(neighbor, decreasingCounter--);
                        predecessors.put(neighbor, currentVertex);
                        queue.add(neighbor);

                        if(neighbor.id == sink.id) {
                            sink = neighbor;
                        }
                    }
                }
            }
        }

        Integer sinkDistance = distances.get(sink);
        if (sinkDistance == null || sinkDistance == Integer.MAX_VALUE / 2) {
            // The sink is not reachable from the source
            return Collections.emptyList();
        }

        // Explore the predecessors to construct the path
        List<VertexNode> path = new ArrayList<>();
        for (VertexNode currentVertex = sink; currentVertex != null; currentVertex = predecessors.get(currentVertex)) {
            path.add(currentVertex);
        }
        Collections.reverse(path);
        return path;
    }
}


public class DFSLike {
    public SimulationMetrics runDFSLikeSimulation(NodeGraph graph, VertexNode source, VertexNode sink, String type){
        List<VertexNode> augmentingPath = ExploreDFS.dfsLikeDijkstra(graph, source, sink);
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

            maxFlow += minCapacity;

            augmentingPath = ExploreDFS.dfsLikeDijkstra(graph, source, sink);

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

        System.out.println("DFS Like Results");
        System.out.println("Maximum Flow: " + maxFlow);
        System.out.println("Paths: " + paths);
        System.out.println("Mean Length: " + meanLength);
        System.out.println("Mean Proportional Length: " + meanProportionalLength);
        System.out.println("Total Edges: " + totalEdges);

        return new SimulationMetrics(type, maxFlow, paths, meanLength, meanProportionalLength, totalEdges);

    }
}
