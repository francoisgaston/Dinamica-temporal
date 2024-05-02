public class PlanetsMain {

    public static void main(String[] args) {
        String inputPath = "Simulation/Input/methodInput.json";
        SimulationConfig simulationConfig = Utils.readConfig(inputPath);
        Utils.writeStatus(simulationConfig);


        simulatePlanets(velocity, alpha, dt, totalTime);

    }

    public static void simulatePlanets( double vel, double alpha, double deltaTime, double totalTime){
        double[] r = {0, 0};


        for(double actualTime = 0; actualTime < totalTime; actualTime += deltaTime){
            r = Methods.GearMethod(r[0], r[1], deltaTime, acelerationFuction, Utils.ALPHA_POSITION);
            bw.write(actualTime + "," + r[0] + "," + r[1] + "\n");
        }
    }
}
