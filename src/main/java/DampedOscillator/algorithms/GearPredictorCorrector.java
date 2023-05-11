package DampedOscillator.algorithms;

import DampedOscillator.models.OscillatorParticle;

public class GearPredictorCorrector {

    // Algorithm information
    private double r0;
    private double r1;
    private double r2;
    private double r3;
    private double r4;
    private double r5;
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
        r0 = particle.getX();
        r1 = particle.getV();
        r2 = particle.getA();
        r3 = (-(k) * r1 - (gamma) * r2) / particle.getMass();
        r4 = (-(k) * r2 - (gamma) * r3) / particle.getMass();
        r5 = (-(k) * r3 - (gamma) * r4) / particle.getMass();

    }

    public OscillatorParticle calculateNext() {

        predict();
        double deltaR2 = getDeltaR2();
        correct(deltaR2);

        particle.setX(r0);
        particle.setV(r1);
        particle.setA(r2);

        return particle;
    }

    private void predict(){
        r0 += r1 * taylorCoef[1] + r2 * (taylorCoef[2]) + r3 * (taylorCoef[3]) +
                r4 * (taylorCoef[4]) + r5 * (taylorCoef[5]);
        r1 += r2 * taylorCoef[1] + r3 * (taylorCoef[2]) +
                r4 * (taylorCoef[3]) + r5 * (taylorCoef[4]);
        r2 += r3 * taylorCoef[1] + r4 * (taylorCoef[2]) + r5 * (taylorCoef[3]);
        r3 += r4 * taylorCoef[1] + r5 * (taylorCoef[2]);
        r4 += r5 * taylorCoef[1];
    }

    private double getDeltaR2(){
        double a = (-k * r0 - gamma * r1)/ particle.getMass();
        double deltaA = a - r2;

        return deltaA * (taylorCoef[2]);
    }

    private void correct(double deltaR2){
        r0 += deltaR2 * alphaDividedTaylorCoef[0];
        r1 += deltaR2 * alphaDividedTaylorCoef[1];
        r2 += deltaR2 * alphaDividedTaylorCoef[2];
        r3 += deltaR2 * alphaDividedTaylorCoef[3];
        r4 += deltaR2 * alphaDividedTaylorCoef[4];
        r5 += deltaR2 * alphaDividedTaylorCoef[5];
    }

    public OscillatorParticle getParticle(){
        return particle;
    }
}
