package model;

public class StepInfo {
    private Node location;
    private double time;
    private int load;
    private double batteryLevel;
    private double chargingTime=0;

    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public double getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(double chargingTime) {
        this.chargingTime = chargingTime;
    }

    public StepInfo(Node location, double time, int load, double batteryLevel) {
        this.location = location;
        this.time = time;
        this.load = load;
        this.batteryLevel = batteryLevel;
    }
}
