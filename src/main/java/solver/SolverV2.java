package solver;

import model.*;
import parser.InstanceReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
class Main
{
    public static void main(String[] args) throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        SolverV2 solver = new SolverV2(instance);
        Solution solution = solver.solve(instance);
        assert solution!=null;
        System.out.println(solution);
    }
}
public class SolverV2 implements ISolver {
    private Instance instance;
    private Solution solution;

    private List<Node> allNodes;
    private int nVehicles;

    private double[][] travelTime;


    private ArrayList<Node> notVisitedNodes;
    private ArrayList<Node> chargingStation;
    private Node[] possibleNextNode;//lista di lungezza n Veicoli che contiene per ogni veicolo il nodo piu vicino
    private List<VehicleInfo> vehicleInfoList = new ArrayList<>();
    private HashMap<Node, Node> mulo = new HashMap<>();
    private int numberOfNodes;
    private HashMap<Node, Node> notVisitedNodesMap = new HashMap<>();

    private boolean cheat = true;


    public SolverV2(Instance instance) {
        this.instance = instance;
        allNodes = instance.getNodes();
        nVehicles = instance.getnVehicles();
        travelTime = instance.getTravelTime();
        notVisitedNodes = instance.getPickupAndDropoffLocations();
        chargingStation = instance.getChargingStationNodes();
        possibleNextNode = new Node[nVehicles];
        numberOfNodes = instance.getnCustomers()*2;


    }


    @Override
    public Solution solve(Instance instance) throws Exception {
        solution = new Solution(instance);
        for (int i = 0; i < nVehicles; i++) {
            if(cheat) {
                vehicleInfoList.add(new VehicleInfo(
                        i + 1,
                        allNodes.get(instance.getArtificialOriginDepotId()[i]),
                        allNodes.get(instance.getArtificialDestinationDepotId()[i]),
                        100,
                        instance.getVehicleCapacity()[i]

                ));
            }
            else
            {
                vehicleInfoList.add(new VehicleInfo(
                        i + 1,
                        allNodes.get(instance.getArtificialOriginDepotId()[i]),
                        allNodes.get(instance.getArtificialDestinationDepotId()[i]),
                        instance.getVehicleBatteryCapacity()[i],
                        instance.getVehicleCapacity()[i]

                ));
            }
        }

        for (Node n : notVisitedNodes) {
            if (n.getNodeType().equals(NodeType.PICKUP))
                notVisitedNodesMap.put(n, notVisitedNodes.get(n.getId() + instance.getnCustomers() - 1));
        }
        notVisitedNodesMap = Util.orderNodeNodeMapBy(notVisitedNodesMap,Order.DESTINATION_DEPARTURE);

        int stopWhileOfDestinationChooser=0;

        for(int i = 0; i< numberOfNodes;) {
            VehicleInfo vehicleInfo;
            Node nextNode;

            //seglie la tipologia di nodo a cui il veicolo è diretto e ci va.
            if(!deafultDestinationChooser()) {
                System.out.println("all vehicle time is over");
                solution.setSolutionComplete(false);
                solution.setDetails("equalNodesDestinationChooser,number of location: "+ i);
                return solution;
            }
            while (!Util.allNodeAreDifferent(possibleNextNode)) {
                if(!equalNodesDestinationChooser())
                {
                    System.out.println("all vehicle time is over");
                    solution.setSolutionComplete(false);
                    solution.setDetails("equalNodesDestinationChooser,number of location: "+ i);
                    return solution;
                }
                if (stopWhileOfDestinationChooser == 100)
                    throw new Exception("stopWhileOfDestinationChooser");
                stopWhileOfDestinationChooser++;
            }

            for (int k = 0; k < nVehicles; k++) {
                vehicleInfo = vehicleInfoList.get(k);
                nextNode = possibleNextNode[k];
                if(nextNode==null && vehicleInfo.isTimeOver())
                    continue;
                solution.getVehicleSeqStopAtLocations()[k][vehicleInfo.getCurrentPosition().getId() - 1][nextNode.getId() - 1] = 1;
                solution.getTimeVehicleStartsAtLocation()[k][nextNode.getId() - 1] = vehicleInfo.getPossibleTimeToArriveToNextNode();
                solution.getLoadOfVehicleAtLocation()[k][nextNode.getId() - 1] = vehicleInfo.getPossiblePassengerDestination().size();
                solution.getBatteryLoadOfVehicleAtLocation()[k][nextNode.getId() - 1] = vehicleInfo.getPossibleBatteryLevel();
                if (nextNode.getNodeType().equals(NodeType.CHARGE))
                    solution.getChargingTimeOfVehicleAtStation()[k][nextNode.getId() - (instance.getChargingStationId()[0])] = vehicleInfo.getTimeSpendAtCharging();
                //solution.getExcessRideTimeOfPassenger()

                if (nextNode.getNodeType().equals(NodeType.PICKUP) || nextNode.getNodeType().equals(NodeType.DROPOFF)) {
                    {
                        i++;
                        if(nextNode.getNodeType().equals(NodeType.PICKUP))
                        {
                            Node des =notVisitedNodesMap.remove(nextNode);
                            mulo.put(nextNode,des);
                        }
                    }
                }
                vehicleInfo.update(nextNode);

                System.out.println( "NVN:" + i +
                        " VId:" + k +
                        " Tempo:"+String.format("%f",vehicleInfo.getTimeOfMission())+
                        " Pos:" + vehicleInfo.getCurrentPosition().getId()+
                        " Passeggeri: "+vehicleInfo.getPassengerDestination()+
                        " Charge"+vehicleInfo.getCurrentBatteryLevel());
            }



        }
        solution.setSolutionComplete(true);
        return solution;
    }

