package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Instance {

    private String title;
    private String name;
    private int nVehicles;
    private int nCustomers;
    private double minBatteryRatioLvl; // r: final minimum battery level ratio

    private int nOriginDepots;
    private int nDestinationDepots;
    private int nStations;
    private int nReplications;
    private double timeHorizon;
    private List<Node> nodes;
    private final int[] commonOriginDepotId;
    private final int[] commonDestinationDepotId;
    private final int[] artificialOriginDepotId; // O: set of origin depots for vehicles k ∈ K, the origin of vehicle k is denoted by ok
    private final int[] artificialDestinationDepotId;
    private final int[] chargingStationId; // S: set of all charging stations
    private final int[] allAvailableDestinationDepotsId; // F: set of all available destination depots
    private final int[] userMaxRideTime;
    private final int[] vehicleCapacity;
    private final double[] vehicleInitBatteryInventory; // Bk0: initial battery capacity of vehicle k ∈ K
    private final double[] vehicleBatteryCapacity; // Q: effective battery capacity
    private final double[] minEndBatteryRatioLvl;
    private final double[] stationRechargingRate;
    private double vehicleDischargingRate;
    private final double[] weightFactor;
    private double[][] travelTime; // t: travel time from location i ∈ V to location j ∈ V
    private final double[] rechargeRate; // αs: recharge rate at charging facility s ∈ S TODO: va bene il tipo?
    private final double[][] batteryConsumption; // βi,j: battery consumption between nodes i, j ∈ V
    private final int[] pickupLocationsId; // P = {1,...,n}: set of pickup locations
    private final int[] dropoffLocationsId; // D = {n + 1,...,2n}: set of dropoff locations
    private  ArrayList<Node> pickupAndDropoffLocations; // N = P ∪ D: set of pickup and dropoff locations
    private final double[][] m; // Mi,j = max{0, depi + di + ti,j − arrj}
    private final double[][] g; // Mi,j = max{0, depi + di + ti,j − arrj}
    private final int[] allPossibleLocationsId; // V = N ∪ O ∪ F ∪ S: set
    private ArrayList<Node> chargingStationNodes;


    public ArrayList<Node> getChargingStationNodes() {
        return chargingStationNodes;
    }

    public void setChargingStationNodes(ArrayList<Node> chargingStationNodes) {
        this.chargingStationNodes = chargingStationNodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getnVehicles() {
        return nVehicles;
    }

    public void setnVehicles(int nVehicles) {
        this.nVehicles = nVehicles;
    }

    public int getnCustomers() {
        return nCustomers;
    }

    public void setnCustomers(int nCustomers) {
        this.nCustomers = nCustomers;
    }

    public double getMinBatteryRatioLvl() {
        return minBatteryRatioLvl;
    }

    public void setMinBatteryRatioLvl(double minBatteryRatioLvl) {
        this.minBatteryRatioLvl = minBatteryRatioLvl;
    }

    public int getnOriginDepots() {
        return nOriginDepots;
    }

    public void setnOriginDepots(int nOriginDepots) {
        this.nOriginDepots = nOriginDepots;
    }

    public int getnDestinationDepots() {
        return nDestinationDepots;
    }

    public void setnDestinationDepots(int nDestinationDepots) {
        this.nDestinationDepots = nDestinationDepots;
    }

    public int getnStations() {
        return nStations;
    }

    public void setnStations(int nStations) {
        this.nStations = nStations;
    }

    public int getnReplications() {
        return nReplications;
    }

    public void setnReplications(int nReplications) {
        this.nReplications = nReplications;
    }

    public double getTimeHorizon() {
        return timeHorizon;
    }

    public void setTimeHorizon(double timeHorizon) {
        this.timeHorizon = timeHorizon;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {this.nodes= nodes; }

    public int[] getCommonOriginDepotId() {
        return commonOriginDepotId;
    }

    public int[] getCommonDestinationDepotId() {
        return commonDestinationDepotId;
    }

    public int[] getArtificialOriginDepotId() {
        return artificialOriginDepotId;
    }

    public int[] getArtificialDestinationDepotId() {
        return artificialDestinationDepotId;
    }

    public int[] getAllAvailableDestinationDepotsId() {
        return allAvailableDestinationDepotsId;
    }

    public int[] getChargingStationId() {
        return chargingStationId;
    }

    public int[] getUserMaxRideTime() {
        return userMaxRideTime;
    }

    public int[] getVehicleCapacity() {
        return vehicleCapacity;
    }

    public double[] getVehicleInitBatteryInventory() {
        return vehicleInitBatteryInventory;
    }

    public double[] getRechargeRate() {
        return rechargeRate;
    }

    public double[] getVehicleBatteryCapacity() {
        return vehicleBatteryCapacity;
    }

    public double[] getMinEndBatteryRatioLvl() {
        return minEndBatteryRatioLvl;
    }

    public double[] getStationRechargingRate() {
        return stationRechargingRate;
    }

    public double getVehicleDischargingRate() {
        return vehicleDischargingRate;
    }

    public void setVehicleDischargingRate(double vehicleDischargingRate) {
        this.vehicleDischargingRate = vehicleDischargingRate;
    }

    public double[][] getBatteryConsumption() {
        return batteryConsumption;
    }

    public double[] getWeightFactor() {
        return weightFactor;
    }

    public double[][] getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double[][] travelTime) {
        this.travelTime = travelTime;
    }

    public int[] getPickupLocationsId() {
        return pickupLocationsId;
    }

    public int[] getDropoffLocationsId() {
        return dropoffLocationsId;
    }

    public ArrayList<Node> getPickupAndDropoffLocations() {
        return pickupAndDropoffLocations;
    }

    public void setPickupAndDropoffLocations(ArrayList<Node> pickupAndDropoffLocations) {
        this.pickupAndDropoffLocations = pickupAndDropoffLocations;
    }

    public int[] getAllPossibleLocationsId() {
        return allPossibleLocationsId;
    }

    public double[][] getG() {
        return g;
    }


    public double[][] getM() {
        return m;
    }

    public Instance(String title, String name, int nVehicles, int nCustomers, int nOriginDepots, int nDestinationDepots, int nStations, int nReplication) {
        this.title = title;
        this.name = name;
        this.nVehicles = nVehicles;
        this.nCustomers = nCustomers;
        this.nOriginDepots = nOriginDepots;
        this.nDestinationDepots = nDestinationDepots;
        this.nStations = nStations;
        this.nReplications = nReplications;
        this.pickupAndDropoffLocations=new ArrayList<>();


        commonOriginDepotId = new int[nOriginDepots];
        commonDestinationDepotId = new int[nDestinationDepots];
        artificialOriginDepotId= new int[nVehicles];
        if(name.equals("a"))
            artificialDestinationDepotId= new int[nVehicles];
        else
            artificialDestinationDepotId= new int[5];
        this.allAvailableDestinationDepotsId = new int[getCommonDestinationDepotId().length+getArtificialDestinationDepotId().length];
        System.arraycopy(getCommonDestinationDepotId(),0,allAvailableDestinationDepotsId,0,getCommonDestinationDepotId().length);
        System.arraycopy(getArtificialDestinationDepotId(),0,allAvailableDestinationDepotsId, getCommonDestinationDepotId().length,getArtificialDestinationDepotId().length);

        chargingStationId = new int[nStations];
        userMaxRideTime = new int[nCustomers];
        vehicleCapacity = new int[nVehicles];
        vehicleInitBatteryInventory = new double[nVehicles];
        vehicleBatteryCapacity = new double[nVehicles];
        minEndBatteryRatioLvl = new double[nVehicles];
        stationRechargingRate = new double[nStations];
        this.travelTime = null;
        weightFactor = new double[2];

        this.pickupLocationsId=new int[20]; // TODO: fix size and populate
        this.dropoffLocationsId=new int[20]; // TODO: fix size and populate
        this.pickupAndDropoffLocations=new ArrayList<>();
        this.rechargeRate=new double[30]; // TODO: fix size and populate
        this.allPossibleLocationsId=new int[50]; // TODO: fix size and populate
        this.rechargeRate=new double[30]; TODO: // TODO: fix size and populate
        this.batteryConsumption=new double[20][5]; // TODO: fix size and populate
        this.m=new double[5][5]; // TODO: fix size and populate
        this.g=new double[5][5]; // TODO: fix size and populate
        /*public double[][] getBatteryConsumption() {
        return batteryConsumption;
    }

    public void setBatteryConsumption(double[][] batteryConsumption) {
        this.batteryConsumption = batteryConsumption;
    }

         */
    }

    private String printMatrix(double[][] m){
        StringBuilder stringBuilder= new StringBuilder();
        if(m==null){
            stringBuilder.append("null");
            return stringBuilder.toString();
        }

        for (double[] doubles : m) {
            for (int j = 0; j < m[0].length; j++) {
                stringBuilder.append(doubles[j]).append("\t");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }



    @Override
    public String toString() {
        return "Instance{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", nVehicles=" + nVehicles +
                ", nCustomers=" + nCustomers +
                ", minBatteryRatioLvl=" + minBatteryRatioLvl +
                ", nOriginDepots=" + nOriginDepots +
                ", nDestinationDepots=" + nDestinationDepots +
                ", nStations=" + nStations +
                ", nReplications=" + nReplications +
                ", timeHorizon=" + timeHorizon +
                ", nodes=" + nodes +
                ", commonOriginDepotId=" + Arrays.toString(commonOriginDepotId) +
                ", commonDestinationDepotId=" + Arrays.toString(commonDestinationDepotId) +
                ", artificialOriginDepotId=" + Arrays.toString(artificialOriginDepotId) +
                ", artificialDestinationDepotId=" + Arrays.toString(artificialDestinationDepotId) +
                ", chargingStationId=" + Arrays.toString(chargingStationId) +
                ", userMaxRideTime=" + Arrays.toString(userMaxRideTime) +
                ", vehicleCapacity=" + Arrays.toString(vehicleCapacity) +
                ", vehicleInitBatteryInventory=" + Arrays.toString(vehicleInitBatteryInventory) +
                ", vehicleBatteryCapacity=" + Arrays.toString(vehicleBatteryCapacity) +
                ", minEndBatteryRatioLvl=" + Arrays.toString(minEndBatteryRatioLvl) +
                ", stationRechargingRate=" + Arrays.toString(stationRechargingRate) +
                ", vehicleDischargingRate=" + vehicleDischargingRate +
                ", weightFactor=" + Arrays.toString(weightFactor) +
                ", travelTime=\n" + printMatrix(travelTime) +
                '}';
    }
}


