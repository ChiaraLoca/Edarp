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
        System.out.println(TEXT_RED+s+TEXT_RESET);
    }

}
