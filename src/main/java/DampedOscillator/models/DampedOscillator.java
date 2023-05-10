package DampedOscillator.models;

import DampedOscillator.algorithms.Beeman;
import DampedOscillator.algorithms.GearPredictorCorrector;
import DampedOscillator.algorithms.Verlet;
import utils.Ovito;

import java.util.ArrayList;
import java.util.List;

public class DampedOscillator {

    private final double mass = 70;
    private final double k = 10000;
    private final double gamma = 100;
    private final double A = 1;
    private final double dt;
    private final Verlet verlet;
    private final Beeman beeman;
    private final GearPredictorCorrector gearPredictorCorrector;


    public DampedOscillator(double dt){

        double r = 1;
        verlet = new Verlet(mass, r, - (A * gamma)/(2*mass), dt, k, gamma);
        beeman = new Beeman(mass, r, - (A * gamma)/(2*mass), dt, k, gamma);
        gearPredictorCorrector = new GearPredictorCorrector(mass, r, - (A * gamma)/(2*mass), dt, k, gamma);
        this.dt = dt;

    }

    public void run(){
        List<String> beemanList = new ArrayList<>();
        List<String> verletList = new ArrayList<>();
        List<String> gearList = new ArrayList<>();
        List<String> solutionList = new ArrayList<>();

        OscillatorParticle verletParticle = verlet.getParticle();
        OscillatorParticle beemanParticle = beeman.getParticle();
        OscillatorParticle gearParticle = gearPredictorCorrector.getParticle();
        int tf = 5;
        for (double t = 0; t <= tf; t+=dt ) {
            beemanList.add(t + " " + beemanParticle.getX());
            beemanParticle = beeman.calculateNext();
            verletList.add(t + " " + verletParticle.getX());
            verletParticle = verlet.calculateNext();
            gearList.add(t + " " + gearParticle.getX());
            gearParticle = gearPredictorCorrector.calculateNext();
            solutionList.add(t + " " + A*Math.exp(-(gamma/(2*mass))*t)*Math.cos(Math.sqrt((k/mass) - (Math.pow(gamma,2)/(4*Math.pow(mass,2))))*t));

        }
        String beeman_path = Ovito.createFile("beeman", "txt");
        Ovito.writeListToFIle(beemanList, beeman_path, true);
        String verlet_path = Ovito.createFile("verlet", "txt");
        Ovito.writeListToFIle(verletList, verlet_path, true);
        String gear_path = Ovito.createFile("gear", "txt");
        Ovito.writeListToFIle(gearList, gear_path, true);
        String solution_path = Ovito.createFile("solution", "txt");
        Ovito.writeListToFIle(solutionList, solution_path, true);

    }

}
