public class SimulationConfig {
    private String algorithm;
    private double totalTime;
    private double deltaT;

    public SimulationConfig(){

    }

    public double getDeltaT() {
        return deltaT;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
