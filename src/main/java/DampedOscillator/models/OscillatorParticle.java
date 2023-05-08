package DampedOscillator.models;

public class OscillatorParticle {


    private double x;
    private double v;
    private double a;
    private final double mass;



    public OscillatorParticle(double x, double v, double a, double mass){
        this.x = x;
        this.mass = mass;
        this.a = a;
        this.v = v;
    }

    public OscillatorParticle(double x, double v, double mass){
        this.x = x;
        this.mass = mass;
        this.v = v;
    }

    public double getForce(double k, double gamma){
        return -k * x - gamma * v;
    }

    public double getX() {
        return x;
    }

    public double getMass() {
        return mass;
    }

    public double getV() {
        return v;
    }

    public double getA() {
        return a;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void setA(double a){
        this.a = a;
    }

    public void setA(double k, double gamma){
        a = (-k * x - gamma * v)/mass;
    }
}

