package Pool.algorithms;

import Pool.models.particle.Particle;

public interface DynamicsAlgorithm {

    void calculateNext();

    void setParticle(Particle particle);

}
