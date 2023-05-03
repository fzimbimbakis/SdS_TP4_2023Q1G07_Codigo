import models.DampedOscillator;

public class MainDampedOscillator {

    public static void main(String[] args) {
        DampedOscillator dampedOscillator = new DampedOscillator(0.01);

        dampedOscillator.run();
    }
}
