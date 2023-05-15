package Pool;

import Pool.models.Grid;
import Pool.models.particle.FixedParticle;
import Pool.models.particle.Particle;
import utils.JsonConfigReader;
import utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

public class PoolSystem {

    private List<Particle> particles;
    private final List<FixedParticle> fixedParticleList;
    private final Double dt;
    private final int expectedBallsIn;
    private final JsonConfigReader config;
    private final double whiteY;
    private final String outputPath;
    private final int animationDT;
    private double mean;
    private double std;
    private double standardError;

    public PoolSystem(JsonConfigReader config, double whiteY, int ballsIn, String outputPath) {
        this.config = config;
        this.whiteY = whiteY;
        this.fixedParticleList = ParticleUtils.generateFixedParticles(config);
        this.dt = config.getDt();
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
            Double ft = 0.0;
//            int counter = 1;
            int ballsIn = 0;
            while (ballsIn < this.expectedBallsIn) {

//                for (Particle particle : particles) {
//                    grid.remove(particle);
//                    particle.prediction();
//                    grid.add(particle);
//                }

                for (Particle particle : particles) {
                    grid.remove(particle);
                    particle.move(grid.getNeighbours(particle.getX(), particle.getY()));
                    grid.add(particle);
                }

                for (FixedParticle particle : fixedParticleList) {
                    List<Particle> neighbours = grid.getNeighbours(particle.getPosition().getX(), particle.getPosition().getY());
                    List<Particle> collision = particle.getCollisions(neighbours);
                    List<Particle> extra = new ArrayList<>();
                    collision.forEach(
                            p -> {
                                boolean a = grid.remove(p);
                                boolean b = this.particles.remove(p);
                                if(!a && !b)
                                    extra.add(p);
                            }
                    );
                    ballsIn += collision.size() - extra.size();
                }

//                if (counter++ % animationDT == 0)
//                    Ovito.writeParticlesToFileXyz(outputPath, particles, fixedParticleList);

                ft += dt;

            }

            totalTimes.add(ft);
        }

        DoubleStream stream = totalTimes.stream().mapToDouble(Double::doubleValue);
        this.mean = stream.average().orElse(0.0); // Calcula el promedio
        stream = totalTimes.stream().mapToDouble(Double::doubleValue);
        this.std = Math.sqrt(stream.map(d -> Math.pow(d - mean, 2)).average().orElse(0.0)); // Calcula la desviación estándar
        this.standardError = std / Math.sqrt(totalTimes.size());

    }

    public Double getMeanTime() {
        return mean;
    }

    public Double getTimeStd() {
        return std;
    }

    public Double getTimeStandardError() {
        return standardError;
    }
}