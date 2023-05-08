package DampedOscillator.algorithms;

import DampedOscillator.models.OscillatorParticle;

public class Verlet{

    // Algorithm information
    private double prevX;
    private final double dt;

    // oscillator information
    private final double k;
    private final double gamma;

    private final OscillatorParticle particle;

    public Verlet(double mass, double x, double v, double dt, double k, double gamma){
        this.dt = dt;

        this.k = k;
        this.gamma = gamma;

        particle = new OscillatorParticle(x, v, mass);

        prevX = particle.getX() - dt*particle.getV() + (Math.pow(dt,2)/(2*particle.getMass()))*particle.getForce(k, gamma);

    }


    public OscillatorParticle calculateNext(){
        double newX = 2*particle.getX() - prevX + (Math.pow(dt,2)/ particle.getMass()) * particle.getForce(k, gamma);
        particle.setV((newX - prevX)/(2*dt));
        prevX = particle.getX();
        particle.setX(newX);

        return particle;
    }

    public OscillatorParticle getParticle(){
        return particle;
    }
}
