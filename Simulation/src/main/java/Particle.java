public class Particle {
    private double position;
    private double previousPosition;
    private double speed;
    private final double mass;

    public Particle(double position, double mass, double speed) {
        this.mass = mass;
        this.position = position;
        this.previousPosition = position;
        this.speed = speed;
    }

    public void setPosition(double position){

    }

    public double getPosition() {
        return position;
    }

    public double getPreviousPosition() {
        return previousPosition;
    }

    public double getSpeed() {
        return speed;
    }

    public double getMass() {
        return mass;
    }
}
