import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

    // TODAS LAS CONSTANTES EN SISTEMA INTERNACIONAL (metro, kilogramo, segundo)
    public static double MASS_METHOD = 70;
    public static double K = Math.pow(10, 4);
    public static double GAMMA = 100;
    public static double INITIAL_POSITION_METHOD = 1;
    public static double AMPLITUDE = 1; // Check
    public static double INITIAL_SPEED_METHOD = - AMPLITUDE * GAMMA / (2 * MASS_METHOD);
    public static double[] ALPHA_VELOCITY = {3.0 / 20, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60};
    public static double[] ALPHA_POSITION = {3.0 / 16, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60};
    public static double G = 6.693 * Math.pow(10, -11);
    public static double STATION_DISTANCE = 1500 * Math.pow(10, 3);
    public static double INTIAL_DELTA_V = 7.12 * Math.pow(10, 3);
    public static double INITIAL_NAVE_V = 8 * Math.pow(10, 3);
    public static double SHIP_MASS = 2 * Math.pow(10, 5);
    public static double SUN_POSITION_X = 0;
    public static double SUN_POSITION_Y = 0;
    public static double SUN_MASS = 1.9891 * Math.pow(10, 30);
    public static double MARS_MASS = 6.39 * Math.pow(10, 23);
    public static double MARS_RADIUS = 3389.5 * Math.pow(10, 3);
    public static double EARTH_MASS = 5.972 * Math.pow(10, 24);
    public static double EARTH_RADIUS = 6378 * Math.pow(10, 3);

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
        String statusFile = "Simulation/Output/Status.json";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totalTime", simulationConfig.getTotalTime());
            jsonObject.put("deltaT", simulationConfig.getDeltaT());
            jsonObject.put("initialTime", simulationConfig.getInitialTime());
            jsonObject.put("deltaW", simulationConfig.getDeltaW());
            jsonObject.put("alpha", simulationConfig.getAlpha());

            FileWriter writer_status = new FileWriter(statusFile);
            writer_status.write(jsonObject.toString());
            writer_status.close();

        } catch(IOException e){
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public static void readCSV(String path, double[][] data){
        String line = "";
        String CSV_SEPARATOR = ",";
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // Read the CSV file line by line
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split the line by the separator to get individual values
                String[] csvLine = line.split(CSV_SEPARATOR);
                // Process the data here
                for(int j = 0; j < csvLine.length; j++){
                    data[i][j] = Float.parseFloat(csvLine[j]) * Math.pow(10, 3);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