    private boolean deafultDestinationChooser() throws Exception {
        System.out.println("deafultDestinationChooser");
        int timeOverCounter=0;
        VehicleInfo vehicleInfo;
        Node nextNode;
        for (int i = 0; i < nVehicles; i++) {
            vehicleInfo = vehicleInfoList.get(i);
            if(!vehicleInfo.isTimeOver()) {

                NodeType nextNodeType = nodeTypeChooser(vehicleInfo);
                possibleNextNode[i] = (choseNextNode(vehicleInfo, nextNodeType, notVisitedNodesMap));
            }
            else
            {
                System.out.println(vehicleInfo.getVehicleId()+"time is over");
                possibleNextNode[i] = null;
                timeOverCounter++;

            }
        }
        if(timeOverCounter==nVehicles)
        {
            System.err.println("all vehicle times is over");
            return false;
        }
        else {
            return true;
        }
    }

    private boolean equalNodesDestinationChooser() throws Exception {


        System.out.println("equalNodesDestinationChooser");

        HashMap<Node,Node> newMap = new HashMap<>(notVisitedNodesMap);
        if(vehicleInfoList.get(0).isTimeOver()&&vehicleInfoList.get(1).isTimeOver())
        {
            return false;
        }
        else if (vehicleInfoList.get(0).isTimeOver()&&!vehicleInfoList.get(1).isTimeOver())
        {
            System.out.println();
        }else if(!vehicleInfoList.get(0).isTimeOver()&&vehicleInfoList.get(1).isTimeOver())
        {
            System.out.println();
        }
        else {
            if (vehicleInfoList.get(0).getPossibleDistanceFromPossibleDestination() < vehicleInfoList.get(1).getPossibleDistanceFromPossibleDestination()) {
                if (possibleNextNode[0].getNodeType().equals(NodeType.PICKUP))
                    newMap.remove(possibleNextNode[0]);
                else if (possibleNextNode[0].getNodeType().equals(NodeType.DROPOFF))
                    newMap.values().removeIf(value -> value.equals(possibleNextNode[0]));

                vehicleInfoList.get(1).getPossiblePassengerDestination().clear();
                vehicleInfoList.get(1).getPossiblePassengerDestination().addAll(vehicleInfoList.get(1).getPassengerDestination());
                possibleNextNode[1] = choseNextNode(vehicleInfoList.get(1), nodeTypeChooser(vehicleInfoList.get(1)), newMap);
            } else {
                if (possibleNextNode[1].getNodeType().equals(NodeType.PICKUP))
                    newMap.remove(possibleNextNode[1]);
                else if (possibleNextNode[1].getNodeType().equals(NodeType.DROPOFF))
                    newMap.values().removeIf(value -> value.equals(possibleNextNode[1]));

                vehicleInfoList.get(0).getPossiblePassengerDestination().clear();
                vehicleInfoList.get(0).getPossiblePassengerDestination().addAll(vehicleInfoList.get(0).getPassengerDestination());
                possibleNextNode[0] = choseNextNode(vehicleInfoList.get(0), nodeTypeChooser(vehicleInfoList.get(0)), newMap);
            }
        }
        return true;



    }

