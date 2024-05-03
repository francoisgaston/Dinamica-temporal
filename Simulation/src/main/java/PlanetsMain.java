import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.BiFunction;

public class PlanetsMain {

    public static void main(String[] args) {
        String inputPath = "Simulation/Input/planetInput.json";
        SimulationConfig simulationConfig = Utils.readConfig(inputPath);
        Utils.writeStatus(simulationConfig);

        //dt y totalTime en días de input y aca lo paso a segundos
        double dt = simulationConfig.getDeltaT()*24*60*60;
        double totalTime = simulationConfig.getTotalTime()*24*60*60;
        double alpha = simulationConfig.getAlpha();

        double[][] earthDistance = new double[(int) simulationConfig.getTotalTime()+2][4];
        Utils.readCSV("Simulation/Input/earth.csv", earthDistance);
        double[][] martDistance = new double[(int) simulationConfig.getTotalTime()+2][4];
        Utils.readCSV("Simulation/Input/mars.csv", martDistance);

        //oneSimulation(earthDistance[0], martDistance[0], dt, totalTime, alpha);

        multiSimulation(earthDistance, martDistance, dt, totalTime, alpha);
    }

    public static double[] calculateShipPosition(double[] earthDistance, double alpha){
        double vTierraTangencial = Math.sqrt(Math.pow(earthDistance[2], 2) + Math.pow(earthDistance[3], 2));

        double dTierra = Math.sqrt(Math.pow(earthDistance[0], 2) + Math.pow(earthDistance[1], 2));
        double posX = (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * earthDistance[0] / dTierra;
        double posY = (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * earthDistance[1] / dTierra;

        double vTangencial = Utils.INTIAL_DELTA_V + (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * vTierraTangencial / (dTierra + Utils.EARTH_RADIUS);
        double velX = -vTangencial * earthDistance[1] / dTierra;
        double velY = vTangencial * earthDistance[0] / dTierra;

        return new double[]{posX, posY, velX, velY};
    }

    public static void multiSimulation(double[][] tierra, double[][] mars, double dt, double totalTime, double alpha){
        try {
            String OutputPath = "Simulation/Output/MultiPlanetsOutput.csv";
            FileWriter fw = new FileWriter(OutputPath);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("timeFrame,spX,spY,svX,svY,mpX,mpY,epX,epY\n");

            for(int i =0 ; i<tierra.length; i++){
                String bestPosition = oneSimulation(mars[i],  tierra[i],  dt, totalTime, alpha);
                bw.write(bestPosition);
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String oneSimulation(double[] tierra, double[] mars, double dt, double totalTime, double alpha){
        double[] nave = calculateShipPosition(tierra, alpha);

        try {
            String OutputPath = "Simulation/Output/PlanetsOutput.csv";
            FileWriter fw = new FileWriter(OutputPath);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("timeFrame,spX,spY,svX,svY,mpX,mpY,epX,epY\n");
            bw.write("0," + nave[0] + "," + nave[1] + "," + nave[2] + "," + nave[3] + "," + mars[0] + "," + mars[1] + "," + tierra[0] + "," + tierra[1] + "\n");

            String bestPosition = simulatePlanets(nave, mars,  tierra,  dt, totalTime, bw);

            bw.close();
            return bestPosition;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String simulatePlanets(double[]nave, double[] tierra, double[] marte, double deltaTime, double totalTime, BufferedWriter bw) throws IOException {
        BiFunction<Double, Double, Double> TierraXFuction;
        BiFunction<Double, Double, Double> TierraYFuction;
        BiFunction<Double, Double, Double> MarteXFuction;
        BiFunction<Double, Double, Double> MarteYFuction;
        BiFunction<Double, Double, Double> NaveXFuction;
        BiFunction<Double, Double, Double> NaveYFuction;

        double[] tierraPrev = tierra;
        double[] martePrev = marte;
        double[] navePrev = nave;

        for (double actualTime = 0; actualTime < totalTime; actualTime += deltaTime){
            double[] tierraAux = tierra;
            double[] marteAux = marte;
            double[] naveAux = nave;

            NaveXFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                    Utils.MARS_MASS * (marteAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(marteAux[0] - positionX, 2) + Math.pow(marteAux[1] - positionY, 2)), 3)) +
                    Utils.EARTH_MASS * (tierraAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraAux[0] - positionX, 2) + Math.pow(tierraAux[1] - positionY, 2)), 3))
            );
            NaveYFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                    Utils.MARS_MASS * (marteAux[1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(marteAux[0] - positionX, 2) + Math.pow(marteAux[1] - positionY, 2)), 3)) +
                    Utils.EARTH_MASS * (tierraAux[1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraAux[0] - positionX, 2) + Math.pow(tierraAux[1] - positionY, 2)), 3))
            );
            nave = Methods.TraditionalBeemanMethod(navePrev[0], navePrev[1], nave[0], nave[1], nave[2], nave[3], deltaTime, NaveXFuction, NaveYFuction);

            MarteXFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                    Utils.SHIP_MASS * (naveAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(naveAux[0] - positionX, 2) + Math.pow(naveAux[1] - positionY, 2)), 3)) +
                    Utils.EARTH_MASS * (tierraAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraAux[0] - positionX, 2) + Math.pow(tierraAux[1] - positionY, 2)), 3))
            );
            MarteYFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                    Utils.SHIP_MASS * (naveAux[1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(naveAux[0] - positionX, 2) + Math.pow(naveAux[1] - positionY, 2)), 3)) +
                    Utils.EARTH_MASS * (tierraAux[1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraAux[0] - positionX, 2) + Math.pow(tierraAux[1] - positionY, 2)), 3))
            );
            marte = Methods.TraditionalBeemanMethod(martePrev[0], martePrev[1], marte[0], marte[1], marte[2], marte[3], deltaTime, MarteXFuction, MarteYFuction);

            TierraXFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                    Utils.MARS_MASS * (marteAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(marteAux[0] - positionX, 2) + Math.pow(marteAux[1] - positionY, 2)), 3)) +
                    Utils.SHIP_MASS * (naveAux[1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(naveAux[0] - positionX, 2) + Math.pow(naveAux[1] - positionY, 2)), 3))
            );
            TierraYFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                    Utils.MARS_MASS * (marteAux[1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(marteAux[0] - positionX, 2) + Math.pow(marteAux[1] - positionY, 2)), 3)) +
                    Utils.SHIP_MASS * (naveAux[1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(naveAux[0] - positionX, 2) + Math.pow(naveAux[1] - positionY, 2)), 3))
            );
            tierra = Methods.TraditionalBeemanMethod(tierraPrev[0], tierraPrev[1], tierra[0], tierra[1], tierra[2], tierra[3], deltaTime, TierraXFuction, TierraYFuction);

            tierraPrev = tierraAux;
            martePrev = marteAux;
            navePrev = naveAux;

            bw.write(deltaTime + "," + nave[0] + "," + nave[1] + "," + nave[2] + "," + nave[3] + "," + marte[0] + "," + marte[1] + "," + tierra[0] + "," + tierra[1] + "\n");
        }

        return String.format(deltaTime + "," + nave[0] + "," + nave[1] + "," + nave[2] + "," + nave[3] + "," + marte[0] + "," + marte[1] + "," + tierra[0] + "," + tierra[1] + "\n");
    }

    /*
    public static void simulatePlanets2(double posX, double posY, double velX, double velY, double alpha, double marsX, double marsY, double earthX, double earthY, double deltaTime, double totalTime, BufferedWriter bw) throws IOException {
        BiFunction<Double, Double, Double> acelerationXFuction;
        BiFunction<Double, Double, Double> acelerationYFuction;
        double[] r = {posX, posY, velX, velY};

        double posXprev = posX;
        double posYprev = posY;
        double auxPosYprev, auxPosXprev;

        int index = 0;
        for (double actualTime = 0; actualTime < totalTime; actualTime+= deltaTime){
            int finalIndex = index;
            auxPosXprev = r[0];
            auxPosYprev = r[1];

            acelerationXFuction = (positionX, positionY) -> Utils.G * (
                        Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                        Utils.MARS_MASS * (martDistance[finalIndex][0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(martDistance[finalIndex][0] - positionX, 2) + Math.pow(martDistance[finalIndex][1] - positionY, 2)), 3)) +
                        Utils.EARTH_MASS * (earthDistance[finalIndex][0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(earthDistance[finalIndex][0] - positionX, 2) + Math.pow(earthDistance[finalIndex][1] - positionY, 2)), 3))
                        );

            acelerationYFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                            Utils.MARS_MASS * (martDistance[finalIndex][1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(martDistance[finalIndex][0] - positionX, 2) + Math.pow(martDistance[finalIndex][1] - positionY, 2)), 3)) +
                            Utils.EARTH_MASS * (earthDistance[finalIndex][1] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(earthDistance[finalIndex][0] - positionX, 2) + Math.pow(earthDistance[finalIndex][1] - positionY, 2)), 3))
            );

            r = Methods.TraditionalBeemanMethod(posXprev, posYprev, r[0], r[1], r[2], r[3], deltaTime, acelerationXFuction, acelerationYFuction);

            posXprev = auxPosXprev;
            posYprev = auxPosYprev;
            index++;

            bw.write(deltaTime*index + "," + r[0] + "," + r[1] + "," + r[2] + "," + r[3] + "," + martDistance[index][0] + "," + martDistance[index][1] + "," + earthDistance[index][0] + "," + earthDistance[index][1] + "\n");
        }
    }

     */
}
