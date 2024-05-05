public class SimulationConfig {
    private String algorithm;
    private double totalTime;
    private double deltaT;
    private double deltaW;
    private double alpha;
    public SimulationConfig(){

    }
    public double getDeltaW() {
        return deltaW;
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
    public double getAlpha() {
        return alpha;
    }
}
