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

        //initialVariation(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw);

        //deltaTimeVariation(earthDistance, martDistance, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, initialTime);

        //velocityVariation(earthDistance, martDistance, dt, totalTime, alpha, dw, initialTime);

        hoursVariation(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, 177);
    }

    public static void hoursVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = true;

        for(int hora=0 ; hora <= 48 ; hora=hora+4){
            System.out.println(hora);
            String OutputPath = "Simulation/Output/PlanetOutput_" + hora + ".csv";
            double[][] cambiados = SimulationFactory.changeHours(tierra[initialTime], mars[initialTime], hora*60*60, dt);
            SimulationFactory.oneSimulation(cambiados[0], cambiados[1], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
        }
    }

    public static void velocityVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double alpha, int dw, int initialTime){
        boolean complete = false;

        for(double vel=0 ; vel < Utils.INITIAL_NAVE_V * 2 ; vel+= Utils.INITIAL_NAVE_V/10){
            System.out.println(vel);
            String OutputPath = "Simulation/Output/PlanetOutput_" + vel + ".csv";
            SimulationFactory.oneSimulation(tierra[initialTime], mars[initialTime], dt, totalTime, vel, alpha, dw, complete, OutputPath);
        }
    }

    public static void deltaTimeVariation(double[][] tierra, double[][] mars, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = false;

        for(double dt=0.1 ; dt<1; dt= dt*10){
            System.out.println(dt);
            int new_dw = (int) (dw*24*60*60/dt);
            String OutputPath = "Simulation/Output/PlanetOutput_" + dt + ".csv";
            SimulationFactory.oneSimulation(tierra[initialTime], mars[initialTime], dt, totalTime, velocity, alpha, new_dw, complete, OutputPath);
        }
    }

    public static void initialVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw){
        boolean complete = false;

        for(int i =175 ; i<185; i++){
            System.out.println(i);
            String OutputPath = "Simulation/Output/PlanetOutput_" + i + ".csv";
            SimulationFactory.oneSimulation(tierra[i], mars[i], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
        }
    }

    public static void singleSimulation(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = false;
        String OutputPath = "Simulation/Output/PlanetOutput.csv";
        SimulationFactory.oneSimulation(tierra[initialTime], mars[initialTime], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
    }
}
