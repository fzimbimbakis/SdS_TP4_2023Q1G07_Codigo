package Pool.algorithms;

import Pool.models.particle.Particle;

public class GearPredictorCorrector implements DynamicsAlgorithm {

    private final Double dt;
    private Particle particle;

    public GearPredictorCorrector(Double dt) {
        this.dt = dt;
    }

    public void calculateNext() {

    }

    @Override
    public void setParticle(Particle particle) {
        this.particle = particle;
    }

}
