import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class SimulationFactory {
    private Particle particle;

    public SimulationFactory(SimulationConfig simulationConfig) {
        switch (simulationConfig.getAlgorithm()) {
            case "verlet":
                VerletDampedOscilations();
                break;
            case "beeman":
                System.out.println("Beeman...");
                break;
            case "gear":
                System.out.println("Gear...");
            default:
                System.out.println("Defaulted");
        }

    }

    public void VerletDampedOscilations() {
        this.particle = new Particle(Utils.INITIAL_POSITION, Utils.MASS, Utils.INITIAL_SPEED);

    }

    public static SimulationConfig readConfig(String path){
        Gson gson = new Gson();
        SimulationConfig sConfig = null;
        try (FileReader reader = new FileReader(path)) {
            sConfig = gson.fromJson(reader, SimulationConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sConfig;
    }

    public static void main(String[] args) {
        //Leer de un JSON parametros
        String FILE_PATH = "Simulation/Input/input.json";
        SimulationConfig simulationConfig = readConfig(FILE_PATH);

        System.out.println("Starting simulation with " + simulationConfig.getAlgorithm());
        SimulationFactory simulationFactory = new SimulationFactory(simulationConfig);
        System.out.println("Done!");
    }
}
