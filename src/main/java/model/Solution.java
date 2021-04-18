package model;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    private Instance instance;
    private List<Mission> list; // K = {1, . . ., k}: set of available vehicles

    // Decision variables:
    private int[][][] vehicleSeqStopAtLocations; // X: 1 if vehicle k sequentially stops at location i and j ∈ V, 0 otherwise
    private double[][] timeVehicleStartsAtLocation; // T: time at which vehicle k starts its service at location i ∈ V
    private double[][] loadOfVehicleAtLocation; // L: load of vehicle k at location i ∈ V
    private double[][] batteryLoadOfVehicleAtLocation; // B: battery load of vehicle k at location i ∈ V
    private double[][] chargingTimeOfVehicleAtStation; // E: charging time of vehicle k at charging station s ∈ S
    private double[][] excessRideTimeOfPassenger; // R: excess ride-time of passenger i ∈ P

    public Solution(Instance instance) {
        this.instance = instance;
        this.list= new ArrayList<>();
    }

    public List<Mission> getList() {
        return list;
    }

    public int[][][] getVehicleSeqStopAtLocations() {
        return vehicleSeqStopAtLocations;
    }

    public double[][] getTimeVehicleStartsAtLocation() {
        return timeVehicleStartsAtLocation;
    }

    public double[][] getLoadOfVehicleAtLocation() {
        return loadOfVehicleAtLocation;
    }

    public double[][] getBatteryLoadOfVehicleAtLocation() {
        return batteryLoadOfVehicleAtLocation;
    }

    public double[][] getChargingTimeOfVehicleAtStation() {
        return chargingTimeOfVehicleAtStation;
    }

    public double[][] getExcessRideTimeOfPassenger() {
        return excessRideTimeOfPassenger;
    }
}
