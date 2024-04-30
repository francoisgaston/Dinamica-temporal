import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SimulationFactory {

    public static void main(String[] args) {
        //Leer de un JSON parametros
        String FILE_PATH = "Simulation/Input/input.json";
        SimulationConfig simulationConfig = readConfig(FILE_PATH);
        String SIM_INPUT_FILE = "Simulation/Output/VerletInput_" + Double.toString(simulationConfig.getDeltaT()).substring(2) + ".json";

        System.out.println("Starting simulation with " + simulationConfig.getAlgorithm());
        SimulationFactory simulationFactory = new SimulationFactory(simulationConfig);
        simulationFactory.writeStatus(simulationConfig, SIM_INPUT_FILE);
        System.out.println("Done!");
    }

    public SimulationFactory(SimulationConfig simulationConfig) {
        switch (simulationConfig.getAlgorithm()) {
            case "verlet":
                VerletDampedOscilations(simulationConfig);
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

    public void VerletDampedOscilations(SimulationConfig simulationConfig) {
        Particle particle = new Particle(Utils.INITIAL_POSITION, Utils.MASS, Utils.INITIAL_SPEED);
        double deltaT = simulationConfig.getDeltaT();
        String FILE_PATH = "Simulation/Output/VerletOutput_" + Double.toString(deltaT).substring(2) + ".csv";

        try {
            FileWriter fw = new FileWriter(FILE_PATH);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("timeFrame,position,velocity");
            bw.newLine();
            bw.write("0," + particle.getPosition() + "," + particle.getSpeed());
            bw.newLine();
            double previousPosition = particle.getPosition() - deltaT * particle.getSpeed() +
                    (-Utils.K * particle.getPosition() - Utils.GAMMA * particle.getSpeed()) * (deltaT * deltaT) / (2 * particle.getMass());
            particle.setPreviousPosition(previousPosition);

            for(double currentTimeFrame = 0; currentTimeFrame <  simulationConfig.getTotalTime(); currentTimeFrame += deltaT) {
                // Calculate following pos
                double nextPosition = 2 * particle.getPosition() - particle.getPreviousPosition() +
                        (-Utils.K * particle.getPosition() - Utils.GAMMA * particle.getSpeed()) * (deltaT * deltaT) / (particle.getMass());

                // Calculate the second following pos
                double secondNextPosition = 2 * nextPosition - particle.getPosition() +
                        (-Utils.K * nextPosition - Utils.GAMMA * particle.getSpeed()) * (deltaT * deltaT) / (particle.getMass());

                // Calculate following speed
                double nextSpeed = (secondNextPosition - particle.getPosition()) / (2 * deltaT);

                particle.setPreviousPosition(particle.getPosition());
                particle.setPosition(nextPosition);
                particle.setSpeed(nextSpeed);

                bw.write(currentTimeFrame + "," + particle.getPosition() + "," + particle.getSpeed());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void writeStatus(SimulationConfig simulationConfig, String statusPath){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("algorithm", simulationConfig.getAlgorithm());
            jsonObject.put("totalTime", simulationConfig.getTotalTime());

            FileWriter writer_status = new FileWriter(statusPath);
            writer_status.write(jsonObject.toString());
            writer_status.close();

        } catch(IOException e){
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}
