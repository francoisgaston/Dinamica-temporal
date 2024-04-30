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

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setPreviousPosition(double previousPosition) {
        this.previousPosition = previousPosition;
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
