package Pool.algorithms;

import Pool.models.particle.Particle;

public class Verlet implements DynamicsAlgorithm{

    // Algorithm information
    private double prevX;
    private double prevY;
    private final double dt;

    private Particle particle;

    public Verlet(double dt){
        this.dt = dt;
    }


    @Override
    public void prediction() {

    }

    public void calculateNext(){
        double newX = 2*particle.getX() - prevX + (Math.pow(dt,2)/ particle.getMass()) * particle.getForceX();
        double newY = 2*particle.getY() - prevY + (Math.pow(dt,2)/ particle.getMass()) * particle.getForceY();

        particle.setVx((newX - prevX)/(2*dt));
        particle.setVy((newY - prevY)/(2*dt));

        prevX = particle.getX();
        particle.setX(newX);
        prevY = particle.getY();
        particle.setY(newY);
    }

    @Override
    public void setParticle(Particle particle) {
        this.particle = particle;

        prevX = particle.getX() - dt*particle.getVx() + (Math.pow(dt,2)/(2*particle.getMass()))*particle.getForceX();
        prevY = particle.getY() - dt*particle.getVy() + (Math.pow(dt,2)/(2*particle.getMass()))*particle.getForceY();
    }

}
