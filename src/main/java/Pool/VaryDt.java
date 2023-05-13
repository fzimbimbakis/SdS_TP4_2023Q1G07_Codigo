package Pool;

import Pool.models.Pair;
import Pool.models.particle.FixedParticle;
import Pool.models.particle.Particle;
import utils.JsonConfigReader;
import utils.Ovito;
import utils.ParticleUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VaryDt {
    private static final String CONFIG_PATH = "./src/main/java/Pool/config.json";

    private static List<Particle> copy(List<Particle> particles, Double dt, Particle.Color color){
        List<Particle> copy = new ArrayList<>();
        for (Particle p : particles) {
            copy.add(Particle.copy(p, dt, color));
        }
        return copy;
    }

    public static void main(String[] args) throws InterruptedException {

        JsonConfigReader config = new JsonConfigReader(CONFIG_PATH);

        List<FixedParticle> fixedParticleList = ParticleUtils.generateInvisible(config);

        List<Particle> particles = ParticleUtils.generateInitialParticles(config);

        // crea un pool de threads con 4 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<DynamicSystem> systems = new ArrayList<>();

        // ejecuta algunas tareas en paralelo
        for (int i = 2; i < 5; i++) {
            systems.add(
                    new DynamicSystem(
                            copy(particles, Math.pow(10, -i), Particle.Color.values()[i - 2]),
                            fixedParticleList,
                            Math.pow(10, -i),
                            config.getMaxTime(),
                            Ovito.createFile("output", "xyz"),
                            (int) Math.pow(10, i - 2)
                    )
            );
            Runnable worker = new Thread(
                    systems.get(i - 2),
                    Math.pow(10, -i)
            );
            executor.execute(worker);
        }

        // cierra el pool de threads
        executor.shutdown();
        boolean termination = executor.awaitTermination(2, TimeUnit.HOURS);

        if (termination)
            System.out.println("Todos los threads han terminado.");
        else System.out.println("Threads timeout");

        List<List<Double>> phis = new ArrayList<>();

        for (int i = 0; i < 2; i++) {

            phis.add(new ArrayList<>());

            for (int j = 0; j < systems.get(i).positions.size(); j++) {
                List<Pair<Double>> A = systems.get(i).positions.get(j);
                List<Pair<Double>> B = systems.get(i + 1).positions.get(j);

                Iterator<Pair<Double>> iterator1 = A.iterator();
                Iterator<Pair<Double>> iterator2 = B.iterator();
                double result = 0.0;
                while (iterator1.hasNext() && iterator2.hasNext()) {
                    Pair<Double> element1 = iterator1.next();
                    Pair<Double> element2 = iterator2.next();
                    result += getDistance(element1, element2);
                }
                phis.get(i).add(result);
            }
        }

        phis.forEach(
                phi -> Ovito.writeListToFIle(phi, Ovito.createFile("phi", "txt"), true)
        );

    }

    private static double getDistance(Pair<Double> a, Pair<Double> b){
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        return Math.sqrt(
                Math.pow(dx, 2) + Math.pow(dy, 2)
        );
    }

}

class Thread implements Runnable{
    private final DynamicSystem system;
    private final Double dt;

    public Thread(DynamicSystem system, Double dt) {
        this.system = system;
        this.dt = dt;
    }

    @Override
    public void run() {
        long tiempoInicial = System.currentTimeMillis();

        system.run();

        long tiempoFinal = System.currentTimeMillis();
        long tiempoTotal = tiempoFinal - tiempoInicial;

        long segundos = tiempoTotal / 1000;
        long minutos = segundos / 60;
        segundos = segundos % 60;

        System.out.println("Delta T: " + dt + " Tiempo total de ejecución: " + minutos + " min " + segundos + " seg");
    }
}