    private NodeType nodeTypeChooser(VehicleInfo vehicleInfo){
        NodeType nextNodeType = NodeType.NONE;
        //Node fartherChargingNode = getFartherChargingNode(vehicleInfo.getCurrentPosition());
        //if (getBatteryConsumptionFrom(vehicleInfo.getCurrentPosition(), fartherChargingNode) < vehicleInfo.getCurrentBatteryLevel()) {
        if (vehicleInfo.getCurrentBatteryLevel()>=1) {
            if (vehicleInfo.getPassengerDestination().size() == 0)
                nextNodeType = NodeType.PICKUP;
            else if (vehicleInfo.getPassengerDestination().size() == vehicleInfo.getMaxLoad())
                nextNodeType = NodeType.DROPOFF;
            else
                nextNodeType = NodeType.PICKUP_DROPOFF;
        } else {
            nextNodeType = NodeType.CHARGE;
        }
        return nextNodeType;
    }

    private Node choseNextNode(VehicleInfo vehicleInfo, NodeType nextNodeType, HashMap<Node,Node> nodes) throws Exception {
        PairNodeDouble pairNodeDouble=null;
        Node nextNode = null;

        switch (nextNodeType) {
            case PICKUP: {
                pairNodeDouble = getPickupNode(vehicleInfo, nodes);
                break;
            }
            case DROPOFF: {
                pairNodeDouble = getDropoffNode(vehicleInfo,nodes);
                break;
            }
            case CHARGE: {
                pairNodeDouble = getChargingNode(vehicleInfo,nodes);

                break;
            }
            case PICKUP_DROPOFF: {
                pairNodeDouble = getPickupDropoffNode(vehicleInfo, nodes);
            }
        }
        if(pairNodeDouble==null && vehicleInfo.isTimeOver())
            return null;
        nextNode = pairNodeDouble.getNode();
        vehicleInfo.setPossibleTimeToArriveToNextNode(pairNodeDouble.getValue());


        return nextNode;
    }

    private PairNodeDouble getPickupNode(VehicleInfo vehicleInfo, HashMap<Node,Node> nodes ) throws Exception {
        if(vehicleInfo.isTimeOver())
            return null;

        int vehicleId = vehicleInfo.getVehicleId();
        System.out.println(vehicleId+"-->getPickupNode");


        HashMap<PairOfNodes, Double> mapPair = new HashMap<>();

        double wait = 0;

        while (mapPair.size() == 0) {
            mapPair= getInTimePickupNodes(vehicleInfo,nodes,wait);
            if(vehicleInfo.isTimeOver())
                return null;

            if (mapPair.size() == 0)
                wait += 0.1;
        }
        System.out.println("Wait: "+wait);

        PairOfNodesDouble pairOfNodesDouble = getBestPickupNode(vehicleInfo,mapPair,wait);
        vehicleInfo.getPossiblePassengerDestination().add(pairOfNodesDouble.getPairOfNodes().getDropoff());
        vehicleInfo.setPossibleDistanceFromPossibleDestination(pairOfNodesDouble.getValue());
        return new PairNodeDouble(pairOfNodesDouble.getPairOfNodes().getPickup(),pairOfNodesDouble.getValue());


    }

