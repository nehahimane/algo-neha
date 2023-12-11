import java.util.List;

public class FordFulkersonMaxFlow {
//     public int findMinCapacity(List<VertexNode> path) {
//        int minCapacity = Integer.MAX_VALUE;
//
//        for (int i = 0; i < path.size() - 1; i++) {
//            VertexNode u = path.get(i);
//            VertexNode v = path.get(i + 1);
//
//            int capacity = u.neighbors.get(v);
//            minCapacity = Math.min(minCapacity, capacity);
//        }
//
//        return minCapacity;
//    }

    public int findMinCapacity(List<VertexNode> path) {
        int minCapacity = Integer.MAX_VALUE;

        VertexNode prev = null;
        for (VertexNode current : path) {
            if (prev != null) {
                int capacity = prev.neighbors.get(current);
                minCapacity = Math.min(minCapacity, capacity);
            }
            prev = current;
        }

        return minCapacity;
    }

//    public void updateResidualGraph(List<VertexNode> path, int minCapacity) {
//        for (int i = 0; i < path.size() - 1; i++) {
//            VertexNode u = path.get(i);
//            VertexNode v = path.get(i + 1);
//
//            int originalCapacity = u.neighbors.get(v);
//            int forwardFlow = Math.min(originalCapacity, minCapacity);
//
//            // Update forward edge
//            u.neighbors.put(v, Math.max(0, originalCapacity - forwardFlow));
//
//            // Check if backward edge already exists
//            if (v.neighbors.containsKey(u)) {
//                // Update backward edge (subtract forward flow)
//                v.neighbors.put(u, v.neighbors.get(u) - forwardFlow);
//            } else {
//                // Add backward edge (create if it doesn't exist)
//                v.neighbors.put(u, forwardFlow);
//            }
//        }
//    }

    public void updateResidualGraph(List<VertexNode> path, int minCapacity) {
        VertexNode prev = null;
        for (VertexNode current : path) {
            if (prev != null) {
                int originalCapacity = prev.neighbors.get(current);
                int forwardFlow = Math.min(originalCapacity, minCapacity);

                // Update forward edge
                prev.neighbors.put(current, Math.max(0, originalCapacity - forwardFlow));

                // Check if backward edge already exists
                if (current.neighbors.containsKey(prev)) {
                    // Update backward edge (subtract forward flow)
                    current.neighbors.put(prev, current.neighbors.get(prev) - forwardFlow);
                } else {
                    // Add backward edge (create if it doesn't exist)
                    current.neighbors.put(prev, forwardFlow);
                }
            }
            prev = current;
        }
    }

}
