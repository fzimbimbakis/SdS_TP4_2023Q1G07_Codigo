package models;


public class Particle{

    public enum Color {
        BLACK,
        WHITE,
        RED
    }

    private final boolean isFixed;
    private double x;
    private double y;
    private double Vx;
    private double Vy;
    private final double radius;
    private final double mass;
    private int collisionsCount;
    private final Color color;
    private final int number;

    public static Particle copy(Particle particle){
        return new Particle(particle.x, particle.y, particle.Vx, particle.Vy, particle.radius, particle.mass, particle.isFixed, particle.color, particle.number);
    }

    public Particle(double x, double y, double vx, double vy, double radius, double mass, boolean isFixed, Color color, int number) {
        this.x = x;
        this.y = y;
        Vx = vx;
        Vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.isFixed = isFixed;
        this.color = color;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public double collidesX(double xMax){
        if(Vx == 0)
            return Double.MAX_VALUE;
        if( Vx > 0)
             return (xMax - radius - x)/Vx;
        return (radius - x)/Vx;
    }

    public double collidesY(double yMax){
        if(Vy == 0)
            return Double.MAX_VALUE;
        if( Vy > 0)
            return (yMax - radius - y)/Vy;
        return (radius - y)/Vy;
    }

    public double collides(Particle b) {
        double deltaX = b.x - x;
        double deltaY = b.y - y;
        double deltaVx = b.Vx - Vx;
        double deltaVy = b.Vy - Vy;

        double productDeltaVR = deltaX * deltaVx + deltaY * deltaVy;

        if (productDeltaVR >= 0)
            return Double.MAX_VALUE;
        double v2 = Math.pow(deltaVx, 2) + Math.pow(deltaVy, 2);

        double r2 = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);

        double d = Math.pow(productDeltaVR, 2) - v2 * (r2 - Math.pow(radius + b.radius, 2));

        if (d < 0)
            return Double.MAX_VALUE;

        return -(productDeltaVR + Math.sqrt(d)) / v2;
    }

    public void bounceX(){
        collisionsCount++;
        Vx = -Vx;
    }

    public void bounceY(){
        collisionsCount++;
        Vy = -Vy;
    }

    public void bounce(Particle b) {
        collisionsCount++;

        if (b.isFixed)
            return;
        else
            b.collisionsCount++;

        double deltaX = b.x - x;
        double deltaY = b.y - y;
        double deltaVx = b.Vx - Vx;
        double deltaVy = b.Vy - Vy;

        double J = (2 * mass * b.mass * (deltaX * deltaVx + deltaY * deltaVy)) / ((radius + b.radius) * (mass + b.mass));

        double Jx = (J * deltaX) / (radius + b.radius);
        double Jy = (J * deltaY) / (radius + b.radius);

        Vx = Vx + Jx / mass;
        Vy = Vy + Jy / mass;

        b.Vx = b.Vx - Jx / b.mass;
        b.Vy = b.Vy - Jy/b.mass;

    }

    public void move(double time, double maxX, double maxY) {
        x += Vx * time;
        if (x > maxX)
            throw new IllegalStateException("Ball " + this.number + " out of bounds: x = " + x);
        y += Vy * time;
        if (y > maxY)
            throw new IllegalStateException("Ball " + this.number + " out of bounds: y = " + y);
    }

    public int getCollisionCount() {
        return collisionsCount;
    }

    public boolean isFixed() {
        return isFixed;
    }

    @Override
    public String toString() {
        switch (color){
            case RED:
                return x + " " + y + " " + Vx + " " + Vy + " " + radius + " 255 0 0";
            case BLACK:
                return x + " " + y + " " + Vx + " " + Vy + " " + radius + " 0 0 0";
            case WHITE:
                return x + " " + y + " " + Vx + " " + Vy + " " + radius + " 255 255 255";

        }

        return x + " " + y + " " + Vx + " " + Vy + " " + radius;
    }

    public double getVx() {
        return Vx;
    }

    public double getVy() {
        return Vy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public int getCollisionsCount() {
        return collisionsCount;
    }

    public Color getColor() {
        return color;
    }
}