    private PairNodeDouble getDropoffNode(VehicleInfo vehicleInfo,HashMap<Node,Node> nodes) throws Exception {
        if(vehicleInfo.isTimeOver())
            return null;

        int vehicleId = vehicleInfo.getVehicleId();
        System.out.println(vehicleId+"-->getDropoffNode");

        HashMap<Node, Double> mapDestination = new HashMap<Node, Double>();

        double wait = 0;

        while (mapDestination.size() == 0) {
            mapDestination = getInTimeDropoffNodes(vehicleInfo,wait);

            if (mapDestination.size() == 0)
                wait += 0.1;
        }
        System.out.println("Wait: "+wait);

        if(wait>0 && !vehicleInfo.isFullyCharged()) {
            PairNodeDouble chargingOccasion = chargingOccasion(vehicleInfo,wait,mapDestination);
            if((chargingOccasion!=null))
                return chargingOccasion(vehicleInfo,wait,mapDestination);
        }
        PairNodeDouble pairNodeDouble = getBestDropoffNode(vehicleInfo, mapDestination, wait);
        vehicleInfo.getPossiblePassengerDestination().remove(pairNodeDouble.getNode());
        return pairNodeDouble;




    }

    private PairNodeDouble getPickupDropoffNode(VehicleInfo vehicleInfo, HashMap<Node,Node> nodes) throws Exception {
        if(vehicleInfo.isTimeOver())
            return null;
        int vehicleId = vehicleInfo.getVehicleId();
        System.out.println(vehicleId+"-->getPickupDropoffNode");

        Node node = null;


        HashMap<Node, Double> map = new HashMap<>();

        HashMap<Node, Double> resultDeparture;
        HashMap<Node, Double> resultDistance;

        HashMap<PairOfNodes, Double> mapStarts = new HashMap<>();
        HashMap<Node, Double> mapDestination = new HashMap<Node, Double>();
        double wait = 0;
        while (mapDestination.size()+mapStarts.size() == 0) {

            mapStarts = getInTimePickupNodes(vehicleInfo,nodes,wait);
            if(vehicleInfo.isTimeOver())
                return null;


            if(vehicleInfo.getPassengerDestination().size()!=0){
                mapDestination = getInTimeDropoffNodes(vehicleInfo,wait);
                if(vehicleInfo.isTimeOver())
                    return null;

            }

            if (mapDestination.size() +mapStarts.size() == 0)
                wait += 0.1;
        }
        System.out.println("Wait: "+wait);
        if(mapDestination.size()!=0 && wait>0 && !vehicleInfo.isFullyCharged()) {

            PairNodeDouble chargingOccasion = chargingOccasion(vehicleInfo,wait,mapDestination);
            if((chargingOccasion!=null))
                return chargingOccasion(vehicleInfo,wait,mapDestination);
        }




        PairOfNodesDouble pickup  = null;
        PairNodeDouble dropoff = null;
        if(mapDestination.size()==0)
        {
            pickup = getBestPickupNode(vehicleInfo,mapStarts,wait);
            node = pickup.getPairOfNodes().getPickup();
            vehicleInfo.getPossiblePassengerDestination().add(pickup.getPairOfNodes().getDropoff());
            return new PairNodeDouble(node,pickup.getValue());
        }
        else if(mapStarts.size()==0)
        {
            dropoff  = getBestDropoffNode(vehicleInfo,mapDestination,wait);
            node = dropoff.getNode();
            vehicleInfo.getPossiblePassengerDestination().remove(node);
            return dropoff;
        }
        else {
            pickup = getBestPickupNode(vehicleInfo, mapStarts,wait);
            dropoff = getBestDropoffNode(vehicleInfo, mapDestination, wait);


            map.put(pickup.getPairOfNodes().getPickup(), pickup.getValue());
            map.put(dropoff.getNode(), dropoff.getValue());


            resultDistance = Util.orderNodeDoubleMapBy(map, Order.DISTANCE);
            Node favoriteDestination = (Node) resultDistance.keySet().toArray()[0];
            double distanceOfFavoritedestination = (double) resultDistance.values().toArray()[0];

            if (favoriteDestination.getNodeType().equals(NodeType.DROPOFF)) {
                node = favoriteDestination;
                vehicleInfo.getPossiblePassengerDestination().remove(node);
                return new PairNodeDouble(node, distanceOfFavoritedestination);
            }


            resultDeparture = Util.orderNodeDoubleMapBy(map, Order.DEPARTURE);

            Node possibleDestination = (Node) resultDeparture.keySet().toArray()[0];
            double DistanceOfPossibledestination = (double) resultDeparture.values().toArray()[0];

            if (!favoriteDestination.equals(possibleDestination)) {
                double d = distanceOfFavoritedestination +
                        computeTimeToArriveToNextNode(vehicleId, favoriteDestination, possibleDestination, wait) +
                        DistanceOfPossibledestination;

                if (d > possibleDestination.getDeparture()) {
                    favoriteDestination = possibleDestination;
                    distanceOfFavoritedestination = DistanceOfPossibledestination;
                }
            }

            node = favoriteDestination;
            if (favoriteDestination.getNodeType().equals(NodeType.PICKUP))
                vehicleInfo.getPossiblePassengerDestination().add(node);
            else
                vehicleInfo.getPossiblePassengerDestination().remove(node);
            return new PairNodeDouble(node, distanceOfFavoritedestination);
        }
    }

