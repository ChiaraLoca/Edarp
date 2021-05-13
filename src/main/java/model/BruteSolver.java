package model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteSolver {
    private List<VehicleInfo> vehicleInfos;
    private List<List<Node>> solution;
    private Instance instance;
    private int nVehicles;
    private boolean found=false;
    private Map<Node, Node> originalUnsolved;
    private static int depth=0;
    private List<List<WaitingInfo>> waits = new ArrayList<>();

    public BruteSolver(List<VehicleInfo> vehicleInfos, Instance instance, Map<Node, Node> originalUnsolved) {
        this.vehicleInfos = vehicleInfos;
        this.instance = instance;
        nVehicles= instance.getnVehicles();
        solution = new ArrayList<>();
        for (int i = 0; i < nVehicles; i++) {
            solution.add(new ArrayList<>());
            waits.add(new ArrayList<>());
        }

        this.originalUnsolved= originalUnsolved;
    }


    //nodi da risolvere
    public void solve(Map<Node, Node> unsolved, int vehicleId) throws Exception {
        depth++;
        if(nodePermanentlyLost(unsolved))
            return;
        System.out.println(depth);
        if(unsolved.isEmpty()){
            found=true;
            return;
        }
        if(vehicleId>=nVehicles)
            vehicleId=0;
        //ogni nodo
        double wait=0;
        while(Util.isTimeHorizonRespected(vehicleId, instance.getTimeHorizon(), vehicleInfos.get(vehicleId).getTimeOfMission(), wait)){
            for (Map.Entry<Node, Node> e : unsolved.entrySet()) {
                if (isPossibleNode(new PairOfNodes(e.getKey(), e.getValue()), wait, vehicleInfos.get(vehicleId))) {
                    //System.out.println("" + vehicleId + '\t' + e.getKey() + '\t' + e.getValue());
                    Map<Node, Node> modifiedMap = new HashMap<>(unsolved);
                    VehicleInfo saved = new VehicleInfo(vehicleInfos.get(vehicleId));
                    //veicolo va al nuovo punto
                    moveToNextNode(vehicleInfos.get(vehicleId), e.getKey(), wait);
                    moveToNextNode(vehicleInfos.get(vehicleId), e.getValue(), 0);
                    //aggiungo i nodi alla soluzione
                    solution.get(vehicleId).add(e.getKey());
                    solution.get(vehicleId).add(e.getValue());

                    double batteryCharge = vehicleInfos.get(vehicleId).getCurrentBatteryLevel();

                    modifiedMap.remove(e.getKey());
                    /*for (List<Node> l: solution) {
                        System.out.println(l);

                    }
                    System.out.println();*/

                    solve(modifiedMap, vehicleId + 1);
                    depth--;
                    if (found){
                        waits.get(vehicleId).add(new WaitingInfo(wait, e.getValue(), batteryCharge));
                        return;

                    }

                    vehicleInfos.set(vehicleId, new VehicleInfo(saved));
                    solution.get(vehicleId).remove(e.getKey());
                    solution.get(vehicleId).remove(e.getValue());
                }

            }
            wait+=1;
        }



    }
    public void moveToNextNode(VehicleInfo vehicleInfo, Node nextNode,double wait)
    {
        double time = computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),nextNode,wait, vehicleInfo);
        vehicleInfo.setTimeOfMission(time+vehicleInfo.getTimeOfMission());

        double movingTime = computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),nextNode,0, vehicleInfo);

        vehicleInfo.setCurrentBatteryLevel(vehicleInfo.getCurrentBatteryLevel()-movingTime*instance.getVehicleDischargingRate());

        vehicleInfo.getPassengerDestination().clear();
        vehicleInfo.getPassengerDestination().addAll(vehicleInfo.getPossiblePassengerDestination());
        vehicleInfo.setWaitingTime(0);

        vehicleInfo.setCurrentPosition(nextNode);

    }

    public double computeTimeToArriveToNextNode( Node start, Node arrive, double wait, VehicleInfo vehicleInfo) {
        double standardTime = vehicleInfo.getTimeOfMission();
        double travelTime = getTravelTimeFrom(start, arrive) + wait;
        double additionalTime = start.getServiceTime();
        double tdij = travelTime + additionalTime;
        return tdij;
    }

    public double getTravelTimeFrom(Node startNode, Node arriveNode) {
        return instance.getTravelTime()[startNode.getId() - 1][arriveNode.getId() - 1];
    }


    public boolean isPossibleNode(PairOfNodes pairOfNodes, double wait, VehicleInfo vehicleInfo) throws Exception {
        //nodo non scaduto, raggiungibile in tempo ora->pik->drop, pick->drop in 8 minuti , ora->pik->drop->char,

        if(vehicleInfo.getTimeOfMission()>pairOfNodes.getDropoff().getDeparture())
            return false;

        double d = computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),pairOfNodes.getPickup(),wait, vehicleInfo)+
                computeTimeToArriveToNextNode(pairOfNodes.getPickup(),pairOfNodes.getDropoff(),0, vehicleInfo);
        if(vehicleInfo.getTimeOfMission()+d>pairOfNodes.getDropoff().getDeparture())
            return false;
/*
        double c= computeTimeToArriveToNextNode(pairOfNodes.getDropoff(),getClosestChargingNode(pairOfNodes.getDropoff()),0, vehicleInfo);
        double charge = (vehicleInfo.getTimeOfMission()+d+c) * instance.getVehicleDischargingRate();
        if(charge>vehicleInfo.getCurrentBatteryLevel())
            return false;
*/
        double e = vehicleInfo.getTimeOfMission()+
                computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),pairOfNodes.getPickup(),wait, vehicleInfo)+
                computeTimeToArriveToNextNode(pairOfNodes.getPickup(),pairOfNodes.getDropoff(),0, vehicleInfo);
        if(e<pairOfNodes.getDropoff().getArrival())
            return false;
        return true;
    }

    private Node getClosestChargingNode(Node start) throws Exception {
        Node node = null;
        HashMap<Node, Double> passeggerDestinationMap = new HashMap<Node, Double>();
        HashMap<PairOfNodes, Double> pickupMap = new HashMap<PairOfNodes, Double>();

        double distance = Double.MAX_VALUE;
        for (Node n : instance.getChargingStationNodes()) {
            if (getTravelTimeFrom(n, start) < distance) {
                distance = getTravelTimeFrom(n, start);
                node = n;
            }

        }
        return node;
    }

    public void start() throws Exception {
        solve(originalUnsolved, 0);
        for (List<Node> l: solution) {
            System.out.println(l);

        }
    }
    public boolean nodePermanentlyLost(Map<Node, Node> map){
        boolean response= true;
        for (int i = 0; i < nVehicles; i++) {
            response=response&&vehicleLostNode(vehicleInfos.get(i), map);
        }
        return response;
    }

    public boolean vehicleLostNode(VehicleInfo vehicleInfo, Map<Node, Node> map){
        for (Map.Entry<Node, Node> e: map.entrySet()) {
            if(vehicleInfo.getTimeOfMission()>e.getValue().getDeparture())
                return true;
        }
        return false;
    }

    public Instance getInstance() {
        return instance;
    }
}
