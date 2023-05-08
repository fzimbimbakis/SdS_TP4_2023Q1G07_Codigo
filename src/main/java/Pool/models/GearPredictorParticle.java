package Pool.models;

import Pool.algorithms.GearPredictorCorrector;

public class GearPredictorParticle extends Particle {

    private GearPredictorCorrector gearPredictorCorrector;

    public GearPredictorParticle(double x, double y, double vx, double vy, double radius, double mass, boolean isFixed, Color color, int number) {
        super(x, y, vx, vy, radius, mass, isFixed, color, number);

    }
}
