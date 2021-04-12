package solver;

import model.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solver {

    private Instance instance;

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

    };


    public Solution solve()
    {
        Solution solution = new Solution(instance);

        //inizializza la lista con tutte lw stazioni di pickup e dropoff
        for(Node n : allNodes) {
            if (n.getNodeType().equals(NodeType.PICKUP) || n.getNodeType().equals(NodeType.DROPOFF))
                notVisitedNodes.add(n);
            else if(n.getNodeType().equals(NodeType.CHARGE))
                chargingStation.add(n);
        }
        //crea un numero di missioni pari al numero di veicoli
        for(int i=0;i<nVehicles;i++) {
            solution.getMissions().add(new Mission((i+1),instance.getArtificialOriginDepotId()[i],instance.getArtificialDestinationDepotId()[i],instance.getVehicleInitBatteryInventory()[i]));
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
            }
            if(allNodeAreDifferent(possibleNextNode))
            {


                for(int i=0;i<nVehicles;i++) {
                    mission = missions.get(i);
                    newStep =possibleNextNode.get(i);
                    currentNode =allNodes.get(mission.getCurrentPositionId()-1);

                    step = new Step();
                    step.setStartingNode(currentNode);
                    step.setArrivingNodo(newStep);
                    step.setBatteryLevel(mission.getCurrentCharge());
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
            else
            {
                //ci sono nodi uguali;
            }

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
                break;}
            case DROPOFF:{
                nextStep =getRightDropoffNode(mission);
                break;}
            case CHARGE:{break;}


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
        double distance=Double.MAX_VALUE;
        Node node=null;
        for (Node n : notVisitedNodes) {
            if(n.getNodeType().equals(mission.getNextNodeType())) {
                if (travelTime[n.getId() - 1][mission.getVehicleId() - 1] < distance) {
                    distance = travelTime[n.getId() - 1][mission.getVehicleId() - 1];
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




}
