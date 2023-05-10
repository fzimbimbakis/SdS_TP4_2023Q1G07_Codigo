package Pool.models;

import Pool.models.particle.Particle;

import java.util.*;

public class Grid {

    private static final Double MAX_X = 224.0;
    private static final Double MAX_Y = 112.0;
    private static final Double CELL_DIMENSION = 7.0;
    private final TreeMap<Double, TreeMap<Double, Cell>> treeMap = new TreeMap<>();

    public Grid() {

        this.createCellsAndFillDataStructures();

    }

    public List<Particle> getNeighbours(Double x, Double y){
        List<Particle> particles = new ArrayList<>();

        Map.Entry<Double, TreeMap<Double, Cell>> EntryX;
        Map.Entry<Double, Cell> EntryY;

        for (Double deltaX = -CELL_DIMENSION; deltaX <= CELL_DIMENSION; deltaX += CELL_DIMENSION){
            EntryX = treeMap.floorEntry(x + deltaX);
            if (EntryX == null) {
                continue;
            }
            for (Double deltaY = -CELL_DIMENSION; deltaY <= CELL_DIMENSION; deltaY += CELL_DIMENSION){
                EntryY = EntryX.getValue().floorEntry(y + deltaY);
                if (EntryY != null) {
                    particles.addAll(EntryY.getValue().getParticles());
                }
            }
        }
        return particles;
    }

    public boolean remove(Particle particle){
        Map.Entry<Double, TreeMap<Double, Cell>> EntryX = treeMap.floorEntry(particle.getX());
        if (EntryX == null) {
            return false;
        }
        Map.Entry<Double, Cell> EntryY = EntryX.getValue().floorEntry(particle.getY());
        if (EntryY != null) {
            return EntryY.getValue().remove(particle);
        }
        return false;
    }

    public void add(Particle particle){
        Map.Entry<Double, TreeMap<Double, Cell>> EntryX = treeMap.floorEntry(particle.getX());
        if (EntryX == null) {
            throw new IllegalStateException();
        }
        Map.Entry<Double, Cell> EntryY = EntryX.getValue().floorEntry(particle.getY());
        if (EntryY != null) {
            EntryY.getValue().add(particle);
        }
        throw new IllegalStateException();
    }

    public  void addAll(List<Particle> particles){
        particles.forEach(this::add);
    }

    private void createCellsAndFillDataStructures(){
        for (Double x = 0.0; x < MAX_X; x += CELL_DIMENSION){
            treeMap.put(x, new TreeMap<>());
            for (Double y = 0.0; y < MAX_Y; y += CELL_DIMENSION) {
                Cell newCell = new Cell(
                        new Pair<>(x, x + CELL_DIMENSION),
                        new Pair<>(y, y + CELL_DIMENSION)
                );
                treeMap.get(x).put(y, newCell);
            }
        }
    }

}