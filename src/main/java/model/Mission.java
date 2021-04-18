package model;

import java.util.ArrayList;
import java.util.List;

public class Mission {
    private List<Step> steps = new ArrayList<>();
    private int vehicleId;
    private int artificialOriginDepotId;
    private int artificialDestinationDepotId;

    private int currentPositionId;

    private double currentCharge;
    private double expetedConsumption;
    private NodeType nextNodeType;
    private NodeType lastNodeType;

    private double timeToArriveToNextNode;

    public List<Step> getSteps() {
        return steps;
    }

    public Mission(int vehicleId, int artificialOriginDepotId, int artificialDestinationDepotId,double currentCharge) {
        this.vehicleId = vehicleId;
        this.artificialOriginDepotId = artificialOriginDepotId;
        this.artificialDestinationDepotId = artificialDestinationDepotId;
        this.currentPositionId=artificialOriginDepotId;
        this.nextNodeType = NodeType.PICKUP;
        this.lastNodeType = NodeType.NONE;
        this.currentCharge = currentCharge;
    }
    public void update(int currentPositionId)
    {

        if(nextNodeType.equals(NodeType.PICKUP)){
            nextNodeType = NodeType.DROPOFF;}
        else if(nextNodeType.equals(NodeType.DROPOFF)){
            nextNodeType = NodeType.PICKUP;}
        else if(nextNodeType.equals(NodeType.CHARGE)){
           if(steps.get(steps.size()-1).getStartingNode().getNodeType().equals(NodeType.PICKUP))
                nextNodeType = NodeType.DROPOFF;
           else if(steps.get(steps.size()-1).getStartingNode().getNodeType().equals(NodeType.DROPOFF))
               nextNodeType = NodeType.PICKUP;
        }
        this.currentPositionId = currentPositionId;
        this.currentCharge -=expetedConsumption;
    }

    public double getTimeToArriveToNextNode() {
        return timeToArriveToNextNode;
    }

    public void setTimeToArriveToNextNode(double timeToArriveToNextNode) {
        this.timeToArriveToNextNode = timeToArriveToNextNode;
    }

    public double getCurrentCharge() {
        return currentCharge;
    }

    public void setCurrentCharge(double currentCharge) {
        this.currentCharge = currentCharge;
    }

    public int getCurrentPositionId() {
        return currentPositionId;
    }

    public void setCurrentPositionId(int currentPositionId) {
        this.currentPositionId = currentPositionId;
    }

    public NodeType getNextNodeType() {
        return nextNodeType;
    }

    public void setNextNodeType(NodeType nextNodeType) {
        this.nextNodeType = nextNodeType;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getArtificialOriginDepotId() {
        return artificialOriginDepotId;
    }

    public void setArtificialOriginDepotId(int artificialOriginDepotId) {
        this.artificialOriginDepotId = artificialOriginDepotId;
    }

    public int getArtificialDestinationDepotId() {
        return artificialDestinationDepotId;
    }

    public void setArtificialDestinationDepotId(int artificialDestinationDepotId) {
        this.artificialDestinationDepotId = artificialDestinationDepotId;
    }

    public double getExpetedConsumption() {
        return expetedConsumption;
    }

    public void setExpetedConsumption(double expetedConsumption) {
        this.expetedConsumption = expetedConsumption;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Veicolo numero: "+vehicleId+"\n");
        for(Step s:steps)
        {
            str.append("["+s.getStartingNode().getId()+"\t- "+s.getArrivingNode().getId()+" \t- "+s.getStartServiceTime()+" \t- "+s.getTimeToArriveToNextNode()+" \t- "+s.getBatteryLevel()+" \t- "+ s.getTravelTime()+"]\n");
        }
        return str.toString();
    }
}
