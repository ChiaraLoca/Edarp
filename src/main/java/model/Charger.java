package model;

import java.util.ArrayList;
import java.util.List;

public class Charger {
    private BruteSolver bruteSolver;

    public Charger(BruteSolver bruteSolver) {
        this.bruteSolver = bruteSolver;
    }

    public void optimizeVehicle(List<Node> solution, int vehicleId){
        List<Node> newSolution= new ArrayList<>(solution);
        for(Node n : solution){
            if(worthChargingAt(n, vehicleId, newSolution))
                charge(n, vehicleId, newSolution);
        }

    }

    private void charge(Node present, int vehicleId, List<Node> solution) {

        Node chargingStation= getBestChargingStation(present, solution.indexOf(present)+1);
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
        Node chargingStation= getBestChargingStation(present, solution.indexOf(present)+1);

        double inTime = bruteSolver.computeTimeToArriveToNextNode(present,chargingStation,0, bruteSolver.getVehicleInfos().get(vehicleId));
        double outTime = bruteSolver.computeTimeToArriveToNextNode(chargingStation,solution.get(solution.indexOf(present)+1),0, bruteSolver.getVehicleInfos().get(vehicleId));

        if(bruteSolver.getWaits().get(vehicleId).get(((int)(solution.indexOf(present)/2)+1)).getWaitTime()<(inTime+outTime))
            return false;




        //vehicleInfo.setCurrentBatteryLevel(vehicleInfo.getCurrentBatteryLevel()-movingTime*instance.getVehicleDischargingRate());

        return true;
    }



    public void optimize(){

    }
}