    private PairNodeDouble getChargingNode(VehicleInfo vehicleInfo, HashMap<Node,Node> nodes) throws Exception {
        if(vehicleInfo.isTimeOver())
            return null;
        PairNodeDouble closestChargingNode = getClosestChargingNode(vehicleInfo);
        HashMap <Node,Double> mapDestination = new HashMap<>();
        if(vehicleInfo.getPassengerDestination().size()!=0)
        {
            double wait = 0;
            while (mapDestination.size() == 0) {
                mapDestination = getInTimeDropoffNodes(vehicleInfo,0);
                if(vehicleInfo.isTimeOver())
                    return null;
                if (mapDestination.size() == 0)
                    wait += 0.1;
            }
            System.out.println("Wait: "+wait);
            PairNodeDouble chargingOccasion = chargingOccasion(vehicleInfo,wait,mapDestination);
            if((chargingOccasion!=null))
                return chargingOccasion(vehicleInfo,wait,mapDestination);
        }
        else

            ifInTime(vehicleInfo,closestChargingNode.getNode(),5);
        return new PairNodeDouble(closestChargingNode.getNode(),vehicleInfo.getPossibleTimeToArriveToNextNode());







    }

    private PairNodeDouble getClosestChargingNode(VehicleInfo vehicleInfo) throws Exception {
        Node node = null;
        HashMap<Node, Double> passeggerDestinationMap = new HashMap<Node, Double>();
        HashMap<PairOfNodes, Double> pickupMap = new HashMap<PairOfNodes, Double>();

        double distance = Double.MAX_VALUE;
        for (Node n : chargingStation) {
            if (travelTime[n.getId() - 1][vehicleInfo.getCurrentPosition().getId() - 1] < distance) {
                distance = travelTime[n.getId() - 1][vehicleInfo.getCurrentPosition().getId() - 1];
                node = n;
            }

        }
        return new PairNodeDouble(node,distance);
    }

    private HashMap<PairOfNodes,Double> getInTimePickupNodes(VehicleInfo vehicleInfo,HashMap<Node,Node> map,double wait) throws Exception {
        HashMap<PairOfNodes,Double> mapPair = new HashMap<>();
        Node start;
        Node destination;
        if(!Util.isTimeHorizonRespected(vehicleInfo.getVehicleId(),instance.getTimeHorizon(),vehicleInfo.getTimeOfMission(),wait))
        {
            vehicleInfo.setTimeOver(true);
            return null;
        }
        vehicleInfo.addWaitingTime(wait);
        for(Map.Entry e : map.entrySet())
        {
            start = (Node) e.getKey();
            destination =(Node) e.getValue();
            if(ifInTime(vehicleInfo, start,0) && ifInTime(vehicleInfo, destination,0))
            {
                mapPair.put(new PairOfNodes(start,destination),
                        computeTimeToArriveToNextNode(vehicleInfo.getVehicleId(),vehicleInfo.getCurrentPosition(),start,wait)+
                                computeTimeToArriveToNextNode(vehicleInfo.getVehicleId(),start,destination,wait)
                );
            }
            /*BoolDouble boolDouble = ifinTimePickup(vehicleInfo,start,destination,wait);
            if(boolDouble.isCheck())
            {
                mapPair.put(new PairOfNodes(start,destination),boolDouble.getValue());
            }*/

        }
        return mapPair;
    }

