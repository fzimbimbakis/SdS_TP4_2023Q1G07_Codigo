package Pool;

import utils.JsonConfigReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VaryWhiteYAnimation {
    private static final String CONFIG_PATH = "./src/main/java/Pool/config.json";

    public static void main(String[] args) throws InterruptedException {

        JsonConfigReader config = new JsonConfigReader(CONFIG_PATH);

        int nPositions = 2;
        double yMin = 42.0;
        double yMax = 56.0;

        ExecutorService executor = Executors.newFixedThreadPool(nPositions);

        List<PoolSystem> list = new ArrayList<>();

        PoolSystem system = new PoolSystem(config, yMin, 8, "./src/main/resources/animation_"+yMin+".xyz");
        list.add(system);

        Runnable worker = new ThreadAuxAnimation(
                yMin,
                system
        );
        executor.execute(worker);

        PoolSystem system2 = new PoolSystem(config, yMax, 8, "./src/main/resources/animation_"+yMax+".xyz");
        list.add(system2);

        Runnable worker2 = new ThreadAuxAnimation(
                yMax,
                system2
        );
        executor.execute(worker2);


        executor.shutdown();
        if(!executor.awaitTermination(10, TimeUnit.HOURS))
            throw new IllegalStateException("Threads timeout");

    }

}

class ThreadAuxAnimation implements Runnable {
    private final PoolSystem system;
    private final double y;

    public ThreadAuxAnimation(double y, PoolSystem system) {
        this.y = y;
        this.system = system;
    }

    @Override
    public void run() {
        long tiempoInicial = System.currentTimeMillis();

        system.run(1);

        long tiempoFinal = System.currentTimeMillis();
        long tiempoTotal = tiempoFinal - tiempoInicial;

        long segundos = tiempoTotal / 1000;
        long minutos = segundos / 60;
        segundos = segundos % 60;

        System.out.println("White Y: " + y + " Tiempo total de ejecución: " + minutos + " min " + segundos + " seg");
    }
}
