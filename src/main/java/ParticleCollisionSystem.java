import models.Collision;
import models.Particle;
import utils.JsonConfigReader;
import utils.Ovito;
import utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ParticleCollisionSystem {
    private static final String OUTPUT_NAME = "output";
    private static final String OUTPUT_EXTENSION = "xyz";
    private static final int BALL_COUNT = 16;
    JsonConfigReader config;
    private int ballsIn = 0;
    PriorityQueue<Collision> queue = new PriorityQueue<>();
    List<Particle> particles;
    List<Particle> fixedParticles;
    private final List<Double> eventTimes = new ArrayList<>();
    private Double finalTime = 0.0;
    private final boolean generateXYZ;

    public ParticleCollisionSystem(JsonConfigReader config) {
        this.config = config;
        this.particles = ParticleUtils.generateInitialParticles(config);
        this.fixedParticles = ParticleUtils.generateFixedParticles(config);
        this.generateXYZ = false;
    }

    public ParticleCollisionSystem(JsonConfigReader config, boolean generateXYZ) {
        this.config = config;
        this.particles = ParticleUtils.generateInitialParticles(config);
        this.fixedParticles = ParticleUtils.generateFixedParticles(config);
        this.generateXYZ = generateXYZ;
    }

    public ParticleCollisionSystem( List<Particle> particles, JsonConfigReader config, boolean generateXYZ) {
        this.config = config;
        this.particles = particles;
        this.fixedParticles = ParticleUtils.generateFixedParticles(config);
        this.generateXYZ = generateXYZ;
    }

    public void run() {
        this.fillQueue();
        double time = 0.0;
        Collision collision;
        String path = null;

        if (this.generateXYZ) {
            path = Ovito.createFile(OUTPUT_NAME, OUTPUT_EXTENSION);
            Ovito.writeParticlesToFileXyz(path, time, particles, fixedParticles, null, null);
        }
        while (ballsIn != BALL_COUNT) {

            //// Search next event
            queue.removeIf(Collision::wasSuperveningEvent);
            collision = queue.remove();

            //// Move particles
            time = collision.getT();
            moveAllParticles(time);
            if (this.generateXYZ)
                Ovito.writeParticlesToFileXyz(path, time, particles, fixedParticles, collision.getA(), collision.getB());

            //// Execute collision
            eventTimes.add(time);
            this.finalTime += time;
            collision.execute();

            //// If one is fixed -> Remove not fixed ball
            if (collision.getB() != null && collision.getB().isFixed()) {
                particles.remove(collision.getA());
                ballsIn++;
            } else
                updateQueue(collision.getA(), collision.getB());


        }
    }

    private void moveAllParticles(double time) {
        for (Particle p : particles) {
            p.move(time, config.getMaxX(), config.getMaxY());
        }
    }

    private void updateQueue(Particle A, Particle B){
        //// Update queue for particle A and B
        if(A != null) {
            for (Particle p : particles) {
                if (!p.equals(A))
                    queue.add(new Collision(A, p, config.getMaxX(), config.getMaxY()));
            }
            addDefaultCollisions(A);
        }
        if(B != null) {
            for (Particle p : particles) {
                if (!p.equals(B))
                    queue.add(new Collision(B, p, config.getMaxX(), config.getMaxY()));
            }
            addDefaultCollisions(B);
        }
    }

    private void fillQueue(){
        particles.forEach(
                p1 -> {
                    particles.forEach(p2 -> {
                        if(!p1.equals(p2))
                            queue.add(new Collision(p1, p2, config.getMaxX(), config.getMaxY()));
                    });

                    addDefaultCollisions(p1);
                }
        );
    }

    private void addDefaultCollisions(Particle p){
        if (p.getVx() != 0)
            queue.add(new Collision(p, null, config.getMaxX(), config.getMaxY()));
        if (p.getVy() != 0)
            queue.add(new Collision(null, p, config.getMaxX(), config.getMaxY()));

        fixedParticles.forEach(f -> {
            queue.add(new Collision(p, f, config.getMaxX(), config.getMaxY()));
        });
    }

    public List<Double> getEventTimes() {
        return eventTimes;
    }

    public Double getFinalTime() {
        return finalTime;
    }
}
