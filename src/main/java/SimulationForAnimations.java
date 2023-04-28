import utils.JsonConfigReader;
import utils.Ovito;

public class SimulationForAnimations {

    private static final String JSON_CONFIG_PATH = "./src/main/java/config.json";

    public static void main(String[] args) {

        // Read JSON
        JsonConfigReader config = new JsonConfigReader(JSON_CONFIG_PATH);
        config.setWhiteY(48.2222222224);

        // Creates files
        String file_path = Ovito.createFile("animation_times_", "txt");
        String total_time_path = Ovito.createFile("animation_total_time_", "txt");

        double totalTime = 0;
        ParticleCollisionSystem particleCollisionSystem = new ParticleCollisionSystem(config, true);

        particleCollisionSystem.run();

        totalTime += particleCollisionSystem.getFinalTime();
        Ovito.writeListToFIle(particleCollisionSystem.getEventTimes(), file_path, true);
        Ovito.writeToFIle(totalTime, total_time_path);
    }


}
