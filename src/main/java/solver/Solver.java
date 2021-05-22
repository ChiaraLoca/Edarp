package solver;

import model.*;
import util.PairOfNodes;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solver {
    private List<VehicleInfo> vehicleInfos;
    private List<List<VehicleInfo>> solution;
    private Instance instance;
    private int nVehicles;
    private boolean found=false;
    private Map<Node, Node> originalUnsolved;
    private static int depth=0;
    private List<List<WaitingInfo>> waits = new ArrayList<>();
    private List<List<List<VehicleInfo>>> solutionList= new ArrayList<>();
    private List<Integer> chargingWaits = new ArrayList<>();

    int counter=50;
    int numberUnsolved;

    public Solver(List<VehicleInfo> vehicleInfos, Instance instance, Map<Node, Node> originalUnsolved) {
        this.vehicleInfos = vehicleInfos;
        this.instance = instance;
        nVehicles= instance.getnVehicles();
        solution = new ArrayList<>();
        for (int i = 0; i < nVehicles; i++) {
            solution.add(new ArrayList<>());
            waits.add(new ArrayList<>());
            chargingWaits.add(3);
        }

        this.originalUnsolved= originalUnsolved;
    }


    public List<VehicleInfo> getVehicleInfos() {
        return vehicleInfos;
    }

    public void setVehicleInfos(List<VehicleInfo> vehicleInfos) {
        this.vehicleInfos = vehicleInfos;
    }

    public List<List<VehicleInfo>> getSolution() {
        return solution;
    }

    public void setSolution(List<List<VehicleInfo>> solution) {
        this.solution = solution;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public int getnVehicles() {
        return nVehicles;
    }

    public void setnVehicles(int nVehicles) {
        this.nVehicles = nVehicles;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public Map<Node, Node> getOriginalUnsolved() {
        return originalUnsolved;
    }

    public void setOriginalUnsolved(Map<Node, Node> originalUnsolved) {
        this.originalUnsolved = originalUnsolved;
    }

    public static int getDepth() {
        return depth;
    }

    public static void setDepth(int depth) {
        Solver.depth = depth;
    }

    public List<List<WaitingInfo>> getWaits() {
        return waits;
    }

    public void setWaits(List<List<WaitingInfo>> waits) {
        this.waits = waits;
    }

    //nodi da risolvere
    public void solve(Map<Node, Node> unsolved, int vehicleId) throws Exception {
        depth++;

        //  Se un nodo non è più raggiungibile
        if(nodePermanentlyLost(unsolved))
            return;

        //  Se non ci sono nodi insoddisfatti
        if(unsolved.isEmpty()){
            found=true;
            return;
        }
        if(counter==0) {
            found = true;
            return;
        }

        if(vehicleId>=nVehicles)
            vehicleId=0;
        System.out.println(depth+"\t"+solution.get(vehicleId).size()+"\t"+unsolved.size()+"\t"+vehicleId+"\t"+vehicleInfos.get(vehicleId).getTimeOfMission());
        if(numberUnsolved!=unsolved.size()) {
            numberUnsolved = unsolved.size();
            counter =50;
        }
        else
            counter--;

        double wait=0;
        while(Util.isTimeHorizonRespected(vehicleId, instance.getTimeHorizon(), vehicleInfos.get(vehicleId).getTimeOfMission(), wait)){
            //  Per ogni coppia di nodi non risolta
            for (Map.Entry<Node, Node> e : unsolved.entrySet()) {
                //  Se la coppia di nodi è raggiungibile correttamente
                if (isPossibleNode(new PairOfNodes(e.getKey(), e.getValue()), wait, vehicleInfos.get(vehicleId))) {
                    boolean chargeWaited = false;

                    //  Salvo lo stato attuale
                    Map<Node, Node> modifiedMap = new HashMap<>(unsolved);
                    VehicleInfo saved = new VehicleInfo(vehicleInfos.get(vehicleId));
                    //  Muovo al primo nodo della coppia
                    Util.moveToNextNode(vehicleInfos.get(vehicleId), e.getKey(), wait,instance);
                    VehicleInfo pickupNode = new VehicleInfo(vehicleInfos.get(vehicleId));
                    //  Aggiungo il primo nodo alla soluzione
                    solution.get(vehicleId).add(pickupNode);
                    //  Muovo al secondo nodo della coppia
                    Util.moveToNextNode(vehicleInfos.get(vehicleId), e.getValue(), 0,instance);
                    VehicleInfo dropoffNode = new VehicleInfo(vehicleInfos.get(vehicleId));
                    //  Aggiungo il secondo nodo alla soluzione
                    solution.get(vehicleId).add(dropoffNode);

                    double batteryCharge = vehicleInfos.get(vehicleId).getCurrentBatteryLevel();

                    //  Rimuovo la coppiua di nodi dagli insoddisfatti
                    modifiedMap.remove(e.getKey());

                    //  Chiamo il prossimo veicolo a risolvere
                    solve(modifiedMap, vehicleId + 1);

                    depth--;

                    //  Se ho trovato una soluzione
                    if (found){
                        waits.get(vehicleId).add(0,new WaitingInfo(wait, e.getValue(), batteryCharge));
                        return;

                    }
                    if(chargeWaited){
                        chargingWaits.set(vehicleId, chargingWaits.get(vehicleId)+1);
                    }
                    //  Ripristino lo stato
                    vehicleInfos.set(vehicleId, new VehicleInfo(saved));
                    solution.get(vehicleId).remove(pickupNode);
                    solution.get(vehicleId).remove(dropoffNode);
                }

            }
            // Se nessuno dei nodi è fattibile aspetto e riprovo
            wait+=1;
        }



    }


    public double computeTimeToArriveToNextNode( Node start, Node arrive, double wait, VehicleInfo vehicleInfo) {
        double standardTime = vehicleInfo.getTimeOfMission();
        double travelTime = getTravelTimeFrom(start, arrive) + wait;
        double additionalTime = start.getNodeType().equals(NodeType.CHARGE) ? 0: start.getServiceTime();
        double tdij = travelTime + additionalTime;
        return tdij;
    }

    public double getTravelTimeFrom(Node startNode, Node arriveNode) {
        return instance.getTravelTime()[startNode.getId() - 1][arriveNode.getId() - 1];
    }


    public boolean isPossibleNode(PairOfNodes pairOfNodes, double wait, VehicleInfo vehicleInfo) throws Exception {
        //nodo non scaduto, raggiungibile in tempo ora->pik->drop, pick->drop in 8 minuti , ora->pik->drop->char,

        double timeMission_wait = vehicleInfo.getTimeOfMission()+wait;

        if(timeMission_wait>pairOfNodes.getDropoff().getDeparture())
            return false;
        if(timeMission_wait>pairOfNodes.getPickup().getDeparture())
            return false;

        double currPick = computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),pairOfNodes.getPickup(),0, vehicleInfo);
        if(currPick+timeMission_wait>pairOfNodes.getPickup().getDeparture())
            return false;

        double currPickDrop = computeTimeToArriveToNextNode(pairOfNodes.getPickup(),pairOfNodes.getDropoff(),0, vehicleInfo)+
                currPick;
        if(currPickDrop+ timeMission_wait>pairOfNodes.getDropoff().getDeparture())
            return false;


/*
        double c= computeTimeToArriveToNextNode(pairOfNodes.getDropoff(),getClosestChargingNode(pairOfNodes.getDropoff()),0, vehicleInfo);
        double charge = (vehicleInfo.getTimeOfMission()+d+c) * instance.getVehicleDischargingRate();
        if(charge>vehicleInfo.getCurrentBatteryLevel())
            return false;
*/
        /*double e = vehicleInfo.getTimeOfMission()+
                computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),pairOfNodes.getPickup(),wait, vehicleInfo)+
                computeTimeToArriveToNextNode(pairOfNodes.getPickup(),pairOfNodes.getDropoff(),0, vehicleInfo);*/
        if(currPickDrop+ timeMission_wait<pairOfNodes.getDropoff().getArrival())
            return false;
        if(currPick+ timeMission_wait<pairOfNodes.getPickup().getArrival())
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
        for (List<VehicleInfo> v: solution) {
            System.out.println(v);
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
            {

                return true;}
        }
        return false;
    }


}
