package Pool;

import Pool.models.Grid;
import Pool.models.particle.FixedParticle;
import Pool.models.particle.Particle;
import utils.Ovito;

import java.util.List;

public class DynamicSystem {

    private final List<Particle> particles;
    private final List<FixedParticle> fixedParticleList;
    private final Double dt;
    private final Double maxT;
    private final String outputPath;
    private final int animationDT;

    public DynamicSystem(List<Particle> particles, List<FixedParticle> fixedParticleList, Double dt, Double maxT, String outputPath, int animationDT) {
        this.particles = particles;
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

            if (counter++ % animationDT == 0)
                Ovito.writeParticlesToFileXyz(outputPath, particles, fixedParticleList);

            time += dt;

        }

    }


}
