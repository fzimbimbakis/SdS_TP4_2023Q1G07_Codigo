import utils.JsonConfigReader;
import utils.Ovito;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AlterWhiteY {
    private static final String JSON_CONFIG_PATH = "./src/main/java/config.json";
    private static final double step = (56.0 - 42.0) / 9.0;
    private static final DecimalFormat df = new DecimalFormat("##.##");

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Ejecutar las tareas
        for (int i = 0; i < 10; i++) {
            executor.execute(new ThreadAux(step * -i));
        }

        // Apagar el ExecutorService después de que se completen todas las tareas
        executor.shutdown();

        try {
            // Esperar hasta que todas las tareas se completen o hasta que transcurra 1 minuto
            if (!executor.awaitTermination(60, TimeUnit.MINUTES))
                System.out.println("TIMEOUT in awaitTermination");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static final int times = 10000;

    static class ThreadAux implements Runnable {
        private final double deltaY;

        public ThreadAux(double deltaY) {
            this.deltaY = deltaY;
        }

        @Override
        public void run() {
            JsonConfigReader config = new JsonConfigReader(JSON_CONFIG_PATH);
            config.setWhiteY(config.getWhiteY() + this.deltaY);
            String file_path = Ovito.createFile("times_" + df.format(config.getWhiteY()) + "_", "txt");
            String total_time_path = Ovito.createFile("total_time_" + df.format(config.getWhiteY()) + "_", "txt");
            String frequency_path = Ovito.createFile("frequency_" + df.format(config.getWhiteY()) + "_", "txt");
            List<Double> totalTimes = new ArrayList<>();
            List<Double> frequencies = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                ParticleCollisionSystem particleCollisionSystem = new ParticleCollisionSystem(config);

                particleCollisionSystem.run();

                totalTimes.add(particleCollisionSystem.getFinalTime());

                frequencies.add(((double) particleCollisionSystem.getEventTimes().size()) / particleCollisionSystem.getFinalTime());
                Ovito.writeListToFIle(particleCollisionSystem.getEventTimes(), file_path, i == times - 1);
            }
            Ovito.writeListToFIle(totalTimes, total_time_path, true);
            Ovito.writeListToFIle(frequencies, frequency_path, true);
        }
    }
}
