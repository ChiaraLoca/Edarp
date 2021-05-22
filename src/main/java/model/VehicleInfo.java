package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public double getPossibleTimeToArriveToNextNode() {
        return possibleTimeToArriveToNextNode;
    }

    public double getTimeSpendAtCharging() {
        return timeSpendAtCharging;
    }

    public void setTimeSpendAtCharging(double timeSpendAtCharging) {
        this.timeSpendAtCharging = timeSpendAtCharging;
    }

    public double getPossibleBatteryLevel() {
        return possibleBatteryLevel;
    }

    public List<Node> getPossiblePassengerDestination() {
        return possiblePassengerDestination;
    }


    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
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

    public boolean isTimeOver() {
        return timeOver;
    }

    public double getTimeAvailableToCharge() {
        return timeAvailableToCharge;
    }

    public Node getLastTimeAtEmpty() {
        return lastTimeAtEmpty;
    }

    @Override
    public String toString() {
        return "VehicleInfo{" +
                "currentPosition=" + currentPosition +
                ", currentBatteryLevel=" + currentBatteryLevel +
                ", timeSpendAtCharging=" + timeSpendAtCharging +
                ", load=" + passengerDestination.size() +
                ", timeOfMission=" + timeOfMission +
                "}\n";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleInfo)) return false;
        VehicleInfo that = (VehicleInfo) o;
        return getVehicleId() == that.getVehicleId() &&
                Double.compare(that.getMaxBatteryCapacity(), getMaxBatteryCapacity()) == 0 &&
                getMaxLoad() == that.getMaxLoad() &&
                Double.compare(that.getCurrentBatteryLevel(), getCurrentBatteryLevel()) == 0 &&
                Double.compare(that.getTimeSpendAtCharging(), getTimeSpendAtCharging()) == 0 &&
                Double.compare(that.getTimeOfMission(), getTimeOfMission()) == 0 &&
                Double.compare(that.getTimeAvailableToCharge(), getTimeAvailableToCharge()) == 0 &&
                isTimeOver() == that.isTimeOver() &&
                Double.compare(that.getPossibleTimeToArriveToNextNode(), getPossibleTimeToArriveToNextNode()) == 0 &&
                Double.compare(that.getPossibleBatteryLevel(), getPossibleBatteryLevel()) == 0 &&
                Double.compare(that.getWaitingTime(), getWaitingTime()) == 0 &&
                Double.compare(that.getPossibleDistanceFromPossibleDestination(), getPossibleDistanceFromPossibleDestination()) == 0 &&
                Objects.equals(getArtificialOriginDepot(), that.getArtificialOriginDepot()) &&
                Objects.equals(getArtificialDestinationDepot(), that.getArtificialDestinationDepot()) &&
                Objects.equals(getCurrentPosition(), that.getCurrentPosition()) &&
                Objects.equals(getPassengerDestination(), that.getPassengerDestination()) &&
                Objects.equals(getLastTimeAtEmpty(), that.getLastTimeAtEmpty()) &&
                Objects.equals(getPossiblePassengerDestination(), that.getPossiblePassengerDestination());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVehicleId(), getArtificialOriginDepot(), getArtificialDestinationDepot(), getMaxBatteryCapacity(), getMaxLoad(), getCurrentPosition(), getCurrentBatteryLevel(), getTimeSpendAtCharging(), getPassengerDestination(), getTimeOfMission(), getTimeAvailableToCharge(), getLastTimeAtEmpty(), isTimeOver(), getPossibleTimeToArriveToNextNode(), getPossibleBatteryLevel(), getPossiblePassengerDestination(), getWaitingTime(), getPossibleDistanceFromPossibleDestination());
    }
}