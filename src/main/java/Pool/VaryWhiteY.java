package Pool;

import utils.JsonConfigReader;
import utils.Ovito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class VaryWhiteY {
    private static final String CONFIG_PATH = "./src/main/java/Pool/config.json";

    public static void main(String[] args) throws InterruptedException {

        JsonConfigReader config = new JsonConfigReader(CONFIG_PATH);

        int nPositions = 20;
        double yMin = 42.0;
        double yMax = 56.0;

        ExecutorService executor = Executors.newFixedThreadPool(nPositions);

        List<PoolSystem> list = new ArrayList<>();

        for (double whiteY = yMin; whiteY < yMax; whiteY += (yMax - yMin) / nPositions) {

            PoolSystem system = new PoolSystem(config, whiteY, 8, Ovito.createFile("pool", "xyz"));
            list.add(system);

            Runnable worker = new ThreadAux(
                    whiteY,
                    system
            );
            executor.execute(worker);

        }

        Ovito.writeListToFIle(
                list.stream().map(PoolSystem::getFinalTime).collect(Collectors.toList()),
                Ovito.createFile("pool_mean_times", "txt"),
                true
        );

        Ovito.writeListToFIle(
                list.stream().map(PoolSystem::getFinalTime).collect(Collectors.toList()),
                Ovito.createFile("pool_mean_times", "txt"),
                true
        );

    }

}

class ThreadAux implements Runnable {
    private final PoolSystem system;
    private final double y;

    public ThreadAux(double y, PoolSystem system) {
        this.y = y;
        this.system = system;
    }

    @Override
    public void run() {
        long tiempoInicial = System.currentTimeMillis();

        system.run(100);

        long tiempoFinal = System.currentTimeMillis();
        long tiempoTotal = tiempoFinal - tiempoInicial;

        long segundos = tiempoTotal / 1000;
        long minutos = segundos / 60;
        segundos = segundos % 60;

        System.out.println("White Y: " + y + " Tiempo total de ejecución: " + minutos + " min " + segundos + " seg");
    }
}

