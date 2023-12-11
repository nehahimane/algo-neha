import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int n = 100;
        double r = 0.3;
        int upperCap = 50;
        String outputFileName = "graph_adjacency_list_"+n+"_"+r+"_"+upperCap+".csv";
        GraphCreator graphGenerator = new GraphCreator();
        graphGenerator.generateSinkSourceGraph(n, r, upperCap, outputFileName);

    }

    static class GraphCreator {

        public void generateSinkSourceGraph(int n, double r, int upperCap, String outputFileName) {
            Set<Vertex> vertices = new HashSet<>();
            Set<Edge> edges = new HashSet<>();

            // Create vertices and assign random coordinates
            for (int i = 0; i < n; i++) {
                Vertex vertex = new Vertex();
                vertices.add(vertex);
            }

            // Generate edges based on distance and random capacities
            for (Vertex u : vertices) {
                for (Vertex v : vertices) {
                    if (!u.equals(v) && Math.pow(u.x - v.x, 2) + Math.pow(u.y - v.y, 2) <= Math.pow(r, 2)) {
                        double rand = Math.random();
                        Edge edge;
                        if (rand < 0.5) {
                            edge = new Edge(u, v);
                        } else {
                            edge = new Edge(v, u);
                        }
                        // Ensure only one directed edge is added
                        if (!edges.contains(edge)) {
                            edges.add(edge);
                        }
                    }
                }
            }

            // Assign random capacities to edges
            for (Edge edge : edges) {
                edge.capacity = new Random().nextInt(upperCap) + 1;
            }

            // Build adjacency list
            Map<Vertex, Map<Vertex, Integer>> adjacencyList = new HashMap<>();
            for (Edge edge : edges) {
                adjacencyList.computeIfAbsent(edge.u, k -> new HashMap<>()).put(edge.v, edge.capacity);
            }

            // Write the graph to a CSV file (Adjacency List)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                for (Vertex vertex : adjacencyList.keySet()) {
                    writer.write(vertex.toCsvString(adjacencyList.get(vertex)));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        static class Vertex {
            static int idCounter = 1;
            int id;
            double x;
            double y;

            Vertex() {
                this.id = idCounter++;
                this.x = Math.random();
                this.y = Math.random();
            }

            String toCsvString(Map<Vertex, Integer> neighbors) {
                StringBuilder builder = new StringBuilder();
                builder.append(id).append(",");
                for (Vertex neighbor : neighbors.keySet()) {
                    builder.append(neighbor.id).append(":").append(neighbors.get(neighbor)).append(",");
                }
                // Remove the trailing comma
                builder.deleteCharAt(builder.length() - 1);
                return builder.toString();
            }
        }

        static class Edge {
            Vertex u;
            Vertex v;
            int capacity;

            Edge(Vertex u, Vertex v) {
                this.u = u;
                this.v = v;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Edge edge = (Edge) obj;
                return (u.equals(edge.u) && v.equals(edge.v)) || (u.equals(edge.v) && v.equals(edge.u));
            }

            @Override
            public int hashCode() {
                return u.hashCode() + v.hashCode();
            }
        }


    }

}