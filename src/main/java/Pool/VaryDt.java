package Pool;

import Pool.models.particle.FixedParticle;
import Pool.models.particle.Particle;
import utils.JsonConfigReader;
import utils.Ovito;
import utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VaryDt {
    private static final String CONFIG_PATH = "./src/main/java/Pool/config.json";

    private static List<Particle> copy(List<Particle> particles, Double dt){
        List<Particle> copy = new ArrayList<>();
        for (Particle p : particles) {
            copy.add(Particle.copy(p, dt));
        }
        return copy;
    }

    public static void main(String[] args) throws InterruptedException {

        JsonConfigReader config = new JsonConfigReader(CONFIG_PATH);

        List<FixedParticle> fixedParticleList = ParticleUtils.generateInvisible(config);

        List<Particle> particles = ParticleUtils.generateInitialParticles(config);

        // crea un pool de threads con 4 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // ejecuta algunas tareas en paralelo
        for (int i = 2; i < 6; i++) {
            Runnable worker = new Thread(
                    new DynamicSystem(
                            copy(particles, Math.pow(10, -i)),
                            fixedParticleList,
                            Math.pow(10, -i),
                            config.getMaxTime(),
                            Ovito.createFile("output", "xyz"),
                            (int)Math.pow(10, i-2)
                    ),
                    Math.pow(10, -i)
            );
            executor.execute(worker);
        }

        // cierra el pool de threads
        executor.shutdown();
        boolean termination = executor.awaitTermination(10, TimeUnit.MINUTES);

        if(termination)
            System.out.println("Todos los threads han terminado.");
        else System.out.println("Threads timeout");
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
