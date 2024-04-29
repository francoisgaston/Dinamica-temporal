import com.google.gson.Gson;
import jdk.jshell.execution.Util;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SimulationFactory {
    private Particle particle;

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
        particle = new Particle(Utils.INITIAL_POSITION, Utils.MASS, Utils.INITIAL_SPEED);
        double deltaT = Math.pow(10,-3);
        String FILE_PATH = "Simulation/Output/VerletOutput_" + Double.toString(deltaT).substring(2) + ".csv";

        try {
            FileWriter fw = new FileWriter(FILE_PATH);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("time frame,position");
            bw.write("0," + particle.getPosition());

            for(double currentTimeFrame = 0; currentTimeFrame <  simulationConfig.getTotalTime(); currentTimeFrame += deltaT) {
                // current pos will be previous pos
                double previousPosition = particle.getPosition();
                // next position
                particle.setPosition(particle.getPosition()*2 - particle.getPreviousPosition() + (deltaT * deltaT * - Utils.K * particle.getPosition()) / Utils.MASS);
                particle.setPreviousPosition(previousPosition);
                bw.write(currentTimeFrame + "," + particle.getPosition());
                bw.newLine();
            }
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

    public static void main(String[] args) {
        //Leer de un JSON parametros
        String FILE_PATH = "Simulation/Input/input.json";
        SimulationConfig simulationConfig = readConfig(FILE_PATH);

        System.out.println("Starting simulation with " + simulationConfig.getAlgorithm());
        SimulationFactory simulationFactory = new SimulationFactory(simulationConfig);
        System.out.println("Done!");
    }
}
