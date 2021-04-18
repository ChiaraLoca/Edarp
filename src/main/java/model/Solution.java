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


    // Decision variables:
    private int[][][] vehicleSeqStopAtLocations; // X: 1 if vehicle k sequentially stops at location i and j ∈ V, 0 otherwise X{K,V,V}
    private double[][] timeVehicleStartsAtLocation; // T: time at which vehicle k starts its service at location i ∈ V T{K,V};
    private double[][] loadOfVehicleAtLocation; // L: load of vehicle k at location i ∈ V L{K,V};
    private double[][] batteryLoadOfVehicleAtLocation; // B: battery load of vehicle k at location i ∈ V B{K,V}
    private double[][] chargingTimeOfVehicleAtStation; // E: charging time of vehicle k at charging station s ∈ S E{K,S}
    private double[] excessRideTimeOfPassenger; // R: excess ride-time of passenger i ∈ P  R{P};








    // Decision variables:
    private int[][][] vehicleSeqStopAtLocations; // X: 1 if vehicle k sequentially stops at location i and j ∈ V, 0 otherwise
    private double[][] timeVehicleStartsAtLocation; // T: time at which vehicle k starts its service at location i ∈ V
    private double[][] loadOfVehicleAtLocation; // L: load of vehicle k at location i ∈ V
    private double[][] batteryLoadOfVehicleAtLocation; // B: battery load of vehicle k at location i ∈ V
    private double[][] chargingTimeOfVehicleAtStation; // E: charging time of vehicle k at charging station s ∈ S
    private double[] excessRideTimeOfPassenger; // R: excess ride-time of passenger i ∈ P

    public Solution(Instance instance) {
        this.instance = instance;

        int V = instance.getNodes().size();
        int K = instance.getnVehicles();

        vehicleSeqStopAtLocations = new int[K][V][V];
        timeVehicleStartsAtLocation =  new double[K][V];
        loadOfVehicleAtLocation = new double [K][V];
        batteryLoadOfVehicleAtLocation = new double [K][V];
        chargingTimeOfVehicleAtStation = new double [K][instance.getChargingStationId().length];
        excessRideTimeOfPassenger = new double [instance.getnCustomers()];
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

    public double[] getExcessRideTimeOfPassenger() {
        return excessRideTimeOfPassenger;
    }
}
