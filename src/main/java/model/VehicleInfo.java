package model;

import java.util.ArrayList;
import java.util.List;

public class VehicleInfo{
    private final int vehicleId;
    private final Node artificialOriginDepot;
    private final Node artificialDestinationDepot;
    private final double maxBatteryCapacity;
    private final int maxLoad;


    private Node currentPosition;
    private double currentBatteryLevel;
    private double timeSpendAtCharging;
    private List<Node> passengerDestination; //contiene gli id delle dropoff location corrispondenti alle personoe a bordo
    private double timeOfMission;

    private double timeAvailableToCharge =0;
    private Node lastTimeAtEmpty =null;

    private boolean timeOver = false;


    private double possibleTimeToArriveToNextNode =Double.NaN;
    private double possibleBatteryLevel=0;
    private List<Node> possiblePassengerDestination;
    private double waitingTime=0;
    private double possibleDistanceFromPossibleDestination;

    public VehicleInfo(int vehicleId, Node artificialOriginDepot, Node artificialDestinationDepot, double maxBatteryCapacity,int maxLoad) {

        this.vehicleId = vehicleId;
        this.artificialOriginDepot = artificialOriginDepot;
        this.artificialDestinationDepot = artificialDestinationDepot;
        this.maxBatteryCapacity = maxBatteryCapacity;
        this.maxLoad = maxLoad;


        this.currentPosition = artificialOriginDepot;
        this.currentBatteryLevel = maxBatteryCapacity;
        this.timeSpendAtCharging = 0;
        this.passengerDestination = new ArrayList<>();
        this.timeOfMission=0;

        this.possibleTimeToArriveToNextNode =0;
        this.possibleBatteryLevel=0;
        this.possiblePassengerDestination = new ArrayList<>();
    }

    public VehicleInfo(VehicleInfo vi)
    {
        this.vehicleId = vi.getVehicleId();
        this.artificialOriginDepot = vi.getArtificialOriginDepot();
        this.artificialDestinationDepot = vi.getArtificialDestinationDepot();
        this.maxBatteryCapacity = vi.getMaxBatteryCapacity();
        this.maxLoad = vi.getMaxLoad();


        this.currentPosition =vi.getCurrentPosition();
        this.currentBatteryLevel =vi.getCurrentBatteryLevel();
        this.timeSpendAtCharging = vi.getTimeSpendAtCharging();
        this.passengerDestination = new ArrayList<>(vi.getPassengerDestination()); //contiene gli id delle dropoff location corrispondenti alle personoe a bordo
        this.timeOfMission = vi.getTimeOfMission();

        this.timeAvailableToCharge =vi.getTimeAvailableToCharge();
        this.lastTimeAtEmpty =vi.getLastTimeAtEmpty();

        this.timeOver = vi.isTimeOver();


        this.possibleTimeToArriveToNextNode = vi.getPossibleTimeToArriveToNextNode();
        this.possibleBatteryLevel=vi.getPossibleBatteryLevel();
        this.possiblePassengerDestination = new ArrayList<>(vi.getPossiblePassengerDestination());
        this.waitingTime=vi.getWaitingTime();
        this.possibleDistanceFromPossibleDestination = vi.getPossibleDistanceFromPossibleDestination();


    }

    public double getMissingCharge()
    {
        return maxBatteryCapacity-getCurrentBatteryLevel();
    }

    @Override
    public String toString() {
        return "VehicleInfo{" +
                "vehicleId=" + vehicleId +
                ", currentPosition=" + currentPosition.getId() +
                ", passengerDestination=" + passengerDestination.size() +
                ", timeOfMission=" + timeOfMission +
                ", timeOver=" + timeOver +
                '}';
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public Node getArtificialOriginDepot() {
        return artificialOriginDepot;
    }

    public Node getArtificialDestinationDepot() {
        return artificialDestinationDepot;
    }

    public double getMaxBatteryCapacity() {
        return maxBatteryCapacity;
    }

    public Node getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Node currentPosition) {
        this.currentPosition = currentPosition;
    }

    public double getCurrentBatteryLevel() {
        return currentBatteryLevel;
    }

    public void setCurrentBatteryLevel(double currentBatteryLevel) {
        this.currentBatteryLevel = currentBatteryLevel;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public List<Node> getPassengerDestination() {
        return passengerDestination;
    }

    public void setPassengerDestination(List<Node> passengerDestination) {
        this.passengerDestination = passengerDestination;
    }

    public double getPossibleTimeToArriveToNextNode() {
        return possibleTimeToArriveToNextNode;
    }

    public void setPossibleTimeToArriveToNextNode(double possibleTimeToArriveToNextNode) {
        this.possibleTimeToArriveToNextNode = possibleTimeToArriveToNextNode;
    }
    public void addTimeToArriveToNextNode(double timeToArriveToNextNode) {
        this.possibleTimeToArriveToNextNode += timeToArriveToNextNode;
    }

    public double getTimeSpendAtCharging() {
        return timeSpendAtCharging;
    }

    public void setTimeSpendAtCharging(double timeSpendAtCharging) {
        this.timeSpendAtCharging = timeSpendAtCharging;
    }

    public void resetCurrentBatteryLevel() {
        possibleBatteryLevel = maxBatteryCapacity;
    }

    public void decreseCurrentBatteryLevel(double d) {
        possibleBatteryLevel = currentBatteryLevel-d;
    }

    public double getPossibleBatteryLevel() {
        return possibleBatteryLevel;
    }

    public void setPossibleBatteryLevel(double possibleBatteryLevel) {
        this.possibleBatteryLevel = possibleBatteryLevel;
    }

    public List<Node> getPossiblePassengerDestination() {
        return possiblePassengerDestination;
    }

    public void setPossiblePassengerDestination(List<Node> possiblePassengerDestination) {
        this.possiblePassengerDestination = possiblePassengerDestination;
    }
    public void update(Node nextNode)
    {
        /*setCurrentPosition(nextNode);
        passengerDestination.clear();
        passengerDestination.addAll(possiblePassengerDestination);



        currentBatteryLevel= possibleBatteryLevel;
        waitingTime =0;
        timeOfMission= possibleTimeToArriveToNextNode;*/
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void addWaitingTime(double waitingTime) {
        this.waitingTime += waitingTime;
    }

    public double getTimeOfMission() {
        return timeOfMission;
    }

    public void setTimeOfMission(double timeOfMission) {
        this.timeOfMission = timeOfMission;
    }

    public double getPossibleDistanceFromPossibleDestination() {
        return possibleDistanceFromPossibleDestination;
    }

    public void setPossibleDistanceFromPossibleDestination(double possibleDistanceFromPossibleDestination) {
        this.possibleDistanceFromPossibleDestination = possibleDistanceFromPossibleDestination;
    }

    public boolean isFullyCharged() {
        if(currentBatteryLevel<maxBatteryCapacity)
            return false;
        return true;
    }

    public boolean isTimeOver() {
        return timeOver;
    }

    public void setTimeOver(boolean timeOver) {
        System.out.println(getVehicleId()+ "time is over");
        this.timeOver = timeOver;
    }

    public double getTimeAvailableToCharge() {
        return timeAvailableToCharge;
    }

    public void setTimeAvailableToCharge(double timeAvailableToCharge) {
        this.timeAvailableToCharge = timeAvailableToCharge;
    }

    public Node getLastTimeAtEmpty() {
        return lastTimeAtEmpty;
    }

    public void setLastTimeAtEmpty(Node lastTimeAtEmpty) {
        this.lastTimeAtEmpty = lastTimeAtEmpty;
    }
}