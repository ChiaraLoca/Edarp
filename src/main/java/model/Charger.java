package model;

import java.util.ArrayList;
import java.util.List;

public class Charger {
    private BruteSolver bruteSolver;
    private List<List<VehicleInfo>> optimized= new ArrayList<>();

    public Charger(BruteSolver bruteSolver) {
        this.bruteSolver = bruteSolver;
    }

    public List<VehicleInfo> optimizeVehicle(List<VehicleInfo> solution, int vehicleId){
        List<VehicleInfo> newSolution= new ArrayList<>(solution);
        for(VehicleInfo n : solution){
            if(worthChargingAt(n, vehicleId, newSolution))
                charge(n, vehicleId, newSolution);
        }
        return newSolution;
    }

    private void charge(VehicleInfo present, int vehicleId, List<VehicleInfo> solution) {

        Node chargingStation= getBestChargingStation(present.getCurrentPosition(), solution.get(solution.indexOf(present)+1).getCurrentPosition());
        double inTime = bruteSolver.computeTimeToArriveToNextNode(present.getCurrentPosition(),chargingStation,0, bruteSolver.getVehicleInfos().get(vehicleId));
        double outTime = bruteSolver.computeTimeToArriveToNextNode(chargingStation,solution.get(solution.indexOf(present)+1).getCurrentPosition(),0, bruteSolver.getVehicleInfos().get(vehicleId));
        double charginTime= bruteSolver.getWaits().get(vehicleId).get(((int)(solution.indexOf(present)/2)+1)).getWaitTime()-inTime-outTime;
        double rechargeRate = bruteSolver.getInstance().getStationRechargingRate()[chargingStation.getId()-bruteSolver.getInstance().getChargingStationId()[0]];
        double amountCharged = rechargeRate*charginTime;
        VehicleInfo preCharge= new VehicleInfo(present);
        if(inTime*bruteSolver.getInstance().getVehicleDischargingRate()+outTime*bruteSolver.getInstance().getVehicleDischargingRate()>amountCharged)
            return;
        bruteSolver.moveToNextNode(preCharge, chargingStation, 0);
        preCharge.setCurrentBatteryLevel(preCharge.getCurrentBatteryLevel()+amountCharged);
        preCharge.setTimeSpendAtCharging(preCharge.getTimeSpendAtCharging()+charginTime);
        solution.add(solution.indexOf(present)+1, new VehicleInfo(preCharge));
        bruteSolver.moveToNextNode(preCharge, solution.get(solution.indexOf(present)+2).getCurrentPosition(), charginTime);

        VehicleInfo oldInfo= new VehicleInfo(solution.get(solution.indexOf(present)+2));
        solution.set(solution.indexOf(present)+2, new VehicleInfo(preCharge));
        double chargeDifference= preCharge.getCurrentBatteryLevel()-oldInfo.getCurrentBatteryLevel();

        if(solution.indexOf(present)+3< solution.size()) {
            for (int i = solution.indexOf(present) + 3; i < solution.size(); i++) {
                solution.get(i).setCurrentBatteryLevel(solution.get(i).getCurrentBatteryLevel() + chargeDifference);
            }
        }

    }

    private boolean worthChargingAt(VehicleInfo present, int vehicleId, List<VehicleInfo> solution) {
        if (present.getCurrentPosition().getNodeType()!=NodeType.DROPOFF)
            return false;
        if((solution.indexOf(present))+1>=(solution.size()))
            return false;
        VehicleInfo copy = new VehicleInfo(present);
        Node chargingStation= getBestChargingStation(present.getCurrentPosition(), solution.get(solution.indexOf(present)+1).getCurrentPosition());

        double inTime = bruteSolver.computeTimeToArriveToNextNode(copy.getCurrentPosition(),chargingStation,0, copy);
        double outTime = bruteSolver.computeTimeToArriveToNextNode(chargingStation,solution.get(solution.indexOf(present)+1).getCurrentPosition(),0, copy);
        if((solution.indexOf(present)/2)+1>= bruteSolver.getWaits().get(vehicleId).size())
            return false;
        if(bruteSolver.getWaits().get(vehicleId).get(((int)(solution.indexOf(present)/2)+1)).getWaitTime()<(inTime+outTime))
            return false;




        //vehicleInfo.setCurrentBatteryLevel(vehicleInfo.getCurrentBatteryLevel()-movingTime*instance.getVehicleDischargingRate());

        return true;
    }



    public List<List<VehicleInfo>> optimize(){

        for (int i = 0; i < bruteSolver.getnVehicles(); i++) {
            optimized.add(optimizeVehicle(bruteSolver.getSolution().get(i), i));
        }
        System.out.println("\n\n\n");
        System.out.println("OPTIMIZED");
        for (List<VehicleInfo> v: optimized) {
            System.out.println(v);

        }
        return optimized;
    }


    public Node getBestChargingStation(Node start, Node destination)
    {

        List<Double> sumOfDistance= new ArrayList<>();

        for (Node n : bruteSolver.getInstance().getChargingStationNodes()) {
            sumOfDistance.add(bruteSolver.getTravelTimeFrom(start, n)+
                    bruteSolver.getTravelTimeFrom(n, destination)+ start.getServiceTime());
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




