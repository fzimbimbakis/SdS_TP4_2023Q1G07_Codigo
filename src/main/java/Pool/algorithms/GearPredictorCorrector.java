package Pool.algorithms;

import DampedOscillator.models.OscillatorParticle;

public class GearPredictorCorrector {

    // Algorithm information
    private double r0p;
    private double r1p;
    private double r2p;
    private double r3p;
    private double r4p;
    private double r5p;
    private double r0c;
    private double r1c;
    private double r2c;
    private double r3c;
    private double r4c;
    private double r5c;
    private final double dt;
    private final double[] alphas = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};

    private boolean initialStep = true;
    // oscillator information
    private final double k;
    private final double gamma;



    private final OscillatorParticle particle;

    public GearPredictorCorrector(double mass, double x, double v, double dt, double k, double gamma){
        this.dt = dt;

        this.k = k;
        this.gamma = gamma;

        particle = new OscillatorParticle(x, v, (-k * x - gamma * v)/mass , mass);

        // Initial values
        r0p = particle.getX();
        r1p = particle.getV();
        r2p = particle.getA();
        r3p = -(k / particle.getMass()) * r1p - (gamma / particle.getMass()) * r2p;
        r4p = -(k / particle.getMass()) * r2p - (gamma / particle.getMass()) * r3p;
        r5p = -(k / particle.getMass()) * r3p - (gamma / particle.getMass()) * r4p;

    }

    public OscillatorParticle calculateNext() {

        if (!initialStep)
            predict();
        double deltaR2 = getDeltaR2();
        correct(deltaR2);

        particle.setX(r0c);
        particle.setV(r1c);
        particle.setA(r2c);
        initialStep = false;

        return particle;
    }

    private void predict(){
        r0p = r0c + r1c*dt + r2c*(Math.pow(dt,2)/2) + r3c*(Math.pow(dt,3)/factorial(3)) +
                r4c*(Math.pow(dt,4)/factorial(4)) + r5c*(Math.pow(dt,5)/factorial(5));
        r1p = r1c + r2c*dt + r3c*(Math.pow(dt,2)/2) +
                r4c*(Math.pow(dt,3)/factorial(3)) + r5c*(Math.pow(dt,4)/factorial(4));
        r2p = r2c + r3c*dt + r4c*(Math.pow(dt,2)/2) + r5c*(Math.pow(dt,3)/factorial(3));
        r3p = r3c + r4c*dt + r5c*(Math.pow(dt,2)/2);
        r4p = r4c + r5c*dt;
        r5p = r5c;
    }

    private double getDeltaR2(){
        double a = (-k * r0p - gamma * r1p)/ particle.getMass();
        double deltaA = a - r2p;

        return deltaA * (Math.pow(dt,2)/2);
    }

    private void correct(double deltaR2){
        r0c = r0p + alphas[0]*deltaR2;
        r1c = r1p + alphas[1]*deltaR2*(1/dt);
        r2c = r2p + alphas[2]*deltaR2*(2/Math.pow(dt,2));
        r3c = r3p + alphas[3]*deltaR2*(factorial(3)/Math.pow(dt,3));
        r4c = r4p + alphas[4]*deltaR2*(factorial(4)/Math.pow(dt,4));
        r5c = r5p + alphas[5]*deltaR2*(factorial(5)/Math.pow(dt,5));
    }

    private int factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    public OscillatorParticle getParticle(){
        return particle;
    }
}
