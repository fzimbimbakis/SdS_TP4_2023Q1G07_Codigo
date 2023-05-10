package Pool.models;

import Pool.models.particle.Particle;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Comparable<Cell>{

    private final List<Particle> particles = new ArrayList<>();
    private final Pair<Double> xLimits;
    private final Pair<Double> yLimits;

    public Cell(Pair<Double> xLimits, Pair<Double> yLimits) {
        this.xLimits = xLimits;
        this.yLimits = yLimits;
    }

    public void add(Particle particle){
        particles.add(particle);
    }

    public boolean remove(Particle particle){
        return particles.remove(particle);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Pair<Double> getxLimits() {
        return xLimits;
    }

    public Pair<Double> getyLimits() {
        return yLimits;
    }

    @Override
    public int compareTo(Cell o) {
        return Integer.compare(this.particles.size(), o.particles.size());
    }
}