    private HashMap<Node,Double> getInTimeDropoffNodes(VehicleInfo vehicleInfo,double wait) throws Exception {

        HashMap<Node,Double> mapDestination = new HashMap<>();
        if(!Util.isTimeHorizonRespected(vehicleInfo.getVehicleId(),instance.getTimeHorizon(),vehicleInfo.getTimeOfMission(),wait))
        {
            vehicleInfo.setTimeOver(true);
            return null;
        }
        vehicleInfo.setWaitingTime(wait);
        for (Node n : vehicleInfo.getPassengerDestination()) {
            if (ifInTime(vehicleInfo, n, 0))
                mapDestination.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
        }
        return mapDestination;
    }

    private PairOfNodesDouble getBestPickupNode(VehicleInfo vehicleInfo,HashMap<PairOfNodes,Double> mapPair,double wait) {
        int vehicleId = vehicleInfo.getVehicleId();
        Node node = null;
        HashMap<PairOfNodes, Double> resultPairDistance;
        HashMap<PairOfNodes, Double> resultPairDeparture;

        resultPairDistance = Util.orderPairOfNodesDoubleMapBy(mapPair,Order.DISTANCE);


        if(resultPairDistance.size()==1)
        {
            node = ((PairOfNodes)resultPairDistance.keySet().toArray()[0]).getPickup();
            /*vehicleInfo.getPossiblePassengerDestination().add(((PairOfNodes)resultPairDistance.keySet().toArray()[0]).getDropoff());
            vehicleInfo.setPossibleDistanceFromPossibleDestination((Double) resultPairDistance.values().toArray()[0]);*/

            //return new PairNodeDouble(node, (Double) resultPairDistance.values().toArray()[0]);
            return new PairOfNodesDouble(((PairOfNodes)resultPairDistance.keySet().toArray()[0]),(Double) resultPairDistance.values().toArray()[0]);
        }
        else
        {
            resultPairDeparture = Util.orderPairOfNodesDoubleMapBy(mapPair,Order.DESTINATION_DEPARTURE);

            PairOfNodes favouritePairOfNodes= (PairOfNodes)resultPairDistance.keySet().toArray()[0];
            double distanceBetweenFavouritePairOfNodes = (Double) resultPairDistance.values().toArray()[0];

            PairOfNodes possiblePairOfNodes = (PairOfNodes) resultPairDeparture.keySet().toArray()[0];
            double distanceBetweenPossiblePairOfNodes = (Double) resultPairDeparture.values().toArray()[0];

            if (!possiblePairOfNodes.equals(favouritePairOfNodes)) {
                double d = distanceBetweenFavouritePairOfNodes +
                        computeTimeToArriveToNextNode(vehicleId, favouritePairOfNodes.getDropoff(), possiblePairOfNodes.getPickup(), wait) +
                        distanceBetweenPossiblePairOfNodes;
                if (d > possiblePairOfNodes.getDropoff().getDeparture()) {
                    favouritePairOfNodes = possiblePairOfNodes;
                    distanceBetweenFavouritePairOfNodes = distanceBetweenPossiblePairOfNodes;
                }
            }

            node = favouritePairOfNodes.getPickup();
            /*vehicleInfo.getPossiblePassengerDestination().add(favouritePairOfNodes.getDropoff());
            vehicleInfo.setPossibleDistanceFromPossibleDestination(distanceBetweenFavouritePairOfNodes);*/
            //return new PairNodeDouble(node,distanceBetweenFavouritePairOfNodes);
            return new PairOfNodesDouble(favouritePairOfNodes,distanceBetweenFavouritePairOfNodes);

        }
    }

