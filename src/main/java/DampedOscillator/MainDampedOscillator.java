package DampedOscillator;

import DampedOscillator.models.DampedOscillator;

public class MainDampedOscillator {

    public static void main(String[] args) {

        double[] dts = {0.01, 0.001, 0.0001, 0.00001};

        DampedOscillator dampedOscillator;
        for (double dt : dts) {
            dampedOscillator = new DampedOscillator(dt);
            dampedOscillator.run();
        }
    }
}
