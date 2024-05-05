public class SimulationConfig {
    private double totalTime;
    private double deltaT;
    private double deltaW;
    private double alpha;
    private int initialTime;
    public SimulationConfig(){

    }
    public int getInitialTime() {
        return initialTime;
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
    public double getAlpha() {
        return alpha;
    }
}
