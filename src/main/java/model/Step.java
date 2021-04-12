package model;

public class Step{
    private Node startingNode;//i
    private Node arrivingNodo;//j
    private double departureTime;//T[i]
    private double arrivalTime;//T[j]
    private double startingNodeArrivingTime;//arr[i]
    private double startingNodeDepartureTime;//dep[i]
    private double arrivingNodeArrivingTime;//arr[j]
    private double arrivingNodeDepartureTime;//dep[j]
    private double travelTime;//t[i,j]
    private double batteryLevel;//i
    private double chargingTime;//i

    public Step() {
    }

    public Step(Node startingNode, Node arrivingNodo) {
        this.startingNode = startingNode;
        this.arrivingNodo = arrivingNodo;
    }

    public Node getStartingNode() {
        return startingNode;
    }

    public void setStartingNode(Node startingNode) {
        this.startingNode = startingNode;
    }

    public Node getArrivingNodo() {
        return arrivingNodo;
    }

    public void setArrivingNodo(Node arrivingNodo) {
        this.arrivingNodo = arrivingNodo;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getStartingNodeArrivingTime() {
        return startingNodeArrivingTime;
    }

    public void setStartingNodeArrivingTime(double startingNodeArrivingTime) {
        this.startingNodeArrivingTime = startingNodeArrivingTime;
    }

    public double getStartingNodeDepartureTime() {
        return startingNodeDepartureTime;
    }

    public void setStartingNodeDepartureTime(double startingNodeDepartureTime) {
        this.startingNodeDepartureTime = startingNodeDepartureTime;
    }

    public double getArrivingNodeArrivingTime() {
        return arrivingNodeArrivingTime;
    }

    public void setArrivingNodeArrivingTime(double arrivingNodeArrivingTime) {
        this.arrivingNodeArrivingTime = arrivingNodeArrivingTime;
    }

    public double getArrivingNodeDepartureTime() {
        return arrivingNodeDepartureTime;
    }

    public void setArrivingNodeDepartureTime(double arrivingNodeDepartureTime) {
        this.arrivingNodeDepartureTime = arrivingNodeDepartureTime;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
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

    @Override
    public String toString() {
        return "" + startingNode.getId() +
                " - " + arrivingNodo.getId()+
                "( "+batteryLevel+" )\t";
    }
}
