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
        double dt = simulationConfig.getDeltaT();
        double totalTime = simulationConfig.getTotalTime()*24*60*60;
        int initialTime = simulationConfig.getInitialTime();
        double alpha = simulationConfig.getAlpha();
        int dw = (int) (simulationConfig.getDeltaW()*24*60*60/simulationConfig.getDeltaT());

        double[][] earthDistance = new double[(int) 367][4];
        Utils.readCSV("Simulation/Input/earth.csv", earthDistance);
        double[][] martDistance = new double[(int) 367][4];
        Utils.readCSV("Simulation/Input/mars.csv", martDistance);

        //singleSimulation(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, initialTime);

        initialVariation(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw);

        //deltaTimeVariation(earthDistance, martDistance, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, initialTime);

        //velocityVariation(earthDistance, martDistance, dt, totalTime, alpha, dw, initialTime);

        hoursVariation(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, initialTime);
    }

    public static void hoursVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = true;

        for(int hora=0 ; hora <= 24 ; hora++){
            String OutputPath = "Simulation/Output/PlanetOutput_" + hora + ".csv";
            double[][] cambiados = changeHours(tierra[initialTime], mars[initialTime]);
            oneSimulation(cambiados[0], cambiados[1], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
        }
    }

    public static double[][] changeHours(double[] tierra, double[] mars){
        return new double[][]{tierra, mars};
    }

    public static void velocityVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double alpha, int dw, int initialTime){
        boolean complete = false;

        for(double vel=0 ; vel < Utils.INITIAL_NAVE_V * 2 ; vel+= Utils.INITIAL_NAVE_V/10){
            System.out.println(vel);
            String OutputPath = "Simulation/Output/PlanetOutput_" + vel + ".csv";
            oneSimulation(tierra[initialTime], mars[initialTime], dt, totalTime, vel, alpha, dw, complete, OutputPath);
        }
    }

    public static void deltaTimeVariation(double[][] tierra, double[][] mars, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = false;

        for(double dt=0.1 ; dt<1; dt= dt*10){
            System.out.println(dt);
            int new_dw = (int) (dw*24*60*60/dt);
            String OutputPath = "Simulation/Output/PlanetOutput_" + dt + ".csv";
            oneSimulation(tierra[initialTime], mars[initialTime], dt, totalTime, velocity, alpha, new_dw, complete, OutputPath);
        }
    }

    public static void initialVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw){
        boolean complete = false;

        for(int i =150 ; i<300; i++){
            System.out.println(i);
            String OutputPath = "Simulation/Output/PlanetOutput_" + i + ".csv";
            oneSimulation(tierra[i], mars[i], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
        }
    }

    public static void singleSimulation(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = false;
        String OutputPath = "Simulation/Output/PlanetOutput.csv";
        oneSimulation(tierra[initialTime], mars[initialTime], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
    }










    public static void oneSimulation(double[] tierra, double[] mars, double dt, double totalTime, double velocity, double alpha, int deltaWrite, boolean complete, String OutputPath){
        try {
            FileWriter fw = new FileWriter(OutputPath);
            BufferedWriter bw = new BufferedWriter(fw);

            double[] nave = calculateShipPosition(tierra, velocity, alpha);

            bw.write("timeFrame,spX,spY,svX,svY,mpX,mpY,mvX,mvY,epX,epY,evX,evY\n");
            bw.write("0" +
                    "," + nave[0] + "," + nave[1] + "," + nave[2] + "," + nave[3] +
                    "," + mars[0] + "," + mars[1] + "," + mars[2] + "," + mars[3] +
                    "," + tierra[0] + "," + tierra[1] + "," + tierra[2] + "," + tierra[3] + "\n");

            simulatePlanets(nave, mars,  tierra,  dt, totalTime, bw, deltaWrite, complete);

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void simulatePlanets(double[]nave, double[] marte, double[] tierra, double deltaTime, double totalTime, BufferedWriter bw, int deltaWrite, boolean complete) throws IOException {
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
        for (double actualTime = 0; actualTime < totalTime; actualTime += deltaTime, counterWrite++) {
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

            if(!complete && Utils.MARS_RADIUS * 1000 > Math.sqrt(Math.pow(nave[0]-marte[0], 2) + Math.pow(nave[1]-marte[1], 2))){
                bw.write(actualTime +
                        "," + nave[0] + "," + nave[1] + "," + nave[2] + "," + nave[3] +
                        "," + marte[0] + "," + marte[1] + "," + marte[2] + "," + marte[3] +
                        "," + tierra[0] + "," + tierra[1] + "," + tierra[2] + "," + tierra[3] + "\n");
                System.out.println("Choco");
                return;
            }

            if(counterWrite == deltaWrite){
                counterWrite = 0;
                bw.write(actualTime +
                        "," + nave[0] + "," + nave[1] + "," + nave[2] + "," + nave[3] +
                        "," + marte[0] + "," + marte[1] + "," + marte[2] + "," + marte[3] +
                        "," + tierra[0] + "," + tierra[1] + "," + tierra[2] + "," + tierra[3] + "\n");
            }
        }
    }

    public static double[] calculateShipPosition(double[] earthDistance, double velocity, double alpha){
        double vTierraTangencial = Math.sqrt(Math.pow(earthDistance[2], 2) + Math.pow(earthDistance[3], 2));

        double dTierra = Math.sqrt(Math.pow(earthDistance[0], 2) + Math.pow(earthDistance[1], 2));
        double posX = (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * earthDistance[0] / dTierra;
        double posY = (dTierra + Utils.EARTH_RADIUS + Utils.STATION_DISTANCE) * earthDistance[1] / dTierra;

        double vTangencialNave = Utils.INTIAL_DELTA_V + velocity + vTierraTangencial;
        double velX = -vTangencialNave * earthDistance[1] / dTierra;
        double velY = vTangencialNave * earthDistance[0] / dTierra;

        return new double[]{posX, posY, velX, velY};
    }
}
