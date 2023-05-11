package Pool;

import Pool.models.Grid;
import Pool.models.Pair;
import Pool.models.particle.FixedParticle;
import Pool.models.particle.Particle;
import utils.Ovito;

import java.util.ArrayList;
import java.util.List;

public class DynamicSystem {

    private final List<Particle> particles;
    private final List<FixedParticle> fixedParticleList;
    private final Double dt;
    private final Double maxT;
    private final String outputPath;
    private final int animationDT;
    List<List<Pair<Double>>> positions;

    private void savePositions() {
        List<Pair<Double>> list = new ArrayList<>();
        particles.stream().map(
                particle -> Pair.copy(particle.getPosition())
        ).forEach(list::add);
        this.positions.add(list);
    }

    public DynamicSystem(List<Particle> particles, List<FixedParticle> fixedParticleList, Double dt, Double maxT, String outputPath, int animationDT) {
        this.positions = new ArrayList<>();
        this.particles = particles;
        savePositions();
        this.fixedParticleList = fixedParticleList;
        this.dt = dt;
        this.maxT = maxT;
        this.outputPath = outputPath;
        this.animationDT = animationDT;
    }

    public void run() {

        Grid grid = new Grid();
        grid.addAll(particles);
        Ovito.writeParticlesToFileXyz(outputPath, particles, fixedParticleList, dt.toString());
        double time = 0.0;
        int counter = 0;
        while (time < maxT) {

            for (Particle particle : particles) {
                grid.remove(particle);
                particle.prediction();
                grid.add(particle);
            }

            for (Particle particle : particles) {
                grid.remove(particle);
                particle.move(grid.getNeighbours(particle.getX(), particle.getY()));
                grid.add(particle);
            }

            if (counter++ % animationDT == 0) {
                Ovito.writeParticlesToFileXyz(outputPath, particles, fixedParticleList);
                savePositions();
            }

            time += dt;

        }

    }


}
