package util;

import model.Node;

import java.util.Objects;

public class PairOfNodes
{
    private Node pickup;
    private Node dropoff;

    public PairOfNodes() {
    }

    public PairOfNodes(Node pickup, Node dropoff) {
        this.pickup = pickup;
        this.dropoff = dropoff;
    }

    public Node getPickup() {
        return pickup;
    }

    public void setPickup(Node pickup) {
        this.pickup = pickup;
    }

    public Node getDropoff() {
        return dropoff;
    }

    public void setDropoff(Node dropoff) {
        this.dropoff = dropoff;
    }






    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairOfNodes that = (PairOfNodes) o;
        return Objects.equals(pickup, that.pickup) &&
                Objects.equals(dropoff, that.dropoff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pickup, dropoff);
    }

    @Override
    public String toString() {
        return "PairOfNodes{" +
                "pickup=" + pickup +
                ", dropoff=" + dropoff +
                '}';
    }
}
