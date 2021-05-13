package model;

public class WaitingInfo {
    private double waitTime;
    private Node waitLocation;

    public WaitingInfo(double waitTime, Node waitLocation, double battery) {
        this.waitTime = waitTime;
        this.waitLocation = waitLocation;
        this.battery = battery;
    }

    private double battery;
}
