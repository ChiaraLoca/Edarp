package solver;

import model.*;


import java.util.*;
import java.util.stream.Collectors;

public class Solver {

    private Instance instance;
    private Solution solution;

    private List<Node> allNodes;
    private int nVehicles;

    private double[][] travelTime;


    private ArrayList<Node> notVisitedNodes;
    private ArrayList<Node> chargingStation;
    private Node[] possibleNextNode;//lista di lungezza n Veicoli che contiene per ogni veicolo il nodo piu vicino
    private List<VehicleInfo> vehicleInfoList = new ArrayList<>();
    private HashMap<Node, Node> destinationMap = new HashMap<>();

    public Solver(Instance instance) {
        this.instance = instance;
        allNodes = instance.getNodes();
        nVehicles = instance.getnVehicles();
        travelTime = instance.getTravelTime();
        notVisitedNodes = instance.getPickupAndDropoffLocations();
        chargingStation = instance.getChargingStationNodes();
        possibleNextNode = new Node[nVehicles];

    }

    ;

    public Solution solve() throws Exception {
        solution = new Solution(instance);
        for (int i = 0; i < nVehicles; i++) {
            vehicleInfoList.add(new VehicleInfo(
                    i + 1,
                    allNodes.get(instance.getArtificialOriginDepotId()[i]),
                    allNodes.get(instance.getArtificialDestinationDepotId()[i]),
                    instance.getVehicleBatteryCapacity()[i],
                    instance.getVehicleCapacity()[i]
            ));

        }
        List<Node> mulo = new ArrayList<>();
        for (Node n : notVisitedNodes) {
            if (n.getNodeType().equals(NodeType.PICKUP))
                destinationMap.put(n, allNodes.get(n.getId() + instance.getnCustomers() - 1));
        }


        while (notVisitedNodes.size() != 0) {
            VehicleInfo vehicleInfo;
            Node nextNode;

            deafultDestinationChooser();
            while (!allNodeAreDifferent(possibleNextNode)) {
                equalNodesDestinationChooser();
            }

            for (int i = 0; i < nVehicles; i++) {
                vehicleInfo = vehicleInfoList.get(i);
                nextNode = possibleNextNode[i];


                /**
                 private int[][][] vehicleSeqStopAtLocations; // X: 1 if vehicle k sequentially stops at location i and j ∈ V, 0 otherwise X{K,V,V}
                 private double[][] timeVehicleStartsAtLocation; // T: time at which vehicle k starts its service at location i ∈ V T{K,V};
                 private double[][] loadOfVehicleAtLocation; // L: load of vehicle k at location i ∈ V L{K,V};
                 private double[][] batteryLoadOfVehicleAtLocation; // B: battery load of vehicle k at location i ∈ V B{K,V}
                 private double[][] chargingTimeOfVehicleAtStation; // E: charging time of vehicle k at charging station s ∈ S E{K,S}
                 private double[] excessRideTimeOfPassenger; // R: excess ride-time of passenger i ∈ P  R{P};
                 */

                solution.getVehicleSeqStopAtLocations()[i][vehicleInfo.getCurrentPosition().getId() - 1][nextNode.getId() - 1] = 1;
                solution.getTimeVehicleStartsAtLocation()[i][nextNode.getId() - 1] = vehicleInfo.getPossibleTimeToArriveToNextNode();
                solution.getLoadOfVehicleAtLocation()[i][nextNode.getId() - 1] = vehicleInfo.getPossiblePassengerDestination().size();
                solution.getBatteryLoadOfVehicleAtLocation()[i][nextNode.getId() - 1] = vehicleInfo.getPossibleBatteryLevel();
                if (nextNode.getNodeType().equals(NodeType.CHARGE))
                    solution.getChargingTimeOfVehicleAtStation()[i][nextNode.getId() - (instance.getChargingStationId()[0])] = vehicleInfo.getTimeSpendAtCharging();
                //solution.getExcessRideTimeOfPassenger()

                if (nextNode.getNodeType().equals(NodeType.PICKUP) || nextNode.getNodeType().equals(NodeType.DROPOFF)) {
                    notVisitedNodes.remove(nextNode);
                    mulo.add(nextNode);
                }
                vehicleInfo.update(nextNode);
                System.out.println("notVisitedNodes: " + notVisitedNodes.size() + " vehicleId: " + i + " position: " + vehicleInfo.getCurrentPosition().getId()+" passeggeri: "+vehicleInfo.getPassengerDestination());
            }

        }


        for (int i = 0; i < nVehicles; i++)
            System.out.println(vehicleInfoList.get(i));
        return solution;
    }

