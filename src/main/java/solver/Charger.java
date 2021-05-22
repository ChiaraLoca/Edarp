package solver;

import model.*;
import util.Util;

import java.util.ArrayList;
import java.util.List;

public class Charger {

    private final Instance instance;
    private List<List<VehicleInfo>> notOptimizedSolution = new ArrayList<>();
    private List<List<WaitingInfo>> infos = new ArrayList<>();

    public Charger(Instance instance,List<List<VehicleInfo>> notOptimizedSolution,List<List<WaitingInfo>> infos) {
        this.instance = instance;
        this.notOptimizedSolution = notOptimizedSolution;
        this.infos = infos;


    }

    public List<VehicleInfo> optimizeVehicle(List<VehicleInfo> solution , int vehicleId,List<WaitingInfo> waitingInfos){
        List<VehicleInfo> newSolution= new ArrayList<>(solution);
        for(VehicleInfo n : solution){
            if(worthChargingAt(n, vehicleId, newSolution,waitingInfos))
                charge(n, newSolution,waitingInfos);
        }
        return newSolution;
    }

    private void charge(VehicleInfo present, List<VehicleInfo> solution,List<WaitingInfo> waitingInfos) {

        Node chargingStation= getBestChargingStation(present.getCurrentPosition(), solution.get(solution.indexOf(present)+1).getCurrentPosition());
        double inTime = Util.computeTimeToArriveToNextNode(present.getCurrentPosition(),chargingStation,0, instance);
        double outTime = Util.computeTimeToArriveToNextNode(chargingStation,solution.get(solution.indexOf(present)+1).getCurrentPosition(),0, instance);
        double charginTime= waitingInfos.get(((int)(solution.indexOf(present)/2)+1)).getWaitTime()-inTime-outTime;
        double rechargeRate = instance.getStationRechargingRate()[chargingStation.getId()-instance.getChargingStationId()[0]];
        double maxChargingTime = (present.getMaxBatteryCapacity()-present.getCurrentBatteryLevel())/ rechargeRate;
        if(charginTime>maxChargingTime)
            charginTime = maxChargingTime;
        double amountCharged = rechargeRate*charginTime;

        VehicleInfo preCharge= new VehicleInfo(present);
        if(inTime*instance.getVehicleDischargingRate()+outTime*instance.getVehicleDischargingRate()>amountCharged)
            return;
        Util.moveToNextNode(preCharge, chargingStation, 0,instance);
        preCharge.setCurrentBatteryLevel(preCharge.getCurrentBatteryLevel()+amountCharged);
        preCharge.setTimeSpendAtCharging(preCharge.getTimeSpendAtCharging()+charginTime);
        solution.add(solution.indexOf(present)+1, new VehicleInfo(preCharge));
        Util.moveToNextNode(preCharge, solution.get(solution.indexOf(present)+2).getCurrentPosition(), charginTime,instance);


        VehicleInfo oldInfo= new VehicleInfo(solution.get(solution.indexOf(present)+2));
        VehicleInfo newV = new VehicleInfo(preCharge);
        newV.setTimeSpendAtCharging(0);
        solution.set(solution.indexOf(present)+2, newV);
        double chargeDifference= preCharge.getCurrentBatteryLevel()-oldInfo.getCurrentBatteryLevel();

        if(solution.indexOf(present)+3< solution.size()) {
            for (int i = solution.indexOf(present) + 3; i < solution.size(); i++) {
                solution.get(i).setCurrentBatteryLevel(solution.get(i).getCurrentBatteryLevel() + chargeDifference);
            }
        }

    }

    private boolean worthChargingAt(VehicleInfo present, int vehicleId, List<VehicleInfo> solution,List<WaitingInfo> waitingInfos) {
        if (present.getCurrentPosition().getNodeType()!=NodeType.DROPOFF)
            return false;
        if((solution.indexOf(present))+1>=(solution.size()))
            return false;
        VehicleInfo copy = new VehicleInfo(present);
        Node chargingStation= getBestChargingStation(present.getCurrentPosition(), solution.get(solution.indexOf(present)+1).getCurrentPosition());

        double inTime = Util.computeTimeToArriveToNextNode(copy.getCurrentPosition(),chargingStation,0,instance);
        double outTime = Util.computeTimeToArriveToNextNode(chargingStation,solution.get(solution.indexOf(present)+1).getCurrentPosition(),0, instance);
        if((solution.indexOf(present)/2)+1>= waitingInfos.size())
            return false;
        if(waitingInfos.get(((int)(solution.indexOf(present)/2)+1)).getWaitTime()<(inTime+outTime))
            return false;

        return true;
    }



    public List<List<VehicleInfo>>  optimize(){

        List<List<VehicleInfo>> tmp = new ArrayList<>();


        for (int i = 0; i < instance.getnVehicles(); i++) {
            List<VehicleInfo> vi =optimizeVehicle(notOptimizedSolution.get(i), i,infos.get(i));
            tmp.add(new ArrayList<>(vi));
        }

        System.out.println("\nOPTIMIZED");

        for (List<VehicleInfo> v: tmp) {
            System.out.println(v);
        }

        return tmp;
    }




    public Node getBestChargingStation(Node start, Node destination)
    {

        List<Double> sumOfDistance= new ArrayList<>();

        for (Node n : instance.getChargingStationNodes()) {
            sumOfDistance.add(Util.getTravelTimeFrom(start, n,instance)+
                    Util.getTravelTimeFrom(n, destination,instance)+ start.getServiceTime());
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

        return instance.getChargingStationNodes().get(index);
    }



}




