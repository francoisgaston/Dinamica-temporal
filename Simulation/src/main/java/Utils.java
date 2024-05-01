import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    public static double MASS = 70;
    public static double K = Math.pow(10, 4);
    public static double GAMMA = 100;
    public static double INITIAL_POSITION = 1;
    public static double AMPLITUDE = 1; // Check
    public static double INITIAL_SPEED = - AMPLITUDE * GAMMA / (2 * MASS);
    public static double[] ALPHA_VELOCITY = {3.0 / 20, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60};
    public static double[] ALPHA_POSITION = {3.0 / 16, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60};

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

    public static void writeStatus(SimulationConfig simulationConfig){
        String statusFile = "Simulation/Output/Status_" + Double.toString(simulationConfig.getDeltaT()).substring(2) + ".json";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("algorithm", simulationConfig.getAlgorithm());
            jsonObject.put("totalTime", simulationConfig.getTotalTime());
            jsonObject.put("deltaT", simulationConfig.getDeltaT());

            FileWriter writer_status = new FileWriter(statusFile);
            writer_status.write(jsonObject.toString());
            writer_status.close();

        } catch(IOException e){
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

}
