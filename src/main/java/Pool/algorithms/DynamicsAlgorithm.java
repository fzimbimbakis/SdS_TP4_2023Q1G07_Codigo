package Pool.algorithms;

import Pool.models.particle.Particle;

public interface DynamicsAlgorithm {

    void prediction();

    void calculateNext();

    void setParticle(Particle particle);

}
