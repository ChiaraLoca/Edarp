package model;

import java.util.ArrayList;
import java.util.List;

public class Charger {
    private BruteSolver bruteSolver;
    private List<List<Node>> optimized= new ArrayList<>();

    public Charger(BruteSolver bruteSolver) {
        this.bruteSolver = bruteSolver;
    }

    public List<Node> optimizeVehicle(List<Node> solution, int vehicleId){
        List<Node> newSolution= new ArrayList<>(solution);
        for(Node n : solution){
            if(worthChargingAt(n, vehicleId, newSolution))
                charge(n, vehicleId, newSolution);
        }
        return newSolution;
    }

    private void charge(Node present, int vehicleId, List<Node> solution) {

        Node chargingStation= getBestChargingStation(present, solution.get(solution.indexOf(present)+1));
        double inTime = bruteSolver.computeTimeToArriveToNextNode(present,chargingStation,0, bruteSolver.getVehicleInfos().get(vehicleId));
        double outTime = bruteSolver.computeTimeToArriveToNextNode(chargingStation,solution.get(solution.indexOf(present)+1),0, bruteSolver.getVehicleInfos().get(vehicleId));
        double charginTime= bruteSolver.getWaits().get(vehicleId).get(((int)(solution.indexOf(present)/2)+1)).getWaitTime()-inTime-outTime;
        double rechargeRate = bruteSolver.getInstance().getStationRechargingRate()[chargingStation.getId()-bruteSolver.getInstance().getChargingStationId()[0]];
        double amountCharged = rechargeRate*charginTime;

        if(inTime*bruteSolver.getInstance().getVehicleDischargingRate()+outTime*bruteSolver.getInstance().getVehicleDischargingRate()>amountCharged)
            return;
        bruteSolver.moveToNextNode(bruteSolver.getVehicleInfos().get(vehicleId), chargingStation, 0);
        bruteSolver.getVehicleInfos().get(vehicleId).setCurrentBatteryLevel(bruteSolver.getVehicleInfos().get(vehicleId).getCurrentBatteryLevel()+amountCharged);
        bruteSolver.getVehicleInfos().get(vehicleId).setTimeSpendAtCharging(charginTime);
        bruteSolver.moveToNextNode(bruteSolver.getVehicleInfos().get(vehicleId), solution.get(solution.indexOf(present)+1), charginTime);

        solution.add(solution.indexOf(present)+1, chargingStation);

    }

    private boolean worthChargingAt(Node present, int vehicleId, List<Node> solution) {
        if (present.getNodeType()!=NodeType.DROPOFF)
            return false;
        if((solution.indexOf(present))+1>=(solution.size()))
            return false;
        Node chargingStation= getBestChargingStation(present, solution.get(solution.indexOf(present)+1));

        double inTime = bruteSolver.computeTimeToArriveToNextNode(present,chargingStation,0, bruteSolver.getVehicleInfos().get(vehicleId));
        double outTime = bruteSolver.computeTimeToArriveToNextNode(chargingStation,solution.get(solution.indexOf(present)+1),0, bruteSolver.getVehicleInfos().get(vehicleId));
        if((solution.indexOf(present)/2)+1>= bruteSolver.getWaits().get(vehicleId).size())
            return false;
        if(bruteSolver.getWaits().get(vehicleId).get(((int)(solution.indexOf(present)/2)+1)).getWaitTime()<(inTime+outTime))
            return false;




        //vehicleInfo.setCurrentBatteryLevel(vehicleInfo.getCurrentBatteryLevel()-movingTime*instance.getVehicleDischargingRate());

        return true;
    }



    public List<List<Node>> optimize(){

        for (int i = 0; i < bruteSolver.getnVehicles(); i++) {
            optimized.add(optimizeVehicle(bruteSolver.getSolution().get(i), i));
        }
        return optimized;
    }


    public Node getBestChargingStation(Node start, Node destination)
    {

        List<Double> sumOfDistance= new ArrayList<>();

        for (Node n : bruteSolver.getInstance().getChargingStationNodes()) {
            sumOfDistance.add(bruteSolver.getTravelTimeFrom(n, start)+
                    bruteSolver.getTravelTimeFrom(n, destination));
        }
        double min = Double.MAX_VALUE;
        int index=0;
        for(int i =0;i<sumOfDistance.size();i++)
        {
            if(sumOfDistance.get(i)<min) {
                min = sumOfDistance.get(i);
                index = i;
            }
        }

        return bruteSolver.getInstance().getChargingStationNodes().get(index);
    }
}




