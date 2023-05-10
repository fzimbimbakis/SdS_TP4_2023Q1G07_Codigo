package Pool;

import Pool.models.Grid;
import Pool.models.particle.FixedParticle;
import Pool.models.particle.Particle;
import utils.JsonConfigReader;
import utils.Ovito;
import utils.ParticleUtils;

import java.util.List;

public class DynamicSystem {

    private static final String CONFIG_PATH = "";

    public void run(){

        String path = Ovito.createFile("output", "xyz");

        JsonConfigReader config = new JsonConfigReader(CONFIG_PATH);

        List<FixedParticle> fixedParticleList = ParticleUtils.generateFixedParticles(config);

        List<Particle> particles = ParticleUtils.generateInitialParticles(config);

        Grid grid = new Grid();
        grid.addAll(particles);

        double time = 0.0;
        while (time < config.getMaxTime()){

            for (Particle particle : particles) {
                grid.remove(particle);
                particle.move(grid.getNeighbours(particle.getX(), particle.getY()));
                grid.add(particle);
            }

            Ovito.writeParticlesToFileXyz(path, particles);

            time += config.getDt();

        }

    }


}
