package Pool;

import Pool.models.Grid;
import Pool.models.particle.FixedParticle;
import Pool.models.particle.Particle;
import utils.JsonConfigReader;
import utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;

public class PoolSystem {

    private List<Particle> particles;
    private final List<FixedParticle> fixedParticleList;
    private final Double dt;
    private Double finalTime;
    private final int expectedBallsIn;
    private final JsonConfigReader config;
    private final double whiteY;
    private final String outputPath;
    private final int animationDT;

    public PoolSystem(JsonConfigReader config, double whiteY, int ballsIn, String outputPath) {
        this.config = config;
        this.whiteY = whiteY;
        this.fixedParticleList = ParticleUtils.generateFixedParticles(config);
        this.dt = 0.01;
        this.outputPath = outputPath;
        this.animationDT = 1;
        this.expectedBallsIn = ballsIn;
    }

    public void run(int times) {

        List<Double> totalTimes = new ArrayList<>();

        for (int i = 0; i < times; i++) {
            this.particles = ParticleUtils.generateInitialParticles(config, whiteY);
            Grid grid = new Grid();
            grid.addAll(particles);
//            Ovito.writeParticlesToFileXyz(outputPath, particles, fixedParticleList, dt.toString());
            double ft = 0.0;
            int counter = 1;
            int ballsIn = 0;
            while (ballsIn < this.expectedBallsIn) {

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

                for (FixedParticle particle : fixedParticleList) {
                    List<Particle> collision = particle.getCollisions(grid.getNeighbours(particle.getPosition().getX(), particle.getPosition().getY()));
                    collision.forEach(
                            p -> {
                                grid.remove(p);
                                particles.remove(p);
                            }
                    );
                    ballsIn += collision.size();
                }

//                if (counter++ % animationDT == 0)
//                    Ovito.writeParticlesToFileXyz(outputPath, particles, fixedParticleList);

                ft += dt;

            }

            totalTimes.add(ft);
            this.finalTime += ft;
        }

    }

    public Double getFinalTime() {
        return finalTime;
    }
}