    private Node choseNextNode(VehicleInfo vehicleInfo, NodeType nextNodeType, List<Node> nodes) throws Exception {
        Node nextNode = null;

        switch (nextNodeType) {
            case PICKUP: {
                //System.out.println(notVisitedNodes.size()+" PICKUP-->\t"+vehicleInfo);
                nextNode = getClosestPickupNode(vehicleInfo, nodes);
                break;
            }
            case DROPOFF: {
                ///System.out.println(notVisitedNodes.size()+" DROPOFF-->\t"+vehicleInfo);
                nextNode = getClosestDropoffNode(vehicleInfo);
                break;
            }
            case CHARGE: {
                //System.out.println(notVisitedNodes.size()+" CHARGE -->\t"+vehicleInfo);
                nextNode = getClosestChargingNode(vehicleInfo);
                break;
            }
            case PICKUP_DROPOFF: {
                //System.out.println(notVisitedNodes.size()+" PICKDROP-->\t"+vehicleInfo);
                nextNode = getPickupDropoffNode(vehicleInfo, nodes);
            }
        }
        ifInTime(vehicleInfo, nextNode, 0);
        return nextNode;
    }

    private Node getFartherChargingNode(Node startNode) {
        Node node = null;
        double distance = Double.MIN_NORMAL;
        for (Node n : chargingStation) {
            if (travelTime[n.getId() - 1][startNode.getId() - 1] > distance) {
                distance = travelTime[n.getId() - 1][startNode.getId() - 1];
                node = n;
            }
        }
        return node;
    }

    private Node getClosestChargingNode(VehicleInfo vehicleInfo) throws Exception {
        Node node = null;
        HashMap<Node, Double> map = new HashMap<Node, Double>();
        double distance = Double.MAX_VALUE;
        for (Node n : chargingStation) {
            if (travelTime[n.getId() - 1][vehicleInfo.getCurrentPosition().getId() - 1] < distance) {
                distance = travelTime[n.getId() - 1][vehicleInfo.getCurrentPosition().getId() - 1];
                node = n;
            }

        }

        if (vehicleInfo.getPassengerDestination().size() == 0) {
            ifInTime(vehicleInfo, node, -1);

            /*for (Node n : notVisitedNodes) {
                map.put(n, computeTimeToArriveToNextNode(vehicleInfo.getVehicleId(), node, n, 0));
            }

            Map<Node, Double> result = map.entrySet()
                    .stream()
                    .sorted((o1, o2) -> {
                        return o1.getKey().getArrival() > o2.getKey().getArrival() ? 1 : -1;
                    })
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

*/

        } else {
            for (Node n : vehicleInfo.getPassengerDestination()) {
                map.put(n, computeTimeToArriveToNextNode(vehicleInfo.getVehicleId(), node, n, 0));
            }

            Map<Node, Double> result = map.entrySet()
                    .stream()
                    .sorted((o1, o2) -> {
                        return o1.getKey().getArrival() > o2.getKey().getArrival() ? 1 : -1;
                    })
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            double possibleTime4Charge = (double) result.values().toArray()[0];

            ifInTime(vehicleInfo, node, possibleTime4Charge);


        }

        if (node == null) {
            //aspetta
            throw new Exception("getClosestChargingNode");
        }
        vehicleInfo.getPossiblePassengerDestination().clear();
        vehicleInfo.getPossiblePassengerDestination().addAll(vehicleInfo.getPassengerDestination());
        return node;
    }