    private PairNodeDouble getBestDropoffNode(VehicleInfo vehicleInfo,HashMap<Node,Double> mapDestination,double wait) {

        Node node = null;
        int vehicleId = vehicleInfo.getVehicleId();
        HashMap<Node, Double> resultDeparture;
        HashMap<Node, Double> resultDistance;

        resultDistance = Util.orderNodeDoubleMapBy(mapDestination,Order.DISTANCE);

        if(resultDistance.size()==1)
        {
            node = (Node) resultDistance.keySet().toArray()[0];
            //vehicleInfo.getPossiblePassengerDestination().remove(node);
            return new PairNodeDouble(node, (Double) resultDistance.values().toArray()[0]);
        }
        else
        {
            resultDeparture = Util.orderNodeDoubleMapBy(mapDestination,Order.DEPARTURE);

            Node favoriteDestination = (Node) resultDistance.keySet().toArray()[0];;
            double distanceOfFavoritedestination = (double) resultDistance.values().toArray()[0];

            Node possibleDestination = (Node) resultDeparture.keySet().toArray()[0];;
            double DistanceOfPossibledestination = (double) resultDeparture.values().toArray()[0];

            if(!favoriteDestination.equals(possibleDestination))
            {
                double d = distanceOfFavoritedestination+
                        computeTimeToArriveToNextNode(vehicleId,favoriteDestination,possibleDestination,wait)+
                        DistanceOfPossibledestination;

                if(d>possibleDestination.getDeparture()){
                    favoriteDestination=possibleDestination;
                }
            }

            node = favoriteDestination;
            //vehicleInfo.getPossiblePassengerDestination().remove(node);
            return new PairNodeDouble(node,distanceOfFavoritedestination);


        }
    }

    PairNodeDouble chargingOccasion(VehicleInfo vehicleInfo,double wait,HashMap<Node,Double> mapDestination) throws Exception {

        mapDestination = Util.orderNodeDoubleMapBy(mapDestination,Order.DEPARTURE);
        Node nextDestination = (Node) mapDestination.keySet().toArray()[0];
        double timeToNextDestination= (double) mapDestination.values().toArray()[0];

        PairNodeDouble closestChargingNode = getClosestChargingNode(vehicleInfo);


        double d = closestChargingNode.getValue()+ computeTimeToArriveToNextNode(vehicleInfo.getVehicleId(),closestChargingNode.getNode(),nextDestination,wait);
        if((d+vehicleInfo.getTimeOfMission())<nextDestination.getArrival())
        {
            double timeForCharge = nextDestination.getArrival()-d-vehicleInfo.getTimeOfMission();
            System.out.println("mi carico di: "+ timeForCharge);

            ifInTime(vehicleInfo,closestChargingNode.getNode(),timeForCharge);
            return new PairNodeDouble(closestChargingNode.getNode(),vehicleInfo.getPossibleTimeToArriveToNextNode());
        }


        return null;
    }

    private double computeTimeToArriveToNextNode(int vehicleId, Node start, Node arrive, double wait) {
        double standardTime = solution.getTimeVehicleStartsAtLocation()[vehicleId - 1][start.getId() - 1];
        double travelTime = getTravelTimeFrom(start, arrive) + wait;
        double additionalTime = start.getServiceTime();
        double tdij = standardTime + travelTime + additionalTime;
        return tdij;
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

    private double getTravelTimeFrom(Node startNode, Node arriveNode) {
        return travelTime[startNode.getId() - 1][arriveNode.getId() - 1];
    }

    private double getBatteryConsumptionFrom(Node startNode, Node arriveNode) {
        return instance.getBatteryConsumption()[startNode.getId() - 1][arriveNode.getId() - 1];
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
            vehicleInfo.setPossibleBatteryLevel(vehicleInfo.getCurrentBatteryLevel()+charge);
        }
        vehicleInfo.setTimeSpendAtCharging(timeinCharging);

        return timeinCharging;
    }
}
