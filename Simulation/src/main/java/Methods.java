import java.util.function.BiFunction;

public class Methods {

    public static double[] GearMethod(double pos, double vel, double deltaTime, BiFunction<Double, Double, Double> acelerationFuction, double[] alpha){
        double dr2 = acelerationFuction.apply(pos, vel);
        double dr3 = acelerationFuction.apply(vel, dr2);
        double dr4 = acelerationFuction.apply(dr2, dr3);
        double dr5 = acelerationFuction.apply(dr3, dr4);

        double r5p = dr5;
        double r4p = dr4 + dr5 * deltaTime;
        double r3p = dr3 + dr4 * deltaTime + dr5 * Math.pow(deltaTime, 2) / 2;
        double r2p = dr2 + dr3 * deltaTime + dr4 * Math.pow(deltaTime, 2) / 2 + dr5 * Math.pow(deltaTime, 3) / 6;
        double r1p = vel + dr2 * deltaTime + dr3 * Math.pow(deltaTime, 2) / 2 + dr4 * Math.pow(deltaTime, 3) / 6 + dr5 * Math.pow(deltaTime, 4) / 24;
        double r0p = pos + vel * deltaTime + dr2 * Math.pow(deltaTime, 2) / 2 + dr3 * Math.pow(deltaTime, 3) / 6 + dr4 * Math.pow(deltaTime, 4) / 24 + dr5 * Math.pow(deltaTime, 5) / 120;

        double r2c = acelerationFuction.apply(r0p, r1p);
        double DR2 = (r2c - r2p) * Math.pow(deltaTime, 2) / 2;

        double r0 = r0p + alpha[0] * DR2 * 1 / Math.pow(deltaTime, 0);
        double r1 = r1p + alpha[1] * DR2 * 1 / Math.pow(deltaTime, 1);
        double r2 = r2p + alpha[2] * DR2 * 2 / Math.pow(deltaTime, 2);
        double r3 = r3p + alpha[3] * DR2 * 6 / Math.pow(deltaTime, 3);
        double r4 = r4p + alpha[4] * DR2 * 24 / Math.pow(deltaTime, 4);
        double r5 = r5p + alpha[5] * DR2 * 120 / Math.pow(deltaTime, 5);

        return new double[]{r0, r1};
    }

    public static double[] BeemanMethod(double pos, double vel, double deltaTime, BiFunction<Double, Double, Double> acelerationFuction, double prevAceleration){
        double newPos = pos + vel * deltaTime + (2.0/3) *  acelerationFuction.apply(pos, vel) * Math.pow(deltaTime, 2) - (1.0/6) * prevAceleration * Math.pow(deltaTime, 2);
        double newPredictVel = vel + (3.0/2) * acelerationFuction.apply(pos, vel) * deltaTime - (1.0/2) * acelerationFuction.apply(pos, vel) * deltaTime;

        double newAceleration = acelerationFuction.apply(newPos, newPredictVel);
        double newVel = vel + (1.0/3) * newAceleration * deltaTime + (5.0/6) * acelerationFuction.apply(pos, vel) * deltaTime - (1.0/6) * acelerationFuction.apply(pos, vel) * deltaTime;

        return new double[]{newPos, newVel};
    }

    public static double[] PerfectMethod(double actualTime){
        double newPos = Utils.AMPLITUDE * Math.pow(Math.E, -Utils.GAMMA * actualTime / (2 * Utils.MASS_METHOD)) * Math.cos(Math.sqrt((Utils.K / Utils.MASS_METHOD) - (Math.pow(Utils.GAMMA, 2) / (4 * Math.pow(Utils.MASS_METHOD, 2)))) * actualTime);

        return new double[]{newPos, 0};
    }

    public static double[] VerletMethod(double pos, double vel, double previousPos, double deltaTime, BiFunction<Double, Double, Double> acelerationFuction){
        double nextPosition = 2 * pos - previousPos + acelerationFuction.apply(pos, vel) * Math.pow(deltaTime, 2);
        double secondNextPosition = 2 * nextPosition - pos + acelerationFuction.apply(nextPosition, vel) * Math.pow(deltaTime, 2);
        double nextSpeed = (secondNextPosition - pos) / (2 * deltaTime);

        return new double[]{nextPosition, nextSpeed};
    }

    public static double[] TraditionalBeemanMethod(double posXprev, double posYprev, double posX, double posY, double velX, double velY, double deltaTime, BiFunction<Double, Double, Double> acelerationXFuction, BiFunction<Double, Double, Double> acelerationYFuction){
        double prevAceleration = acelerationXFuction.apply(posXprev, posYprev);

        double newPosX = posX + velX * deltaTime + (2.0/3) *  acelerationXFuction.apply(posX, posY) * Math.pow(deltaTime, 2) - (1.0/6) * prevAceleration * Math.pow(deltaTime, 2);
        double newPosY = posY + velY * deltaTime + (2.0/3) *  acelerationYFuction.apply(posX, posY) * Math.pow(deltaTime, 2) - (1.0/6) * prevAceleration * Math.pow(deltaTime, 2);

        double newVelX = velX + (1.0/3) * acelerationXFuction.apply(newPosX, newPosY) * deltaTime + (5.0/6) * acelerationXFuction.apply(posX, posY) * deltaTime - (1.0/6) * prevAceleration * deltaTime;
        double newVelY = velY + (1.0/3) * acelerationYFuction.apply(newPosX, newPosY) * deltaTime + (5.0/6) * acelerationYFuction.apply(posX, posY) * deltaTime - (1.0/6) * prevAceleration * deltaTime;

        return new double[]{newPosX, newPosY, newVelX, newVelY};
    }
}
