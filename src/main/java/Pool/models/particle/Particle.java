package Pool.models.particle;

import Pool.algorithms.DynamicsAlgorithm;
import Pool.algorithms.GearPredictorCorrector;
import Pool.models.Cell;
import Pool.models.Pair;

import java.util.List;

public class Particle {
    private Cell cell;
    private static final Double MAX_X = 224.0;
    private static final Double MAX_Y = 112.0;
    private static final Double K = 10000.0 / 100;
    private final Pair<Double> position;
    private final Pair<Double> velocity;
    private final Double radius;
    private final Double mass;
    private final DynamicsAlgorithm algorithm;
    private final Pair<Double> force;

    public static Particle copy(Particle particle, Double dt){
        return new Particle(particle.getX(), particle.getY(), particle.getVx(), particle.getVy(), particle.radius, particle.mass, new GearPredictorCorrector(dt));
    }

    public Particle(Double x, Double y, Double vx, Double vy, Double radius, Double mass, DynamicsAlgorithm algorithm) {
        this.position = new Pair<>(x, y);
        this.velocity = new Pair<>(vx, vy);
        this.radius = radius;
        this.mass = mass;
        this.algorithm = algorithm;
        this.force = new Pair<>(0.0, 0.0);
        this.algorithm.setParticle(this);
    }


    public void move(List<Particle> particles) {

        this.updateForce(particles);
        this.algorithm.calculateNext();

    }

    public void prediction(){
        algorithm.prediction();
    }

    private void updateForce(List<Particle> particles) {
        this.force.setX(0.0);
        this.force.setY(0.0);
        particles.forEach(
                p -> {
                    if (this.borderDistanceTo(p.position, p.radius) <= 0) {
                        Pair<Double> interactionForce = this.getInteractionForce(p);
                        this.force.setX(this.force.getX() + interactionForce.getX());
                        this.force.setY(this.force.getY() + interactionForce.getY());
                    }
                }
        );

        if (position.getX() < 0)
            this.force.setX(this.force.getX() - position.getX() * K);
        if (position.getX() > MAX_X)
            this.force.setX(this.force.getX() - (position.getX() - MAX_X) * K);

        if (position.getY() < 0)
            this.force.setY(this.force.getY() - position.getY() * K);
        if (position.getY() > MAX_Y)
            this.force.setY(this.force.getY() - (position.getY() - MAX_Y) * K);
    }


    public Pair<Double> getInteractionForce(Particle particle) {

        double radiusSum = particle.getRadius() + getRadius();

        double deltaX = particle.getX() - getX();
        double absDeltaX = Math.abs(deltaX);

        double deltaY = particle.getY() - getY();
        double absDeltaY = Math.abs(deltaY);

        return new Pair<>(
                K * (absDeltaX - radiusSum) * (deltaX/absDeltaX),
                K * (absDeltaY - radiusSum) * (deltaY/absDeltaY)
        );
    }

    public Double getX() {
        return position.getX();
    }


    public Double getY() {
        return position.getY();
    }

    public Double getVx() {
        return velocity.getX();
    }


    public Double getVy() {
        return velocity.getY();
    }


    public void setX(Double x) {
        position.setX(x);
    }


    public void setY(Double y) {
        position.setY(y);
    }

    public void setVx(Double Vx) {
        velocity.setX(Vx);
    }


    public void setVy(Double Vy) {
        velocity.setY(Vy);
    }


    public Double getRadius() {
        return radius;
    }

    public Double borderDistanceTo(Pair<Double> o, Double radius) {
        return Math.sqrt(
                Math.pow(getX() - o.getX(), 2) + Math.pow(getY() - o.getY(), 2)
        ) - (getRadius() + radius);
    }

    public Double getMass() {
        return mass;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + velocity.getX() + " " + velocity.getY() + " " + radius + " 255 0 0";
    }

    public Double getForceX() {
        return force.getX();
    }

    public Double getForceY() {
        return force.getY();
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
