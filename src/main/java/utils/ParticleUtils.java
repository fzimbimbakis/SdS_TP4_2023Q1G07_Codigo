package utils;
import models.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils {

    private static final boolean FIXED = true;

    private static Double randomEpsilon(Double min, Double max){
        return min + Math.random() * Math.abs(max - min);
    }

    public static List<Particle> generateFixedParticles(JsonConfigReader config){
        Double RADIUS = config.getRadius();
        Double MASS = config.getMass();


        List<Particle> list = new ArrayList<>();

        //// Fixed
        list.add(new Particle(0, 0, 0, 0, 2*RADIUS, MASS, FIXED, Particle.Color.BLACK, 101));
        list.add(new Particle(config.getMaxX() / 2, 0, 0, 0, 2*RADIUS, MASS, FIXED, Particle.Color.BLACK, 102));
        list.add(new Particle(config.getMaxX(), 0, 0, 0, 2*RADIUS, MASS, FIXED, Particle.Color.BLACK, 103));

        list.add(new Particle(0, config.getMaxY(), 0, 0, 2*RADIUS, MASS, FIXED, Particle.Color.BLACK, 104));
        list.add(new Particle(config.getMaxX() / 2, config.getMaxY(), 0, 0, 2*RADIUS, MASS, FIXED, Particle.Color.BLACK, 105));
        list.add(new Particle(config.getMaxX(), config.getMaxY(), 0, 0, 2*RADIUS, MASS, FIXED, Particle.Color.BLACK, 106));

        return list;
    }

    public static List<Particle> generateInitialParticles(JsonConfigReader config){

        Double RADIUS = config.getRadius();
        Double MASS = config.getMass();
        Double MAX_EPSILON = config.getMaxEpsilon();
        Double MIN_EPSILON = config.getMinEpsilon();

        List<Particle> list = new ArrayList<>();

        //// White
        list.add(new Particle(config.getWhiteX(), config.getWhiteY(), config.getWhiteV(), 0, RADIUS, MASS, !FIXED, Particle.Color.WHITE, -1));

        //// Default balls
        Double triangleX = config.getTriangleX();
        Double triangleY = config.getTriangleY();

        int n = 1;
        double deltaY = RADIUS * 2 + MAX_EPSILON;
        double deltaX = Math.cos(Math.PI / 6) * (RADIUS * 2 + MAX_EPSILON);

        for (int i = 0; i < 5; i++) {
            list.add(new Particle(triangleX + randomEpsilon(MIN_EPSILON, MAX_EPSILON), triangleY + randomEpsilon(MIN_EPSILON, MAX_EPSILON), 0, 0, RADIUS, MASS, !FIXED, Particle.Color.RED, i * n / 2));

            for (int j = 1; j < n; j++) {
                list.add(new Particle(triangleX + randomEpsilon(MIN_EPSILON, MAX_EPSILON), triangleY - j * deltaY + randomEpsilon(MIN_EPSILON, MAX_EPSILON), 0, 0, RADIUS, MASS, !FIXED, Particle.Color.RED, i * n / 2 + j));
            }

            n++;
            triangleX += deltaX;
            triangleY += deltaY / 2;
        }

        return list;
    }

}
