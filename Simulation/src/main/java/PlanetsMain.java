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
        int dw = (int) (simulationConfig.getDeltaW()/simulationConfig.getDeltaT());

        double[][] earthDistance = new double[(int) 367][4];
        Utils.readCSV("Simulation/Input/earth.csv", earthDistance);
        double[][] martDistance = new double[(int) 367][4];
        Utils.readCSV("Simulation/Input/mars.csv", martDistance);

        oneSimulation(earthDistance[180], martDistance[180], dt, totalTime, alpha, dw);
        //multiSimulation(earthDistance, martDistance, dt, totalTime, alpha);
    }

    public static double[] calculateShipPosition(double[] earthDistance, double alpha){
        double vTierraTangencial = Math.sqrt(Math.pow(earthDistance[2], 2) + Math.pow(earthDistance[3], 2));

        double dTierra = Math.sqrt(Math.pow(earthDistance[0], 2) + Math.pow(earthDistance[1], 2));
        double posX = (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * earthDistance[0] / dTierra;
        double posY = (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * earthDistance[1] / dTierra;

        double vTangencialNave = Utils.INTIAL_DELTA_V + Utils.INITIAL_NAVE_V + vTierraTangencial;
        double velX = -vTangencialNave * earthDistance[1] / dTierra;
        double velY = vTangencialNave * earthDistance[0] / dTierra;

        return new double[]{posX, posY, velX, velY};
    }

    public static void multiSimulation(double[][] tierra, double[][] mars, double dt, double totalTime, double alpha){
        try {
            String OutputPath = "Simulation/Output/MultiPlanetsOutput.csv";
            FileWriter fw = new FileWriter(OutputPath);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("timeFrame,spX,spY,svX,svY,mpX,mpY,epX,epY\n");

            int dw = (int) (totalTime/dt);
            for(int i =0 ; i<10; i++){
                double[] nave = calculateShipPosition(tierra[i], alpha);
                simulatePlanets(nave, mars[i],  tierra[i],  dt, totalTime, bw, dw);
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void oneSimulation(double[] tierra, double[] mars, double dt, double totalTime, double alpha, int deltaWrite){
        try {
            String OutputPath = "Simulation/Output/PlanetsOutput.csv";
            FileWriter fw = new FileWriter(OutputPath);
            BufferedWriter bw = new BufferedWriter(fw);

            double[] nave = calculateShipPosition(tierra, alpha);

            bw.write("timeFrame,spX,spY,svX,svY,mpX,mpY,epX,epY\n");
            bw.write("0," + nave[0] + "," + nave[1] + "," + nave[2] + "," + nave[3] + "," + mars[0] + "," + mars[1] + "," + tierra[0] + "," + tierra[1] + "\n");

            simulatePlanets(nave, mars,  tierra,  dt, totalTime, bw, deltaWrite);

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void simulatePlanets(double[]nave, double[] marte, double[] tierra, double deltaTime, double totalTime, BufferedWriter bw, int deltaWrite) throws IOException {
        double[] tierraPrev = tierra;
        double[] martePrev = marte;
        double[] navePrev = nave;

        BiFunction<Double, Double, Double> TierraXFuction = (positionX, positionY) -> Utils.G * (
                Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                Utils.MARS_MASS * (martePrev[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(martePrev[0] - positionX, 2) + Math.pow(martePrev[1] - positionY, 2)), 3)) +
                Utils.SHIP_MASS * (navePrev[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(navePrev[0] - positionX, 2) + Math.pow(navePrev[1] - positionY, 2)), 3))
        );
        BiFunction<Double, Double, Double> TierraYFuction = (positionX, positionY) -> Utils.G * (
                Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                Utils.MARS_MASS * (martePrev[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(martePrev[0] - positionX, 2) + Math.pow(martePrev[1] - positionY, 2)), 3)) +
                Utils.SHIP_MASS * (navePrev[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(navePrev[0] - positionX, 2) + Math.pow(navePrev[1] - positionY, 2)), 3))
        );
        BiFunction<Double, Double, Double> MarteXFuction = (positionX, positionY) -> Utils.G * (
                Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                Utils.SHIP_MASS * (navePrev[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(navePrev[0] - positionX, 2) + Math.pow(navePrev[1] - positionY, 2)), 3)) +
                Utils.EARTH_MASS * (tierraPrev[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraPrev[0] - positionX, 2) + Math.pow(tierraPrev[1] - positionY, 2)), 3))
        );
        BiFunction<Double, Double, Double> MarteYFuction = (positionX, positionY) -> Utils.G * (
                Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                Utils.SHIP_MASS * (navePrev[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(navePrev[0] - positionX, 2) + Math.pow(navePrev[1] - positionY, 2)), 3)) +
                Utils.EARTH_MASS * (tierraPrev[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraPrev[0] - positionX, 2) + Math.pow(tierraPrev[1] - positionY, 2)), 3))
        );
        BiFunction<Double, Double, Double> NaveXFuction = (positionX, positionY) -> Utils.G * (
                Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                Utils.MARS_MASS * (martePrev[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(martePrev[0] - positionX, 2) + Math.pow(martePrev[1] - positionY, 2)), 3)) +
                Utils.EARTH_MASS * (tierraPrev[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraPrev[0] - positionX, 2) + Math.pow(tierraPrev[1] - positionY, 2)), 3))
        );
        BiFunction<Double, Double, Double> NaveYFuction = (positionX, positionY) -> Utils.G * (
                Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                Utils.MARS_MASS * (martePrev[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(martePrev[0] - positionX, 2) + Math.pow(martePrev[1] - positionY, 2)), 3)) +
                Utils.EARTH_MASS * (tierraPrev[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraPrev[0] - positionX, 2) + Math.pow(tierraPrev[1] - positionY, 2)), 3))
        );

        double NavePrevAccelerationX = NaveXFuction.apply(nave[0], nave[1]);
        double NavePrevAccelerationY = NaveYFuction.apply(nave[0], nave[1]);
        double MartePrevAccelerationX = MarteXFuction.apply(marte[0], marte[1]);
        double MartePrevAccelerationY = MarteYFuction.apply(marte[0], marte[1]);
        double TierraPrevAccelerationX = TierraXFuction.apply(tierra[0], tierra[1]);
        double TierraPrevAccelerationY = TierraYFuction.apply(tierra[0], tierra[1]);

        int counterWrite = 0;
        for (double actualTime = 0; actualTime < totalTime || Utils.MARS_RADIUS > Math.sqrt(Math.pow(nave[0]-marte[0], 2) + Math.pow(nave[1]-marte[1], 2) ); actualTime += deltaTime, counterWrite++) {
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
                            Utils.MARS_MASS * (marteAux[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(marteAux[0] - positionX, 2) + Math.pow(marteAux[1] - positionY, 2)), 3)) +
                            Utils.EARTH_MASS * (tierraAux[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraAux[0] - positionX, 2) + Math.pow(tierraAux[1] - positionY, 2)), 3))
            );
            MarteXFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                            Utils.SHIP_MASS * (naveAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(naveAux[0] - positionX, 2) + Math.pow(naveAux[1] - positionY, 2)), 3)) +
                            Utils.EARTH_MASS * (tierraAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraAux[0] - positionX, 2) + Math.pow(tierraAux[1] - positionY, 2)), 3))
            );
            MarteYFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                            Utils.SHIP_MASS * (naveAux[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(naveAux[0] - positionX, 2) + Math.pow(naveAux[1] - positionY, 2)), 3)) +
                            Utils.EARTH_MASS * (tierraAux[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(tierraAux[0] - positionX, 2) + Math.pow(tierraAux[1] - positionY, 2)), 3))
            );
            TierraXFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_X - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                            Utils.MARS_MASS * (marteAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(marteAux[0] - positionX, 2) + Math.pow(marteAux[1] - positionY, 2)), 3)) +
                            Utils.SHIP_MASS * (naveAux[0] - positionX) / Math.abs(Math.pow(Math.sqrt(Math.pow(naveAux[0] - positionX, 2) + Math.pow(naveAux[1] - positionY, 2)), 3))
            );
            TierraYFuction = (positionX, positionY) -> Utils.G * (
                    Utils.SUN_MASS * (Utils.SUN_POSITION_Y - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(Utils.SUN_POSITION_X - positionX, 2) + Math.pow(Utils.SUN_POSITION_Y - positionY, 2)), 3)) +
                            Utils.MARS_MASS * (marteAux[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(marteAux[0] - positionX, 2) + Math.pow(marteAux[1] - positionY, 2)), 3)) +
                            Utils.SHIP_MASS * (naveAux[1] - positionY) / Math.abs(Math.pow(Math.sqrt(Math.pow(naveAux[0] - positionX, 2) + Math.pow(naveAux[1] - positionY, 2)), 3))
            );

            marte = Methods.TraditionalBeemanMethod(MartePrevAccelerationX, MartePrevAccelerationY, marte[0], marte[1], marte[2], marte[3], deltaTime, MarteXFuction, MarteYFuction);
            nave = Methods.TraditionalBeemanMethod(NavePrevAccelerationX, NavePrevAccelerationY, nave[0], nave[1], nave[2], nave[3], deltaTime, NaveXFuction, NaveYFuction);
            tierra = Methods.TraditionalBeemanMethod(TierraPrevAccelerationX, TierraPrevAccelerationY, tierra[0], tierra[1], tierra[2], tierra[3], deltaTime, TierraXFuction, TierraYFuction);

            MartePrevAccelerationX = MarteXFuction.apply(marteAux[0], marteAux[1]);
            MartePrevAccelerationY = MarteYFuction.apply(marteAux[0], marteAux[1]);
            NavePrevAccelerationX = NaveXFuction.apply(naveAux[0], naveAux[1]);
            NavePrevAccelerationY = NaveYFuction.apply(naveAux[0], naveAux[1]);
            TierraPrevAccelerationX = TierraXFuction.apply(tierraAux[0], tierraAux[1]);
            TierraPrevAccelerationY = TierraYFuction.apply(tierraAux[0], tierraAux[1]);

            if(counterWrite % deltaWrite == 0){
                bw.write(deltaTime + "," + nave[0] + "," + nave[1] + "," + nave[2] + "," + nave[3] + "," + marte[0] + "," + marte[1] + "," + tierra[0] + "," + tierra[1] + "\n");
            }
        }
        if(Utils.MARS_RADIUS > Math.sqrt(Math.pow(nave[0]-marte[0], 2) + Math.pow(nave[1]-marte[1], 2))){
            System.out.println("Choco");
        }
    }

}
