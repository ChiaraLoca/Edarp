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
    private List<Mission> missions;

    // Decision variables:
    private int[][][] vehicleSeqStopAtLocations; // X: 1 if vehicle k sequentially stops at location i and j ∈ V, 0 otherwise
    private double[][] timeVehicleStartsAtLocation; // T: time at which vehicle k starts its service at location i ∈ V
    private double[][] loadOfVehicleAtLocation; // L: load of vehicle k at location i ∈ V
    private double[][] batteryLoadOfVehicleAtLocation; // B: battery load of vehicle k at location i ∈ V
    private double[][] chargingTimeOfVehicleAtStation; // E: charging time of vehicle k at charging station s ∈ S
    private double[][] excessRideTimeOfPassenger; // R: excess ride-time of passenger i ∈ P

    public Solution(Instance instance) {
        this.instance = instance;
        this.missions= new ArrayList<>();
    }

    public List<Mission> getMissions() {
        return missions;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("SOLUZIONE: "+instance.getTitle()+"\n");
        for(Mission m:missions)
        {
            str.append(m.toString());
        }
        return str.toString();
    }
}
