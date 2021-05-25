package model;

public class Solution {

    private Instance instance;

    // Decision variables:
    public int[][][] vehicleSeqStopAtLocations;         // X: 1 if vehicle k sequentially stops at location i and j ∈ V, 0 otherwise X{K,V,V}
    public double[][] timeVehicleStartsAtLocation;      // T: time at which vehicle k starts its service at location i ∈ V T{K,V};
    public double[][] loadOfVehicleAtLocation;          // L: load of vehicle k at location i ∈ V L{K,V};
    public double[][] batteryLoadOfVehicleAtLocation;   // B: battery load of vehicle k at location i ∈ V B{K,V}
    public double[][] chargingTimeOfVehicleAtStation;   // E: charging time of vehicle k at charging station s ∈ S E{K,S}
    public double[] excessRideTimeOfPassenger;          // R: excess ride-time of passenger i ∈ P  R{P};

    private double score;






    private String constraint;





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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

}
