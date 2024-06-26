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

        //singleSimulation(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, 178);

        //initialVariation(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw);

        //deltaTimeVariation(earthDistance, martDistance, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, initialTime);

        //velocityVariation(earthDistance, martDistance, dt, totalTime, alpha, dw, 178);

        //hoursVariation(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, 177);

        singleSimulationSecond(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, 996);

        //initialVariationSecond(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw);

        //hoursVariationSecond(earthDistance, martDistance, dt, totalTime, Utils.INITIAL_NAVE_V, alpha, dw, 177);
    }

    public static void hoursVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = true;

        for(int hora=0 ; hora <= 48 ; hora=hora+1){
            //System.out.println(hora);
            String OutputPath = "Simulation/Output/PlanetOutput_" + hora + ".csv";
            double[][] cambiados = SimulationFactory.changeHours(tierra[177], mars[177], hora*60*60, dt);
            SimulationFactory.oneSimulation(cambiados[0], cambiados[1], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
        }
    }

    public static void hoursVariationSecond(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = false;

        for(int hora=0 ; hora <= 48 ; hora=hora+1){
            //System.out.println(hora);
            String OutputPath = "Simulation/Output/PlanetOutput_" + hora + ".csv";
            double[][] cambiados = SimulationFactory.changeHours(tierra[0], mars[0], 995*24*60*60, dt);
            cambiados = SimulationFactory.changeHours(cambiados[0], cambiados[1], hora*60*60, dt);
            SimulationFactory.secondSimulation(cambiados[0], cambiados[1], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
        }
    }

    public static void velocityVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double alpha, int dw, int initialTime){
        boolean complete = false;

        for(double vel=0 ; vel < Utils.INITIAL_NAVE_V * 2.0 ; vel+= Utils.INITIAL_NAVE_V/20){
            System.out.println(vel);
            String OutputPath = "Simulation/Output/PlanetOutput_" + vel + ".csv";
            SimulationFactory.oneSimulation(tierra[initialTime], mars[initialTime], dt, totalTime, vel, alpha, dw, complete, OutputPath);
        }
    }

    public static void deltaTimeVariation(double[][] tierra, double[][] mars, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = true;

        for(double dt=0.1 ; dt>=0.1; dt= dt/10.0){
            System.out.println(dt);
            int new_dw = (int) (dw/dt);
            String OutputPath = "Simulation/Output/PlanetOutput_" + dt + ".csv";
            SimulationFactory.oneSimulation(tierra[initialTime], mars[initialTime], dt, totalTime, velocity, alpha, new_dw, complete, OutputPath);
        }
    }

    // Esto va a tardar 3hs => mejor hacerlo cuando estudio
    public static void initialVariation(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw) {
        boolean complete = false;

        for(int dias=732 ; dias <= 800 ; dias=dias+1){
            System.out.println(dias);
            String OutputPath = "Simulation/Output/PlanetOutput_" + dias + ".csv";
            double[][] cambiados = SimulationFactory.changeHours(tierra[0], mars[0], dias*24*60*60, dt);
            SimulationFactory.oneSimulation(cambiados[0], cambiados[1], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
        }
    }


    public static void initialVariationNasa(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw){
        boolean complete = false;

        for(int i =0 ; i<366; i++){
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

    public static void singleSimulationSecond(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw, int initialTime){
        boolean complete = false;
        String OutputPath = "Simulation/Output/PlanetOutput.csv";
        double[][] cambiados = SimulationFactory.changeHours(tierra[0], mars[0], 996*24*60*60, dt);
        SimulationFactory.secondSimulation(cambiados[0], cambiados[1], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
    }

    public static void initialVariationSecond(double[][] tierra, double[][] mars, double dt, double totalTime, double velocity, double alpha, int dw) {
        boolean complete = false;

        for(int dias=178 ; dias <= 178 ; dias=dias+1){
            System.out.println(dias);
            String OutputPath = "Simulation/Output/PlanetOutput_" + dias + ".csv";
            double[][] cambiados = SimulationFactory.changeHours(tierra[0], mars[0], dias*24*60*60, dt);
            SimulationFactory.secondSimulation(cambiados[0], cambiados[1], dt, totalTime, velocity, alpha, dw, complete, OutputPath);
        }
    }
}
