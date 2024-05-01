public class Utils {
    public static double MASS = 70;
    public static double K = Math.pow(10, 4);
    public static double GAMMA = 100;
    public static double INITIAL_POSITION = 1;
    public static double AMPLITUDE = 1; // Check
    public static double INITIAL_SPEED = - AMPLITUDE * GAMMA / (2 * MASS);
    public static double[] ALPHA_VELOCITY = {3.0 / 20, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60};
    public static double[] ALPHA_POSITION = {3.0 / 16, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60};
}
