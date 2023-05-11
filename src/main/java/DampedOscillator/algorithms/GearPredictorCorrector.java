package DampedOscillator.algorithms;

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
    private final double[] alphas = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};

    // oscillator information
    private final double k;
    private final double gamma;
    private final double[] taylorCoef;
    private final double[] alphaDividedTaylorCoef;



    private final OscillatorParticle particle;

    public GearPredictorCorrector(double mass, double x, double v, double dt, double k, double gamma){
        double dt2 = Math.pow(dt, 2);
        double dt3 = Math.pow(dt, 3);
        double dt4 = Math.pow(dt, 4);
        double dt5 = Math.pow(dt, 5);
        int[] factorial = {1, 1, 2, 6, 24, 120};
        this.taylorCoef = new double[]{1, dt / factorial[1], dt2 / factorial[2], dt3 / factorial[3], dt4 / factorial[4], dt5 / factorial[5]};
        alphaDividedTaylorCoef = new double[6];
        for (int i = 0; i < 6; i++) {
            alphaDividedTaylorCoef[i] = alphas[i] / taylorCoef[i];
        }

        this.k = k;
        this.gamma = gamma;

        particle = new OscillatorParticle(x, v, (-k * x - gamma * v) / mass, mass);

        // Initial values
        r0p = particle.getX();
        r1p = particle.getV();
        r2p = particle.getA();
        r3p = (-(k) * r1p - (gamma) * r2p) / particle.getMass();
        r4p = (-(k) * r2p - (gamma) * r3p) / particle.getMass();
        r5p = (-(k) * r3p - (gamma) * r4p) / particle.getMass();

        r0c = r0p;
        r1c = r1p;
        r2c = r2p;
        r3c = r3p;
        r4c = r4p;
        r5c = r5p;

    }

    public OscillatorParticle calculateNext() {

        predict();
        double deltaR2 = getDeltaR2();
        correct(deltaR2);

        particle.setX(r0c);
        particle.setV(r1c);
        particle.setA(r2c);

        return particle;
    }

    private void predict(){
        r0p = r0c + r1c * taylorCoef[1] + r2c * (taylorCoef[2]) + r3c * (taylorCoef[3]) +
                r4c * (taylorCoef[4]) + r5c * (taylorCoef[5]);
        r1p = r1c + r2c * taylorCoef[1] + r3c * (taylorCoef[2]) +
                r4c * (taylorCoef[3]) + r5c * (taylorCoef[4]);
        r2p = r2c + r3c * taylorCoef[1] + r4c * (taylorCoef[2]) + r5c * (taylorCoef[3]);
        r3p = r3c + r4c * taylorCoef[1] + r5c * (taylorCoef[2]);
        r4p = r4c + r5c * taylorCoef[1];
        r5p = r5c;
    }

    private double getDeltaR2(){
        double a = (-k * r0p - gamma * r1p)/ particle.getMass();
        double deltaA = a - r2p;

        return deltaA * (taylorCoef[2]);
    }

    private void correct(double deltaR2){
        r0c = r0p + deltaR2 * alphaDividedTaylorCoef[0];
        r1c = r1p + deltaR2 * alphaDividedTaylorCoef[1];
        r2c = r2p + deltaR2 * alphaDividedTaylorCoef[2];
        r3c = r3p + deltaR2 * alphaDividedTaylorCoef[3];
        r4c = r4p + deltaR2 * alphaDividedTaylorCoef[4];
        r5c = r5p + deltaR2 * alphaDividedTaylorCoef[5];
    }

    public OscillatorParticle getParticle(){
        return particle;
    }
}
