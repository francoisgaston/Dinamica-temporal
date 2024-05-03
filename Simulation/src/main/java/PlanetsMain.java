import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import javax.swing.event.MouseInputListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.BiFunction;

public class PlanetsMain {

    public static void main(String[] args) {
        String inputPath = "Simulation/Input/planetInput.json";
        SimulationConfig simulationConfig = Utils.readConfig(inputPath);
        Utils.writeStatus(simulationConfig);

        double dt = simulationConfig.getDeltaT();
        double totalTime = simulationConfig.getTotalTime();
        double alpha = simulationConfig.getAlpha();

        double[][] earthDistance = new double[367][4];
        Utils.readCSV("Simulation/Input/earth.csv", earthDistance);
        double[][] martDistance = new double[367][2];
        Utils.readCSV("Simulation/Input/mars.csv", martDistance);
        double vTierraTangencial = Math.sqrt(Math.pow(earthDistance[0][2], 2) + Math.pow(earthDistance[0][3], 2));

        double dTierra = Math.sqrt(Math.pow(earthDistance[0][0], 2) + Math.pow(earthDistance[0][1], 2));
        double posX = (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * earthDistance[0][0] / dTierra;
        double posY = (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * earthDistance[0][1] / dTierra;

        double vTangencial = Utils.INTIAL_DELTA_V + (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * vTierraTangencial / (dTierra + Utils.EARTH_RADIUS);
        double velX = -vTangencial * earthDistance[0][1] / dTierra;
        double velY = vTangencial * earthDistance[0][0] / dTierra;

        try {
            String OutputPath = "Simulation/Output/PlanetsOutput.csv";
            FileWriter fw = new FileWriter(OutputPath);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("timeFrame,spX,spY,svX,svY,mpX,mpY,epX,epY\n");
            bw.write("0," + posX + "," + posY + "," + velX + "," + velY + "," + martDistance[0][0] + "," + martDistance[0][1] + "," + earthDistance[0][0] + "," + earthDistance[0][1] + "\n");

            simulatePlanets(posX, posY, velX, velY, alpha, dt, totalTime*dt, martDistance, earthDistance, bw);

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void simulatePlanets(double posX, double posY, double velX, double velY, double alpha, double deltaTime, double totalTime, double[][] martDistance, double[][] earthDistance, BufferedWriter bw) throws IOException {
        BiFunction<Double, Double, Double> acelerationXFuction;
        BiFunction<Double, Double, Double> acelerationYFuction;
        double[] rx = {posX, velX};
        double[] ry = {posY, velY};

        int index = 0;
        for (double actualTime = 0; actualTime < totalTime; actualTime+= deltaTime){
            int finalIndex = index;

            acelerationXFuction = (positionX, positionY) -> Utils.G * (
                        Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                        Utils.MARS_MASS * (martDistance[finalIndex][0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(martDistance[finalIndex][0] - positionX, 2) + Math.pow(martDistance[finalIndex][1] - positionY, 2)), 3)) +
                        Utils.EARTH_MASS * (earthDistance[finalIndex][0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(earthDistance[finalIndex][0] - positionX, 2) + Math.pow(earthDistance[finalIndex][1] - positionY, 2)), 3))
                        );
            rx = Methods.GearMethod(rx[0], rx[1], deltaTime, acelerationXFuction, Utils.ALPHA_POSITION);

            acelerationYFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                            Utils.MARS_MASS * (martDistance[finalIndex][1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(martDistance[finalIndex][0] - positionX, 2) + Math.pow(martDistance[finalIndex][1] - positionY, 2)), 3)) +
                            Utils.EARTH_MASS * (earthDistance[finalIndex][1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(earthDistance[finalIndex][0] - positionX, 2) + Math.pow(earthDistance[finalIndex][1] - positionY, 2)), 3))
            );
            ry = Methods.GearMethod(ry[0], ry[1], deltaTime, acelerationYFuction, Utils.ALPHA_POSITION);
            index++;

            bw.write(deltaTime*index + "," + rx[0] + "," + ry[0] + "," + rx[1] + "," + ry[1] + "," + martDistance[index][0] + "," + martDistance[index][1] + "," + earthDistance[index][0] + "," + earthDistance[index][1] + "\n");
        }
    }
}