    private Node getPickupDropoffNode(VehicleInfo vehicleInfo, List<Node> nodes) throws Exception {
        Node node = null;
        double value = Double.MAX_VALUE;
        List<Node> possibleNodes = new ArrayList<>();
        HashMap<Node, Double> map = new HashMap<Node, Double>();
        double wait = 0;

        while (map.size() == 0) {
            if (wait >= 50)
                throw new Exception("Time is not enough " + wait);
            vehicleInfo.addWaitingTime(wait);
            System.out.println("\taspetto " + wait);
            for (Node n : nodes) {
                if (ifInTime(vehicleInfo, n, 0))
                    map.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
            }
            if (map.size() == 0)
                wait += 0.1;
        }
        Map<Node, Double> result = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Node closestNode = (Node) result.keySet().toArray()[0];
        double closeseNodeDistance = (double) result.values().toArray()[0];
        Node expiringDropoffNode = null;
        double expiringDropoffNodeDistance = 0;
        if (closestNode.getNodeType().equals(NodeType.DROPOFF))
            node = closestNode;
        else {
            Node possibleDropoff;
            for (Map.Entry e : result.entrySet()) {
                double distance = Double.MAX_VALUE;
                if (((Node) e.getKey()).getNodeType().equals(NodeType.DROPOFF)) {
                    possibleDropoff = (Node) e.getKey();
                    if (vehicleInfo.getPassengerDestination().contains(possibleDropoff) && possibleDropoff.getArrival() < distance) {
                        expiringDropoffNode = (Node) e.getKey();
                        expiringDropoffNodeDistance = (double) e.getValue();
                        distance = ((Node) e.getKey()).getArrival();
                    }
                }
            }
            if (expiringDropoffNode == null)
                node = closestNode;
            else if ((closeseNodeDistance + expiringDropoffNodeDistance) < expiringDropoffNode.getArrival())
                node = closestNode;
            else
                node = expiringDropoffNode;
        }


        if (node.getNodeType().equals(NodeType.PICKUP))
            vehicleInfo.getPossiblePassengerDestination().add(destinationMap.get(node));
        else if (node.getNodeType().equals(NodeType.DROPOFF))
            vehicleInfo.getPossiblePassengerDestination().remove(node);
        return node;
    }


    private double getTravelTimeFrom(Node startNode, Node arriveNode) {
        return travelTime[startNode.getId() - 1][arriveNode.getId() - 1];
    }

    private double getBatteryConsumptionFrom(Node startNode, Node arriveNode) {
        return instance.getBatteryConsumption()[startNode.getId() - 1][arriveNode.getId() - 1];
    }

    private Node getClosestPickupNode(VehicleInfo vehicleInfo, List<Node> nodes) throws Exception {
        Node node = null;
        double value = Double.MAX_VALUE;
        List<Node> possibleNodes = new ArrayList<>();
        HashMap<Node, Double> map = new HashMap<Node, Double>();
        double wait = 0;

        while (map.size() == 0) {
            if (wait >= 50)
                throw new Exception("Time is not enough " + wait);
            vehicleInfo.addWaitingTime(wait);
            System.out.println("\taspetto " + wait);
            for (Node n : nodes) {
                if (ifInTime(vehicleInfo, n, 0))
                    map.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
            }
            if (map.size() == 0)
                wait += 0.1;
        }
        Map<Node, Double> result = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        node = (Node) result.keySet().toArray()[0];


        vehicleInfo.getPossiblePassengerDestination().add(destinationMap.get(node));
        return node;
    }

    private Node getClosestDropoffNode(VehicleInfo vehicleInfo) throws Exception {

        Node node = null;
        double value = Double.MAX_VALUE;
        List<Node> possibleNodes = new ArrayList<>();
        HashMap<Node, Double> map = new HashMap<Node, Double>();
        double wait = 0;

        while (map.size() == 0) {
            if (wait >= 50)
                throw new Exception("Time is not enough");
            vehicleInfo.addWaitingTime(wait);
            System.out.println("\taspetto " + wait);
            for (Node n : vehicleInfo.getPassengerDestination()) {
                if (ifInTime(vehicleInfo, n, 0))
                    map.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
            }
            if (map.size() == 0)
                wait += 0.1;
        }
        Map<Node, Double> result = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        node = (Node) result.keySet().toArray()[0];

        vehicleInfo.getPossiblePassengerDestination().remove(node);
        return node;


    }

