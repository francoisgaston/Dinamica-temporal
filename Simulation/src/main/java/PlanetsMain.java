public class PlanetsMain {

    public static void main(String[] args) {
        String inputPath = "Simulation/Input/methodInput.json";
        SimulationConfig simulationConfig = Utils.readConfig(inputPath);
        Utils.writeStatus(simulationConfig);

        System.out.println("ACA VA LO DE LOS PLANETAS");
    }


}
