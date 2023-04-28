package models;

public class Collision implements Comparable<Collision>{

    private final Particle A;
    private int collisionsA;
    private final Particle B;
    private int collisionsB;

    private final Double maxX;
    private final Double maxY;

    public Collision(Particle A, Particle B, Double maxX, Double maxY){

        if(A == null && B == null)
            throw new IllegalArgumentException("Both event particles are null.");

        this.A = A;
        if(A != null)
            this.collisionsA = A.getCollisionCount();

        this.B = B;
        if(B != null)
            this.collisionsB = B.getCollisionCount();

        this.maxX = maxX;
        this.maxY = maxY;

    }

    public double getT() {
        if(B == null)
            return A.collidesX(maxX);
        else if(A == null)
            return B.collidesY(maxY);
        else return A.collides(B);
    }

    public Particle getA() {
        return A;
    }

    public Particle getB() {
        return B;
    }

    public boolean wasSuperveningEvent(){
        if(A == null)
            return this.collisionsB != B.getCollisionCount();
        if(B == null)
            return this.collisionsA != A.getCollisionCount();
        return this.collisionsA != A.getCollisionCount() || this.collisionsB != B.getCollisionCount();
    }

    public void execute(){
        if(A == null)
            B.bounceY();
        else if(B == null)
            A.bounceX();
        else A.bounce(B);
    }

    @Override
    public int compareTo(Collision collision) {
        return Double.compare(this.getT(), collision.getT());
    }
}
