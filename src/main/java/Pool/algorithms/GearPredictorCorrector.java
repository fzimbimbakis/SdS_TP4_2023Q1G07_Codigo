package Pool.algorithms;

import Pool.models.particle.Particle;

public class GearPredictorCorrector implements DynamicsAlgorithm {

    private Particle particle;
    private final Double[] rX = new Double[6];
    private final Double[] rY = new Double[6];
    private static final double[] alphas = {3.0 / 20, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60};

    private final double[] taylorCoef;
    private final double[] alphaDividedTaylorCoef;
//    private boolean initialStep = true;

    public GearPredictorCorrector(Double dt) {
        double dt2 = Math.pow(dt, 2);
        double dt3 = Math.pow(dt, 3);
        double dt4 = Math.pow(dt, 4);
        double dt5 = Math.pow(dt, 5);
        int[] factorial = {0, 1, 2, 6, 24, 120};
        this.taylorCoef = new double[]{1, dt / factorial[1], dt2 / factorial[2], dt3 / factorial[3], dt4 / factorial[4], dt5 / factorial[5]};
        alphaDividedTaylorCoef = new double[6];
        for (int i = 0; i < 6; i++) {
            alphaDividedTaylorCoef[i] = alphas[i] / taylorCoef[i];
        }
    }

    @Override
    public void setParticle(Particle particle) {
        this.particle = particle;

        rX[0] = particle.getX();
        rX[1] = particle.getVx();
        rX[2] = particle.getForceX() / particle.getMass();
        rX[3] = 0.0;
        rX[4] = 0.0;
        rX[5] = 0.0;

        rY[0] = particle.getY();
        rY[1] = particle.getVy();
        rY[2] = particle.getForceY() / particle.getMass();
        rY[3] = 0.0;
        rY[4] = 0.0;
        rY[5] = 0.0;
    }


    /**
     * se tiene que llamar antes del calculateNext para que todas las particulas
     * actualicen su posicion segun lo predecido y asi se calcule su respectivas fuerzas
     * en base a las posiciones predichas.
     * Cuando se tengan las fuerzas predichas de todas las particulas ahi se ejecuta el calculateNext
     * ------
     * Se deberia llamar todo en este orden
     * 1) inicializo el algoritmo
     * 2) seteo la particula en el algoritmo
     * 3) llamar a prediction para todas las particulas
     * 4) actualizar las fuerzas de todas las particulas
     * 5) llamar al calculateNext
     * */
    @Override
    public void prediction(){
        predict();
        particle.setX(rX[0]);
        particle.setY(rY[0]);
    }

    public void calculateNext() {


        double deltaR2x = getDeltaR2x();
        double deltaR2y = getDeltaR2y();
        correct(deltaR2x, deltaR2y);

        particle.setX(rX[0]);
        particle.setVx(rX[1]);
        particle.setY(rY[0]);
        particle.setVy(rY[1]);
//        initialStep = false;
    }

    private void predict() {

        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6 - i; j++) {
                rX[i] += rX[j + i] * taylorCoef[j];
                rY[i] += rY[j + i] * taylorCoef[j];
            }
        }
    }

    private double getDeltaR2x(){
        double a = particle.getForceX()/ particle.getMass();
        double deltaA = a - rX[2];

        return deltaA * taylorCoef[2];
    }

    private double getDeltaR2y(){
        double a = particle.getForceY()/ particle.getMass();
        double deltaA = a - rY[2];

        return deltaA * taylorCoef[2];
    }

    private void correct(double deltaR2x, double deltaR2y) {

        for (int i = 0; i < 6; i++) {
            rX[i] += deltaR2x * alphaDividedTaylorCoef[i];
            rY[i] += deltaR2y * alphaDividedTaylorCoef[i];
        }

    }

}