    private boolean ifInTime(VehicleInfo vehicleInfo, Node possibleNode, double time) {


        double standardTime = solution.getTimeVehicleStartsAtLocation()[vehicleInfo.getVehicleId() - 1][vehicleInfo.getCurrentPosition().getId() - 1];
        double travelTime = getTravelTimeFrom(vehicleInfo.getCurrentPosition(), possibleNode) + vehicleInfo.getWaitingTime();
        double additionalTime;
        double tdij;
        if (possibleNode.getNodeType().equals(NodeType.PICKUP) || possibleNode.getNodeType().equals(NodeType.DROPOFF)) {
            additionalTime = vehicleInfo.getCurrentPosition().getServiceTime();
            tdij = standardTime + travelTime + additionalTime;

            double arr = possibleNode.getArrival();
            double dep = possibleNode.getDeparture();
            boolean barr = tdij > arr;
            boolean bdep = tdij < dep;
            /*if(vehicleInfo.getWaitingTime()>1 && vehicleInfo.getWaitingTime()<3)*/
            //System.out.println("arr-dep: "+barr+"-"+bdep+"\tarr-tdij-dep: "+arr+"-"+tdij+"-"+dep);
            if (barr && bdep) {
                vehicleInfo.setPossibleTimeToArriveToNextNode(tdij);
                vehicleInfo.setTimeSpendAtCharging(0);
                vehicleInfo.decreseCurrentBatteryLevel(getBatteryConsumptionFrom(vehicleInfo.getCurrentPosition(), possibleNode));
                return true;
            } else
                return false;
        } else if (possibleNode.getNodeType().equals(NodeType.CHARGE)) {

            tdij = standardTime + travelTime + charge(time, vehicleInfo, possibleNode);
            vehicleInfo.setPossibleTimeToArriveToNextNode(tdij);

            return true;
        } else
            return false;

    }


    private double computeTimeToArriveToNextNode(int vehicleId, Node start, Node arrive, double wait) {
        double standardTime = solution.getTimeVehicleStartsAtLocation()[vehicleId - 1][start.getId() - 1];
        double travelTime = getTravelTimeFrom(start, arrive) + wait;
        double additionalTime = start.getServiceTime();
        double tdij = standardTime + travelTime + additionalTime;
        return tdij;
    }

    private double charge(double time, VehicleInfo vehicleInfo, Node possibleChargingNode) {
        double charge;
        double timeinCharging;
        double rechargingRate = instance.getStationRechargingRate()[possibleChargingNode.getId() - instance.getChargingStationId()[0]];
        if (time == -1) {
            timeinCharging = vehicleInfo.getMissingCharge() / rechargingRate;
            vehicleInfo.resetCurrentBatteryLevel();
        } else {
            charge = rechargingRate * time;
            timeinCharging = time;
            vehicleInfo.setCurrentBatteryLevel(charge);
        }
        vehicleInfo.setTimeSpendAtCharging(timeinCharging);

        return timeinCharging;
    }

    private boolean allNodeAreDifferent(Node[] possibleNextNode) {

        Set<Node> set = new HashSet<Node>(new ArrayList<>(Arrays.asList(possibleNextNode)));
        if (set.size() < possibleNextNode.length) {
            return false;
        }
        return true;
    }


    private void deafultDestinationChooser() throws Exception {
        VehicleInfo vehicleInfo;
        Node nextNode;
        for (int i = 0; i < nVehicles; i++) {
            vehicleInfo = vehicleInfoList.get(i);


            NodeType nextNodeType = NodeType.NONE;
            Node fartherChargingNode = getFartherChargingNode(vehicleInfo.getCurrentPosition());
            if (getBatteryConsumptionFrom(vehicleInfo.getCurrentPosition(), fartherChargingNode) < vehicleInfo.getCurrentBatteryLevel()) {
                if (vehicleInfo.getPassengerDestination().size() == 0)
                    nextNodeType = NodeType.PICKUP;
                else if (vehicleInfo.getPassengerDestination().size() == vehicleInfo.getMaxLoad())
                    nextNodeType = NodeType.DROPOFF;
                else
                    nextNodeType = NodeType.PICKUP_DROPOFF;
            } else {
                nextNodeType = NodeType.CHARGE;
            }

            possibleNextNode[i] = (choseNextNode(vehicleInfo, nextNodeType, notVisitedNodes));
        }
    }

    private void equalNodesDestinationChooser() throws Exception {
        List<Node> newList = new ArrayList<>(notVisitedNodes);
        newList.remove(possibleNextNode[0]);
        for (int i = 1; i < nVehicles; i++) {
            vehicleInfoList.get(i).getPossiblePassengerDestination().clear();
            vehicleInfoList.get(i).getPossiblePassengerDestination().addAll(vehicleInfoList.get(i).getPassengerDestination());
            possibleNextNode[i] = (choseNextNode(vehicleInfoList.get(i), possibleNextNode[i].getNodeType(), newList));
        }
    }
}

