package model;

import java.util.ArrayList;
import java.util.List;

public class Charger {
    private List<Node> startingSolution;
    private BruteSolver bruteSolver;

    public Charger(BruteSolver bruteSolver) {
        this.bruteSolver = bruteSolver;
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

/*
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


* */