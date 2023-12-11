public class SimulationMetrics {
    private String simIdentifier;  // Identifier for the simulation
    private int maxFlow;
    private int paths;
    private double meanLength;
    private double meanProportionalLength;
    private int totalEdges;

    SimulationMetrics(){}

    // Constructor
    public SimulationMetrics(String simIdentifier, int maxFlow, int paths, double meanLength, double meanProportionalLength, int totalEdges) {
        this.simIdentifier = simIdentifier;
        this.maxFlow = maxFlow;
        this.paths = paths;
        this.meanLength = meanLength;
        this.meanProportionalLength = meanProportionalLength;
        this.totalEdges = totalEdges;
    }

    public String getsimIdentifier() {
        return simIdentifier;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public int getPaths() {
        return paths;
    }

    public double getMeanLength() {
        return meanLength;
    }

    public double getMeanProportionalLength() {
        return meanProportionalLength;
    }

    public int getTotalEdges() {
        return totalEdges;
    }

    public String toFormattedString(int n, double r, int upperCap) {
        return String.format("%-10s\t%-5s\t%-5.2f\t%-10s\t%-7s\t%-5.2f\t%-5.2f\t%-5s",
                simIdentifier, n, r, upperCap, paths, meanLength, meanProportionalLength, totalEdges);
    }


}
