package model;



import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Util {
    public static boolean isTimeHorizonRespected(int id,double maxTime,double timeSpend,double wait) throws Exception {
        if ((timeSpend+wait)>maxTime)
            throw new Exception(id+" Time is not enough: " + timeSpend+"wait: "+wait);
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

}
