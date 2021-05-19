package model;



import java.util.*;
import java.util.stream.Collectors;

public class Util {
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";


    public static boolean isTimeHorizonRespected(int id,double maxTime,double timeSpend,double wait) throws Exception {
        if ((timeSpend+wait)>maxTime)
            return false;
        return true;
    }

    public static HashMap<PairOfNodes,Double> orderPairOfNodesDoubleMapBy(HashMap<PairOfNodes,Double> map, Order order) {

        Map<PairOfNodes,Double> result = null;
        switch (order)
        {
            case DESTINATION_DEPARTURE:{
                result = map.entrySet()
                        .stream()
                        .sorted((o1, o2) -> {return o1.getKey().getDropoff().getDeparture()>o2.getKey().getDropoff().getDeparture() ? 1:-1;})
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;
            }
            case DISTANCE:
            {
                result = map.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;

            }

        }
        return (HashMap<PairOfNodes, Double>) result;

    }

    public static HashMap<Node,Double> orderNodeDoubleMapBy(HashMap<Node,Double>map,Order order) {

        Map<Node,Double> result = null;
        switch (order)
        {
            case DEPARTURE:{
                result = map.entrySet()
                        .stream()
                        .sorted((o1, o2) -> {return o1.getKey().getDeparture()>o2.getKey().getDeparture() ? 1:-1;})
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;
            }
            case ARRIVAL:{
                result = map.entrySet()
                        .stream()
                        .sorted((o1, o2) -> {return o1.getKey().getArrival()>o2.getKey().getArrival() ? 1:-1;})
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;
            }
            case DISTANCE:
            {
                result = map.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;

            }

        }
        return (HashMap<Node, Double>) result;

    }

    public static HashMap<Node,Node> orderNodeNodeMapBy(Map<Node,Node>map,Order order) {

        Map<Node,Node> result = null;
        switch (order)
        {
            case DESTINATION_DEPARTURE:{
                result = map.entrySet()
                        .stream()
                        .sorted((o1, o2) -> {return o1.getValue().getArrival()>o2.getValue().getArrival() ? 1:-1;})
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;
            }
        }
        return (HashMap<Node, Node>) result;

    }

    public static boolean allNodeAreDifferent(Node[] possibleNextNode) {

        Set<Node> set = new HashSet<Node>(new ArrayList<>(Arrays.asList(possibleNextNode)));
        if (set.size() < possibleNextNode.length) {
            return false;
        }
        return true;
    }

    public static void printRed(String s)
    {
        System.out.print(TEXT_RED+s+TEXT_RESET);
    }
    public static void printBlack(String s)
    {
        System.out.print(TEXT_BLACK+s+TEXT_RESET);
    }
    public static void printGreen(String s)
    {
        System.out.print(TEXT_GREEN+s+TEXT_RESET);
    }
    public static void printYellow(String s)
    {
        System.out.print(TEXT_YELLOW+s+TEXT_RESET);
    }
    public static void printBlue(String s)
    {
        System.out.print(TEXT_BLUE+s+TEXT_RESET);
    }
    public static void printPurple(String s)
    {
        System.out.print(TEXT_PURPLE+s+TEXT_RESET);
    }
    public static void printCyan(String s)
    {
        System.out.print(TEXT_CYAN+s+TEXT_RESET);
    }

    public static void printStepinfo(int id,int iteration,double time,Map<Node,Node> nodes)
    {

        Util.printRed("\nVEICOLO NUMERO "+id+" iterazione: "+iteration+"------------------------------------------------------------\n");
        Util.printRed("Nodi rimasti ");
        for(Map.Entry e: nodes.entrySet() )
        {

            Util.printRed(((Node)e.getKey()).getId()+" - ");
        }
        Util.printRed("Tempo: "+time+"\n\n");
    }

    public static double computeTimeToArriveToNextNode( Node start, Node arrive, double wait,Instance instance) {

        double travelTime = getTravelTimeFrom(start, arrive,instance) + wait;
        double additionalTime = start.getNodeType().equals(NodeType.CHARGE) ? 0: start.getServiceTime();
        double tdij = travelTime + additionalTime;
        return tdij;
    }
    public static double getTravelTimeFrom(Node startNode, Node arriveNode,Instance instance) {
        return instance.getTravelTime()[startNode.getId() - 1][arriveNode.getId() - 1];
    }

    public static  void moveToNextNode(VehicleInfo vehicleInfo, Node nextNode,double wait,Instance instance)
    {
        if(nextNode.getNodeType().equals(NodeType.PICKUP))
            vehicleInfo.getPassengerDestination().add(instance.getNodes().get(nextNode.getId()+ instance.getnCustomers()-1));
        else if(nextNode.getNodeType().equals(NodeType.DROPOFF))
            vehicleInfo.getPassengerDestination().remove(nextNode);


        double time = computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),nextNode,wait, instance);
        vehicleInfo.setTimeOfMission(time+vehicleInfo.getTimeOfMission());

        double movingTime = computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),nextNode,0, instance);

        vehicleInfo.setCurrentBatteryLevel(vehicleInfo.getCurrentBatteryLevel()-movingTime*instance.getVehicleDischargingRate());

        vehicleInfo.setWaitingTime(0);

        vehicleInfo.setCurrentPosition(nextNode);

    }
}
