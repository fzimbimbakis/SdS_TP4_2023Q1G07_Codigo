package Pool.algorithms;

import Pool.models.particle.Particle;

public class GearPredictorCorrector implements DynamicsAlgorithm {

    private final Double dt;
    private Particle particle;
    private Double[] rX = new Double[6];
    private Double[] rY = new Double[6];
    private final double[] alphas = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};
    private final int[] factorial = {0, 1, 2, 6, 24, 120};

    private boolean initialStep = true;

    public GearPredictorCorrector(Double dt) {
        this.dt = dt;
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
     * */
    /**
     * Se deberia llamar todo en este orden
     * 1) inicializo el algoritmo
     * 2) seteo la particula en el algoritmo
     * 3) llamar a prediction para todas las particulas
     * 4) actualizar las fuerzas de todas las particulas
     * 5) llamar al calculateNext
     * */
    @Override
    public void prediction(){
        if (!initialStep)
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
        initialStep = false;
    }

    private void predict(){
        double dt2 = Math.pow(dt,2);
        double dt3 = Math.pow(dt,3);
        double dt4 = Math.pow(dt,4);
        double dt5 = Math.pow(dt,5);

        rX[0] += rX[1]*dt + rX[2]*(dt2/2) + rX[3]*(dt3/factorial[3]) +
                rX[4]*(dt4/factorial[4]) + rX[5]*(dt5/factorial[5]);
        rX[1] += rX[2]*dt + rX[3]*(dt2/2) + rX[4]*(dt3/factorial[3]) +
                rX[5]*(dt4/factorial[4]);
        rX[2] += rX[3]*dt + rX[4]*(dt2/2) + rX[5]*(dt3/factorial[3]);
        rX[3] += rX[4]*dt + rX[5]*(dt2/2);
        rX[4] += rX[5]*dt;

        rY[0] += rY[1]*dt + rY[2]*(dt2/2) + rY[3]*(dt3/factorial[3]) +
                rY[4]*(dt4/factorial[4]) + rY[5]*(dt5/factorial[5]);
        rY[1] += rY[2]*dt + rY[3]*(dt2/2) + rY[4]*(dt3/factorial[3]) +
                rY[5]*(dt4/factorial[4]);
        rY[2] += rY[3]*dt + rY[4]*(dt2/2) + rY[5]*(dt3/factorial[3]);
        rY[3] += rY[4]*dt + rY[5]*(dt2/2);
        rY[4] += rY[5]*dt;
    }

    private double getDeltaR2x(){
        double a = particle.getForceX()/ particle.getMass();
        double deltaA = a - rX[2];

        return (deltaA*Math.pow(dt,2))/2.0;
    }

    private double getDeltaR2y(){
        double a = particle.getForceY()/ particle.getMass();
        double deltaA = a - rY[2];

        return (deltaA*Math.pow(dt,2))/2.0;
    }

    private void correct(double deltaR2x, double deltaR2y){
        rX[0] += alphas[0]*deltaR2x;
        rX[1] += alphas[1]*deltaR2x*(factorial[1]/dt);
        rX[2] += alphas[2]*deltaR2x*(factorial[2]/Math.pow(dt,2));
        rX[3] += alphas[3]*deltaR2x*(factorial[3]/Math.pow(dt,3));
        rX[4] += alphas[4]*deltaR2x*(factorial[4]/Math.pow(dt,4));
        rX[5] += alphas[5]*deltaR2x*(factorial[5]/Math.pow(dt,5));

        rY[0] += alphas[0]*deltaR2y;
        rY[1] += alphas[1]*deltaR2y*(factorial[1]/dt);
        rY[2] += alphas[2]*deltaR2y*(factorial[2]/Math.pow(dt,2));
        rY[3] += alphas[3]*deltaR2y*(factorial[3]/Math.pow(dt,3));
        rY[4] += alphas[4]*deltaR2y*(factorial[4]/Math.pow(dt,4));
        rY[5] += alphas[5]*deltaR2y*(factorial[5]/Math.pow(dt,5));
    }

}
