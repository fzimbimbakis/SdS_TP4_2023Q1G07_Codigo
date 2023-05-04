import models.DampedOscillator;

public class MainDampedOscillator {

    public static void main(String[] args) {

        double[] dts = {0.1, 0.01, 0.001, 0.0001, 0.00001};

        DampedOscillator dampedOscillator;
        for (int i = 0; i < dts.length; i++) {
            dampedOscillator = new DampedOscillator(dts[i]);
            dampedOscillator.run();
        }
    }
}
