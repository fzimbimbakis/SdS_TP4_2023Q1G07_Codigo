package Pool.models;

import Pool.models.particle.Particle;

import java.util.*;

public class Grid {

    private static final Double MAX_X = 224.0;
    private static final Double MAX_Y = 112.0;
    private static final Double CELL_DIMENSION = 5.7435897435897435897435897435897;
    private final TreeMap<Double, TreeMap<Double, Cell>> treeMap = new TreeMap<>();

    public Grid() {

        this.createCellsAndFillDataStructures();

    }

    public List<Particle> getNeighbours(Double x, Double y){
        List<Particle> particles = new ArrayList<>();

        Map.Entry<Double, TreeMap<Double, Cell>> EntryX;
        Map.Entry<Double, Cell> EntryY;

        for (Double deltaX = -CELL_DIMENSION; deltaX <= CELL_DIMENSION; deltaX += CELL_DIMENSION){
            if(x + deltaX > MAX_X)
                continue;
            EntryX = treeMap.floorEntry(x + deltaX);
            if (EntryX == null) {
                continue;
            }
            for (Double deltaY = -CELL_DIMENSION; deltaY <= CELL_DIMENSION; deltaY += CELL_DIMENSION){
                if(y +deltaY > MAX_Y)
                    continue;
                EntryY = EntryX.getValue().floorEntry(y + deltaY);
                if (EntryY != null) {
                    particles.addAll(EntryY.getValue().getParticles());
                }
            }
        }
        return particles;
    }

    public void remove(Particle particle){
        particle.getCell().remove(particle);
        particle.setCell(null);
//        double x = Math.min(MAX_X, particle.getX());
//        x = Math.max(0, x);
//        double y = Math.min(MAX_Y, particle.getY());
//        y = Math.max(0, y);
//        Map.Entry<Double, TreeMap<Double, Cell>> EntryX = treeMap.floorEntry(x);
//        if (EntryX == null) {
//            return false;
//        }
//        Map.Entry<Double, Cell> EntryY = EntryX.getValue().floorEntry(y);
//        if (EntryY != null) {
//            return EntryY.getValue().remove(particle);
//        }
//        return false;
    }

    public void add(Particle particle){
        double x = Math.min(MAX_X, particle.getX());
        x = Math.max(0, x);
        double y = Math.min(MAX_Y, particle.getY());
        y = Math.max(0, y);
        Map.Entry<Double, TreeMap<Double, Cell>> EntryX = treeMap.floorEntry(x);
        if (EntryX == null) {
            throw new IllegalStateException();
        }
        Map.Entry<Double, Cell> EntryY = EntryX.getValue().floorEntry(y);
        if (EntryY != null) {
            EntryY.getValue().add(particle);
            particle.setCell(EntryY.getValue());
            return;
        }
        throw new IllegalStateException();
    }

    public  void addAll(List<Particle> particles){
        particles.forEach(this::add);
    }

    private void createCellsAndFillDataStructures(){
        for (double x = 0.0; x < MAX_X; x += CELL_DIMENSION){
            treeMap.put(x, new TreeMap<>());
            for (double y = 0.0; y < MAX_Y; y += CELL_DIMENSION) {
                treeMap.get(x).put(y, new Cell());
            }
        }
    }

}
