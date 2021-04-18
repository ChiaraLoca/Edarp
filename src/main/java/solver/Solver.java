package solver;

import model.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solver {

    private Instance instance;
    private Solution solution;

    private List<Node> allNodes;
    private int nVehicles;

    private double[][] travelTime;


    private ArrayList<Node> notVisitedNodes = new ArrayList<>();
    private ArrayList<Node> chargingStation = new ArrayList<>();
    private ArrayList<Node> possibleNextNode = new ArrayList<>();//lista di lungezza n Veicoli che contiene per ogni veicolo il nodo piu vicino
    public Solver(Instance instance){
        this.instance =instance;
        allNodes = instance.getNodes();
        nVehicles = instance.getnVehicles();
        travelTime = instance.getTravelTime();
        notVisitedNodes = instance.getPickupAndDropoffLocations();

    };

    public Solution solve2()
    {
        for(Node n : allNodes) {
            if(n.getNodeType().equals(NodeType.CHARGE))
                chargingStation.add(n);
        }
        //crea un numero di missioni pari al numero di veicoli
        for(int i=0;i<nVehicles;i++) {
            solution.getMissions().add(new Mission((i+1),instance.getArtificialOriginDepotId()[i],instance.getArtificialDestinationDepotId()[i],instance.getVehicleInitBatteryInventory()[i]));
            solution.getTimeVehicleStartsAtLocation()[i][0]= 0;
        }
    }


    public Solution solve() throws Exception {
         solution = new Solution(instance);

        //inizializza la lista con tutte lw stazioni di pickup e dropoff
        for(Node n : allNodes) {
            if(n.getNodeType().equals(NodeType.CHARGE))
                chargingStation.add(n);
        }
        //crea un numero di missioni pari al numero di veicoli
        for(int i=0;i<nVehicles;i++) {
            solution.getMissions().add(new Mission((i+1),instance.getArtificialOriginDepotId()[i],instance.getArtificialDestinationDepotId()[i],instance.getVehicleInitBatteryInventory()[i]));
            solution.getTimeVehicleStartsAtLocation()[i][0]= 0;
        }


        List<Mission> missions = solution.getMissions();
        Step step;
        Mission mission = null;
        Node newStep =null;
        Node currentNode =null;
        double consuption=0;
        while(notVisitedNodes.size()!=0)
        {
                for(int i=0;i<nVehicles;i++) {
                    possibleNextNode.add(getNextNode(missions.get(i)));
                    mission = missions.get(i);
                    newStep =possibleNextNode.get(i);
                    currentNode =allNodes.get(mission.getCurrentPositionId()-1);

                    solution.getTimeVehicleStartsAtLocation()[i][newStep.getId()-1] = mission.getTimeToArriveToNextNode();

                    step = new Step();
                    step.setStartingNode(currentNode);
                    step.setArrivingNode(newStep);
                    step.setStartServiceTime(solution.getTimeVehicleStartsAtLocation()[i][mission.getCurrentPositionId()-1]);
                    step.setTimeToArriveToNextNode(mission.getTimeToArriveToNextNode());
                    step.setBatteryLevel(mission.getCurrentCharge());
                    step.setTravelTime(travelTime[currentNode.getId()-1][newStep.getId()-1]);//si puo togliere

                    if(mission.getNextNodeType().equals(NodeType.CHARGE)){
                        mission.setCurrentCharge(instance.getVehicleInitBatteryInventory()[i]);
                    }
                    mission.getSteps().add(step);


                    if(mission.getNextNodeType().equals(NodeType.PICKUP) || mission.getNextNodeType().equals(NodeType.DROPOFF))
                        notVisitedNodes.remove(newStep);
                    mission.update(newStep.getId());
                }
                possibleNextNode.clear();


        }





        return solution;
    }
    private Node getNextNode(Mission mission)
    {
        Node nextStep = null;
        Node charging = null;
        int current = mission.getCurrentPositionId()-1;
        double consumptionNextStep =0;
        double consumptionCharging =0;
        switch(mission.getNextNodeType())
        {

            case PICKUP:{
                nextStep = getClosestNode(mission);
                if(nextStep==null)
                    nextStep = allNodes.get(mission.getArtificialDestinationDepotId()-1);
                break;}
            case DROPOFF:{
                nextStep =getRightDropoffNode(mission);
                break;}
            case CHARGE:{
                nextStep = getClosestChargingNode(mission);
            double currentCharge = mission.getCurrentCharge();
            double maxCharge = instance.getVehicleBatteryCapacity()[mission.getVehicleId()-1];
            double charge = maxCharge-currentCharge;
            double rechargingRate =instance.getStationRechargingRate()[mission.getVehicleId()-1];
            double time = charge/rechargingRate;
            double d= solution.getTimeVehicleStartsAtLocation()[mission.getVehicleId()-1][current-1]+travelTime[current-1][nextStep.getId()-1]+time;
            mission.setTimeToArriveToNextNode(d);
            return nextStep;
            }


        }


        charging=getClosestChargingNode(mission);
        consumptionCharging = instance.getBatteryConsumption()[mission.getCurrentPositionId()-1][charging.getId()-1];
        consumptionNextStep = instance.getBatteryConsumption()[mission.getCurrentPositionId()-1][nextStep.getId()-1];

        if(mission.getCurrentCharge()>consumptionNextStep+consumptionCharging){
            mission.setExpetedConsumption(consumptionNextStep);
            return nextStep;}
        else {
            mission.setNextNodeType(NodeType.CHARGE);
            mission.setExpetedConsumption(consumptionCharging);
            return charging;
        }
    }
    private Node getClosestNode(Mission mission)
    {
        double value=Double.MAX_VALUE;
        Node node=null;
        for (Node n : notVisitedNodes) {
            /*if(n.getNodeType().equals(mission.getNextNodeType())) {
                if (travelTime[n.getId() - 1][mission.getVehicleId() - 1] < value && ifInTime(mission,n)) {
                    value = travelTime[n.getId() - 1][mission.getVehicleId() - 1];
                    node = n;
                }
            }*/
            if(n.getNodeType().equals(mission.getNextNodeType()))
            {
                if(n.getDeparture()<value && ifInTime(mission,n))
                {
                    value = n.getDeparture();
                    node = n;
                }
            }
        }
        return node;
    }
    private Node getClosestChargingNode(Mission mission)
    {
        double distance=Double.MAX_VALUE;
        Node node=null;
        for (Node n : chargingStation) {
           if (travelTime[n.getId() - 1][mission.getVehicleId() - 1] < distance) {
               distance = travelTime[n.getId() - 1][mission.getVehicleId() - 1];
               node = n;
           }

        }
        return node;
    }
    private Node getRightDropoffNode(Mission mission)
    {
        if(mission.getCurrentPositionId()>instance.getnCustomers()){

            int i = mission.getSteps().get(mission.getSteps().size()-1).getStartingNode().getId();
            return allNodes.get(i-1+instance.getnCustomers());
        }
        else
            return allNodes.get(mission.getCurrentPositionId()-1+instance.getnCustomers());
}

    private boolean allNodeAreDifferent(ArrayList<Node> possibleNextNode)
    {
        Set<Node> set = new HashSet<Node>(possibleNextNode);
        if(set.size() < possibleNextNode.size()){
           return false;
        }
        return true;
    }

    private boolean ifInTime(Mission mission,Node possibleNodo)
    {
        int i = mission.getCurrentPositionId();
        int j = possibleNodo.getId();
        Node nodeI =allNodes.get(i-1);

        double tdij = solution.getTimeVehicleStartsAtLocation()[mission.getVehicleId()-1][i-1]+travelTime[i-1][j-1]+nodeI.getServiceTime();

        if(tdij>nodeI.getArrival() && tdij<nodeI.getDeparture()) {
            mission.setTimeToArriveToNextNode(tdij);
            return true;
        }
        else
            return false;
    }




}
