package model;

public class WaitingInfo {
    private double waitTime;
    private Node waitLocation;

    public WaitingInfo(double waitTime, Node waitLocation, double battery) {
        this.waitTime = waitTime;
        this.waitLocation = waitLocation;
        this.battery = battery;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public Node getWaitLocation() {
        return waitLocation;
    }

    public void setWaitLocation(Node waitLocation) {
        this.waitLocation = waitLocation;
    }

    public double getBattery() {
        return battery;
    }

    @Override
    public String toString() {
        return "WaitingInfo{" +
                "waitTime=" + waitTime +
                ", waitLocation=" + waitLocation +
                ", battery=" + battery +
                '}';
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    private double battery;
}
