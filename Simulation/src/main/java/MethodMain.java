import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.BiFunction;

public class MethodMain {

    public static void main(String[] args) {
        String inputPath = "Simulation/Input/methodInput.json";
        SimulationConfig simulationConfig = Utils.readConfig(inputPath);
        Utils.writeStatus(simulationConfig);

        dumpedMethods(simulationConfig.getTotalTime(), simulationConfig.getDeltaT());
        VerletDampedOscilations(simulationConfig);
    }

    public static void dumpedMethods(double totalTime, double deltaTime){
        BiFunction<Double, Double, Double> acelerationFuction = (pos, vel) -> (-Utils.K * pos - Utils.GAMMA * vel) / Utils.MASS_METHOD;
        double[] r = {Utils.INITIAL_POSITION_METHOD, Utils.INITIAL_SPEED_METHOD};

        String[] methods = new String[]{"Beeman", "Gear", "Analitica"};
        for(String method : methods){

            System.out.println("Starting simulation with " + method);
            String OutputPath = "Simulation/Output/" + method + "Output_" + Double.toString(deltaTime).substring(2) + ".csv";

            try {
                FileWriter fw = new FileWriter(OutputPath);
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write("timeFrame,position,velocity\n");
                bw.write("0,"+ Utils.INITIAL_POSITION_METHOD + "," + Utils.INITIAL_SPEED_METHOD + "\n");

                runMethod(method, totalTime, deltaTime, r, acelerationFuction, bw);

                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Done!");
        }
    }


    // Esto es feo pero es para estar seguro de usar lo mismo en la simulacion de marte y para no usar args variables
    public static void runMethod(String method, double totalTime, double deltaTime, double[] r,  BiFunction<Double, Double, Double> acelerationFuction, BufferedWriter bw) throws IOException {

        switch (method) {
            case "Gear" -> {
                for (double actualTime = 0; actualTime < totalTime; actualTime += deltaTime) {
                    r = Methods.GearMethod(r[0], r[1], deltaTime, acelerationFuction, Utils.ALPHA_VELOCITY);
                    bw.write(actualTime + "," + r[0] + "," + r[1] + "\n");
                }
            }
            case "Beeman" -> {
                for (double actualTime = 0, prevAceleration = acelerationFuction.apply(r[0], r[1]), actualAceleration; actualTime < totalTime; actualTime += deltaTime) {
                    actualAceleration = acelerationFuction.apply(r[0], r[1]);
                    r = Methods.BeemanMethod(r[0], r[1], deltaTime, acelerationFuction, prevAceleration);
                    prevAceleration = actualAceleration;
                    bw.write(actualTime + "," + r[0] + "," + r[1] + "\n");
                }
            }
            case "Analitica" -> {
                for (double actualTime = 0; actualTime < totalTime; actualTime += deltaTime) {
                    r = Methods.PerfectMethod(actualTime);
                    bw.write(actualTime + "," + r[0] + "," + r[1] + "\n");
                }
            }
            default -> throw new IllegalArgumentException("Método no reconocido: " + method);
        }
    }

    public static void VerletDampedOscilations(SimulationConfig simulationConfig) {
        Particle particle = new Particle(Utils.INITIAL_POSITION_METHOD, Utils.MASS_METHOD, Utils.INITIAL_SPEED_METHOD);
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

}
