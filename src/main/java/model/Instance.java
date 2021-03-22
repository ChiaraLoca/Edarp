package model;

import java.util.List;

public class Instance {
    private String name;
    private int nVehicles;
    private int nCustomers;
    private double minBatteryRatioLvl;

    private int nOriginDepots;
    private int nDestinationDepots;
    private int nStations;
    private int nReplications;
    private int timeHorizon;
    private List<Node> nodes;
    private final int[] commonOriginDepotId = new int[nOriginDepots];
    private final int[] commonDestinationDepotId = new int[nDestinationDepots];
    private final int[] artificialOriginDepotId= new int[nVehicles]; //TODO non sappiamo da dove arriva la lunghezza di questo
    private final int[] chargingStationId = new int[nStations];
    private final int[] userMaxRideTime = new int[nCustomers];
    private final int[] vehicleCapacity = new int[nVehicles];
    private final int[] vehicleInitBatteryInventory = new int[nVehicles];
    private final double[] vehicleBatteryCapacity = new double[nVehicles];
    private final double[] minEndBatteryRatioLvl = new double[nVehicles];
    private final double[] stationRechargingRate = new double[nStations];
    private double vehicleDischargingRate;
    private final double[] weightFactor = new double[2]; //TODO non abbiamo idea di che cazzo sia
    //Todo original travel time non compare mai


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

    public int getTimeHorizon() {
        return timeHorizon;
    }

    public void setTimeHorizon(int timeHorizon) {
        this.timeHorizon = timeHorizon;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public int[] getCommonOriginDepotId() {
        return commonOriginDepotId;
    }

    public int[] getCommonDestinationDepotId() {
        return commonDestinationDepotId;
    }

    public int[] getArtificialOriginDepotId() {
        return artificialOriginDepotId;
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

    public int[] getVehicleInitBatteryInventory() {
        return vehicleInitBatteryInventory;
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

    public double[] getWeightFactor() {
        return weightFactor;
    }

    public Instance(String[] firstLine) {
        this.nVehicles = Integer.parseInt(firstLine[0]);
        this.nCustomers = Integer.parseInt(firstLine[1]);
        this.nOriginDepots = Integer.parseInt(firstLine[2]);
        this.nDestinationDepots = Integer.parseInt(firstLine[3]);
        this.nStations = Integer.parseInt(firstLine[4]);
        this.nReplications = Integer.parseInt(firstLine[5]);
        this.timeHorizon = Integer.parseInt(firstLine[6]);
    }

    public Instance(String name, int nVehicles, int nCustomers, double minBatteryRatioLvl, int nOriginDepots, int nDestinationDepots, int nStations, int nReplications, int timeHorizon, List<Node> nodes, double vehicleDischargingRate) {
        this.name = name;
        this.nVehicles = nVehicles;
        this.nCustomers = nCustomers;
        this.minBatteryRatioLvl = minBatteryRatioLvl;
        this.nOriginDepots = nOriginDepots;
        this.nDestinationDepots = nDestinationDepots;
        this.nStations = nStations;
        this.nReplications = nReplications;
        this.timeHorizon = timeHorizon;
        this.nodes = nodes;
        this.vehicleDischargingRate = vehicleDischargingRate;
    }
}
