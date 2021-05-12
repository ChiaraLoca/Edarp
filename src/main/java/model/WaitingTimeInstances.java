package model;

public class WaitingTimeInstances {
    private Node curruentNode;
    private Node nextNode;
    private double wait;
    private Node lastEmptyLocationId;


    public WaitingTimeInstances(Node curruentNode, Node nextNode, double wait) {
        this.curruentNode = curruentNode;
        this.nextNode = nextNode;
        this.wait = wait;
    }

    public WaitingTimeInstances(Node curruentNode, double wait,Node lastEmptyLocationId) {
        this.curruentNode = curruentNode;
        this.wait = wait;
        this.lastEmptyLocationId = lastEmptyLocationId;
    }

    public Node getCurruentNode() {
        return curruentNode;
    }

    public void setCurruentNode(Node curruentNode) {
        this.curruentNode = curruentNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public double getWait() {
        return wait;
    }

    public void setWait(double wait) {
        this.wait = wait;
    }

    public Node getLastEmptyLocationId() {
        return lastEmptyLocationId;
    }

    public void setLastEmptyLocationId(Node lastEmptyLocationId) {
        this.lastEmptyLocationId = lastEmptyLocationId;
    }
}
