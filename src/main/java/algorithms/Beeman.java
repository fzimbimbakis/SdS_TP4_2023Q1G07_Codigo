package algorithms;

import models.OscillatorParticle;

public class Beeman {

    // Algorithm information
    private double prevA;
    private final double dt;

    // oscillator information
    private final double k;
    private final double gamma;



    private final OscillatorParticle particle;

    public Beeman(double mass, double x, double v, double dt, double k, double gamma){
        this.dt = dt;

        this.k = k;
        this.gamma = gamma;

        particle = new OscillatorParticle(x, v, (-k * x - gamma * v)/mass , mass);

        // Euler
        double prevX = x - dt*v + (Math.pow(dt,2)/2*mass)*particle.getForce(k,gamma);
        double prevV = v - (dt/mass)*particle.getForce(k,gamma);
        prevA = (-k * prevX - gamma * prevV)/mass;

    }

    public OscillatorParticle calculateNext() {
        double dt2 = Math.pow(dt,2);
        double newX = particle.getX() + particle.getV()*dt + (2.0/3.0)*particle.getA()*dt2 - (1.0/6.0)*prevA*dt2;


        double predictedX = particle.getX() + particle.getV() * dt + 0.5 * particle.getA() * dt2;
        double predictedV = particle.getV() + 0.5 * (3 * particle.getA() - prevA) * dt;
        double predictedA = (-k * predictedX - gamma * predictedV)/ particle.getMass();

        double newV = particle.getV() + (1.0/3.0)*predictedA*dt + (5.0/6.0)* particle.getA()*dt - (1.0/6.0)*prevA*dt;

        particle.setX(newX);
        particle.setV(newV);
        prevA = particle.getA();
        particle.setA(k, gamma);

        return particle;
    }


    public OscillatorParticle getParticle(){
        return particle;
    }


}
