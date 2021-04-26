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
    private HashMap<Node, Node> mulo = new HashMap<>();
    private int numberOfNodes;

    public Solver(Instance instance) {
        this.instance = instance;
        allNodes = instance.getNodes();
        nVehicles = instance.getnVehicles();
        travelTime = instance.getTravelTime();
        notVisitedNodes = instance.getPickupAndDropoffLocations();
        chargingStation = instance.getChargingStationNodes();
        possibleNextNode = new Node[nVehicles];
        numberOfNodes = instance.getnCustomers()*2;


    }
    private HashMap<Node, Node> notVisitedNodesMap = new HashMap<>();

    public Solution solveProva() throws Exception {
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

        for (Node n : notVisitedNodes) {
            if (n.getNodeType().equals(NodeType.PICKUP))
                notVisitedNodesMap.put(n, notVisitedNodes.get(n.getId() + instance.getnCustomers() - 1));
        }
        notVisitedNodesMap = orderNodeNodeMapBy(notVisitedNodesMap,Order.DESTINATION_DEPARTURE);

        int stopWhileOfDestinationChooser=0;

        for(int i = 0; i< numberOfNodes;) {
            VehicleInfo vehicleInfo;
            Node nextNode;

            //seglie la tipologia di nodo a cui il veicolo è diretto e ci va.
            deafultDestinationChooser();
            while (!allNodeAreDifferent(possibleNextNode)) {
                equalNodesDestinationChooser();
                if (stopWhileOfDestinationChooser == 100)
                    throw new Exception("stopWhileOfDestinationChooser");
                stopWhileOfDestinationChooser++;
            }

            for (int k = 0; k < nVehicles; k++) {
                vehicleInfo = vehicleInfoList.get(k);
                nextNode = possibleNextNode[k];

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
                        " Passeggeri: "+vehicleInfo.getPassengerDestination());
            }



        }

        return solution;
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
                pairNodeDouble = getClosestChargingNode(vehicleInfo,nodes);
                break;
            }
            case PICKUP_DROPOFF: {
                pairNodeDouble = getPickupDropoffNode(vehicleInfo, nodes);
            }
        }
        nextNode = pairNodeDouble.getNode();
        vehicleInfo.setPossibleTimeToArriveToNextNode(pairNodeDouble.getValue());


        return nextNode;
    }

    private HashMap<PairOfNodes,Double> getInTimePickupNodes(VehicleInfo vehicleInfo,HashMap<Node,Node> map,double wait) throws Exception {
        HashMap<PairOfNodes,Double> mapPair = new HashMap<>();
        Node start;
        Node destination;
        Util.isTimeHorizonRespected(vehicleInfo.getVehicleId(),instance.getTimeHorizon(),vehicleInfo.getTimeOfMission(),wait);
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
        Util.isTimeHorizonRespected(vehicleInfo.getVehicleId(),instance.getTimeHorizon(),vehicleInfo.getTimeOfMission(),wait);
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

    private PairNodeDouble getPickupNode(VehicleInfo vehicleInfo, HashMap<Node,Node> nodes ) throws Exception {
        int vehicleId = vehicleInfo.getVehicleId();
        System.out.println(vehicleId+"-->getPickupNode");


        HashMap<PairOfNodes, Double> mapPair = new HashMap<>();

        double wait = 0;

        while (mapPair.size() == 0) {
            mapPair= getInTimePickupNodes(vehicleInfo,nodes,wait);

            if (mapPair.size() == 0)
                wait += 0.1;
        }

        PairOfNodesDouble pairOfNodesDouble = getBestPickupNode(vehicleInfo,mapPair,wait);
        vehicleInfo.getPossiblePassengerDestination().add(pairOfNodesDouble.getPairOfNodes().getDropoff());
        vehicleInfo.setPossibleDistanceFromPossibleDestination(pairOfNodesDouble.getValue());
        return new PairNodeDouble(pairOfNodesDouble.getPairOfNodes().getPickup(),pairOfNodesDouble.getValue());


    }

    private PairNodeDouble getDropoffNode(VehicleInfo vehicleInfo,HashMap<Node,Node> nodes) throws Exception {
        int vehicleId = vehicleInfo.getVehicleId();
        System.out.println(vehicleId+"-->getDropoffNode");

        HashMap<Node, Double> mapDestination = new HashMap<Node, Double>();

        double wait = 0;

        while (mapDestination.size() == 0) {
            mapDestination = getInTimeDropoffNodes(vehicleInfo,wait);
            if (mapDestination.size() == 0)
                wait += 0.1;
        }

        PairNodeDouble pairNodeDouble = getBestDropoffNode(vehicleInfo,mapDestination,wait);
        vehicleInfo.getPossiblePassengerDestination().remove(pairNodeDouble.getNode());
        return pairNodeDouble;


    }

    private PairNodeDouble getPickupDropoffNode(VehicleInfo vehicleInfo, HashMap<Node,Node> nodes) throws Exception {
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
            if(vehicleInfo.getPassengerDestination().size()!=0)
                mapDestination = getInTimeDropoffNodes(vehicleInfo,wait);

            if (mapDestination.size() +mapStarts.size() == 0)
                wait += 0.1;
        }
        PairOfNodesDouble pickup  = null;
        PairNodeDouble dropoff = null;
        if(mapDestination.size()==0)
        {
            pickup = getBestPickupNode(vehicleInfo,mapStarts,wait);
            node = pickup.getPairOfNodes().getPickup();
            vehicleInfo.getPossiblePassengerDestination().add(node);
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

    BoolDouble ifinTimePickup(VehicleInfo vehicleInfo, Node pickup,Node dropoff,double wait)
    {
        double timeFromCurrentToPickup = computeTimeToArriveToNextNode(vehicleInfo.getVehicleId(),vehicleInfo.getCurrentPosition(),dropoff,wait);
        double timeFromPickupToDropoff = computeTimeToArriveToNextNode(vehicleInfo.getVehicleId(),pickup,dropoff,wait);

        double totalTime = timeFromCurrentToPickup + timeFromPickupToDropoff;

        double arr = dropoff.getArrival();
        double dep = dropoff.getDeparture();
        boolean barr = totalTime > arr;
        boolean bdep = totalTime < dep;
        if (barr && bdep) {
            return new BoolDouble(true,totalTime);
        }
        return new BoolDouble(false,totalTime);

    }
    /****************************************************************************************/

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

        int stopWhileOfDestinationChoser=0;
        while (notVisitedNodes.size() != 0) {
            VehicleInfo vehicleInfo;
            Node nextNode;

            deafultDestinationChooser();
            while (!allNodeAreDifferent(possibleNextNode)) {
                equalNodesDestinationChooser();
                if(stopWhileOfDestinationChoser==100)
                    throw new Exception("stopWhileOfDestinationChoser");
                stopWhileOfDestinationChoser++;
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

                System.out.println( "NVN:" + notVisitedNodes.size() +
                                    " VId:" + i +
                                    " Tempo:"+String.format("%f",vehicleInfo.getTimeOfMission())+
                                    " Pos:" + vehicleInfo.getCurrentPosition().getId()+
                                    " Passeggeri: "+vehicleInfo.getPassengerDestination());
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
                nextNode = getClosestPickupNode(vehicleInfo, nodes);
                break;
            }
            case DROPOFF: {
                nextNode = getClosestDropoffNode(vehicleInfo);
                break;
            }
            case CHARGE: {
               // nextNode = getClosestChargingNode(vehicleInfo);
                break;
            }
            case PICKUP_DROPOFF: {
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

    private PairNodeDouble getClosestChargingNode(VehicleInfo vehicleInfo,HashMap<Node,Node> nodes) throws Exception {
        Node node = null;
        HashMap<Node, Double> passeggerDestinationMap = new HashMap<Node, Double>();
        HashMap<PairOfNodes, Double> pickupMap = new HashMap<PairOfNodes, Double>();
        double wait=0;
        double distance = Double.MAX_VALUE;
        for (Node n : chargingStation) {
            if (travelTime[n.getId() - 1][vehicleInfo.getCurrentPosition().getId() - 1] < distance) {
                distance = travelTime[n.getId() - 1][vehicleInfo.getCurrentPosition().getId() - 1];
                node = n;
            }

        }
        double value;
        if (vehicleInfo.getPassengerDestination().size() == 0) {

            while(pickupMap.size()==0) {
                pickupMap = getInTimePickupNodes(vehicleInfo, nodes, wait);
                if(pickupMap.size()==0)
                    wait +=0.1;
            }
            ifInTime(vehicleInfo, node, wait);
            value = vehicleInfo.getPossibleTimeToArriveToNextNode();

        } else {
            for (Node n : vehicleInfo.getPassengerDestination()) {
                passeggerDestinationMap.put(n, computeTimeToArriveToNextNode(vehicleInfo.getVehicleId(), node, n, 0));
            }

            Map<Node, Double> result = passeggerDestinationMap.entrySet()
                    .stream()
                    .sorted((o1, o2) -> {
                        return o1.getKey().getArrival() > o2.getKey().getArrival() ? 1 : -1;
                    })
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            double possibleTime4Charge = (double) result.values().toArray()[0];

            ifInTime(vehicleInfo, node, possibleTime4Charge+3);
            value = vehicleInfo.getPossibleTimeToArriveToNextNode();

        }

        if (node == null) {
            //aspetta
            throw new Exception("getClosestChargingNode");
        }
        vehicleInfo.getPossiblePassengerDestination().clear();
        vehicleInfo.getPossiblePassengerDestination().addAll(vehicleInfo.getPassengerDestination());

        return new PairNodeDouble(node,value);
    }

    private Node getPickupDropoffNode(VehicleInfo vehicleInfo, List<Node> nodes) throws Exception {
        System.out.println("getPickupDropoffNode");
        Node node = null;
        HashMap<Node, Double> mapPickupDropoff = new HashMap<Node, Double>();
        HashMap<Node, Double> mapExpiringDestination = new HashMap<Node, Double>();
        Map<Node, Double> resultExpiringDropoff;
        Map<Node, Double> resultPickupDropoff;
        double wait = 0;

        HashMap<Node, Double> mapPickup = new HashMap<Node, Double>();
        HashMap<Node, Double> mapDropoff = new HashMap<Node, Double>();
        HashMap<PairOfNodes, Double> mapPair = new HashMap<>();
        HashMap<PairOfNodes, Double> resultPair = new HashMap<>();
        Map<Node, Double> result;

        double nodeDistance =0;

        //while riempie 3 map: mapPickup, mapDropoff, mapExpiringDestination che contengono i nodi ragginugibili a questo tempo
        while (mapPickup.size()+mapDropoff.size()+mapExpiringDestination.size() == 0) {

            if ((vehicleInfo.getTimeOfMission()+wait)>instance.getTimeHorizon())
                throw new Exception(vehicleInfo.getVehicleId()+" Time is not enough: " + vehicleInfo.getTimeOfMission()+"wait: "+wait);
            vehicleInfo.addWaitingTime(wait);

            for (Node n : nodes) {
                if (ifInTime(vehicleInfo, n, 0))
                {
                    if(n.getNodeType().equals(NodeType.PICKUP))
                        mapPickup.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
                    else
                        mapDropoff.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
                }
            }
            for (Node n : vehicleInfo.getPassengerDestination()) {
                if (ifInTime(vehicleInfo, n, 0))
                {
                    mapExpiringDestination.put(n,vehicleInfo.getPossibleTimeToArriveToNextNode());
                }
            }

            if (mapPickup.size()+mapDropoff.size()+mapExpiringDestination.size() == 0)
                wait += 0.1;
        }



        if(mapDropoff.size()!=0) {
            for (Map.Entry e : mapPickup.entrySet()) {
                Node pickup = (Node) e.getKey();
                double pickupTime = (double) e.getValue();
                double dropoffTime = 0;
                Node destination = destinationMap.get(pickup);

                if (mapDropoff.containsKey(destination)) {
                    dropoffTime = mapDropoff.get(destination);
                    mapPair.put(new PairOfNodes(pickup,destination), pickupTime + dropoffTime);

                }
            }
        }
        if(mapPair.size()!=0) {
            resultPair = mapPair.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            if(resultPair.size()==1) {
                node = ((PairOfNodes) resultPair.keySet().toArray()[0]).getPickup();
                nodeDistance = (double) resultPair.values().toArray()[0];
            }
            else
            {
                PairOfNodes favouritePairOfNodes = (PairOfNodes) resultPair.keySet().toArray()[0];
                Double favouriteDistance = (Double) resultPair.values().toArray()[0];

                for(int i=1;i<resultPair.size();i++)
                {
                    PairOfNodes pairOfNodes = (PairOfNodes) resultPair.keySet().toArray()[i];
                    Double distance = (Double) resultPair.values().toArray()[i];
                    if(pairOfNodes.getDropoff().getArrival()<favouritePairOfNodes.getDropoff().getArrival())
                    {
                        favouritePairOfNodes = pairOfNodes;
                        favouriteDistance = distance;
                    }
                }

                node = favouritePairOfNodes.getPickup();
                nodeDistance = favouriteDistance;
            }
        }
        else {

            result = mapPickup.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            node = (Node) result.keySet().toArray()[0];
            nodeDistance = (double) result.values().toArray()[0];
        }

        if(mapExpiringDestination.size()>0)
        {
            Node node1;
            double d;
            HashMap<Node, Double> res = mapExpiringDestination;
            if(mapExpiringDestination.size()>1) {
                res = mapExpiringDestination.entrySet()
                        .stream()
                        .sorted((o1, o2) -> {return o1.getKey().getArrival() > o2.getKey().getArrival() ? 1 : -1;})
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            }
            node1 = (Node) res.keySet().toArray()[0];
            d= (double) res.values().toArray()[0];

            if(node1.getDeparture()<node.getDeparture())
                node = node1;
            else if(node1.getDeparture()==node.getDeparture())
            {
                if(d<nodeDistance)
                    node = node1;

            }

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

    private Node getClosestPickupNode(VehicleInfo vehicleInfo, List<Node> nodes ) throws Exception {
        System.out.println("getClosestPickupNode");
        Node node = null;
        double value = Double.MAX_VALUE;
        List<Node> possibleNodes = new ArrayList<>();
        HashMap<Node, Double> mapPickup = new HashMap<Node, Double>();
        HashMap<Node, Double> mapDropoff = new HashMap<Node, Double>();
        HashMap<PairOfNodes, Double> mapPair = new HashMap<>();
        HashMap<PairOfNodes, Double> resultPair = new HashMap<>();
        Map<Node, Double> result;

        double wait = 0;

        while (mapPickup.size()+mapDropoff.size() == 0) {
            if ((vehicleInfo.getTimeOfMission()+wait)>instance.getTimeHorizon())
                throw new Exception(vehicleInfo.getVehicleId()+" Time is not enough: " + vehicleInfo.getTimeOfMission()+"wait: "+wait);
            vehicleInfo.addWaitingTime(wait);
            //System.out.println("\taspetto " + wait);
            for (Node n : nodes) {
                if (ifInTime(vehicleInfo, n, 0))
                {
                    if(n.getNodeType().equals(NodeType.PICKUP))
                        mapPickup.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
                    else
                        mapDropoff.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
                }

            }
            if (mapPickup.size()+mapDropoff.size() == 0)
                wait += 0.1;
        }

            if(mapDropoff.size()!=0) {
                for (Map.Entry e : mapPickup.entrySet()) {
                    Node pickup = (Node) e.getKey();
                    double pickupTime = (double) e.getValue();
                    double dropoffTime = 0;
                    Node destination = destinationMap.get(pickup);

                    if (mapDropoff.containsKey(destination)) {
                        dropoffTime = mapDropoff.get(destination);
                        mapPair.put(new PairOfNodes(pickup,destination), pickupTime + dropoffTime);

                    }
                }
            }
            if(mapPair.size()!=0) {
                resultPair = mapPair.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                if(resultPair.size()==1)
                    node = ((PairOfNodes) resultPair.keySet().toArray()[0]).getPickup();
                else
                {
                    PairOfNodes favouritePairOfNodes = (PairOfNodes) resultPair.keySet().toArray()[0];
                    Double favouriteDistance = (Double) resultPair.values().toArray()[0];

                    for(int i=1;i<resultPair.size();i++)
                    {
                        PairOfNodes pairOfNodes = (PairOfNodes) resultPair.keySet().toArray()[i];
                        Double distance = (Double) resultPair.values().toArray()[i];
                        if(pairOfNodes.getDropoff().getArrival()<favouritePairOfNodes.getDropoff().getArrival())
                        {
                            favouritePairOfNodes = pairOfNodes;
                            favouriteDistance = distance;
                        }
                    }

                    node = favouritePairOfNodes.getPickup();
                }
            }
            else {

                result = mapPickup.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                node = (Node) result.keySet().toArray()[0];
            }



        vehicleInfo.getPossiblePassengerDestination().add(destinationMap.get(node));
        return node;
    }

    private Node getClosestDropoffNode(VehicleInfo vehicleInfo) throws Exception {
        //System.out.println("getClosestDropoffNode");
        Node node = null;
        double value = Double.MAX_VALUE;
        List<Node> possibleNodes = new ArrayList<>();
        HashMap<Node, Double> map = new HashMap<Node, Double>();
        double wait = 0;

        while (map.size() == 0) {
            if ((vehicleInfo.getTimeOfMission()+wait)>instance.getTimeHorizon())
                throw new Exception(vehicleInfo.getVehicleId()+" Time is not enough: " + vehicleInfo.getTimeOfMission()+"wait: "+wait);
            vehicleInfo.setWaitingTime(wait);
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
            vehicleInfo.setPossibleBatteryLevel(vehicleInfo.getCurrentBatteryLevel()+charge);
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
        System.out.println("deafultDestinationChooser");

        VehicleInfo vehicleInfo;
        Node nextNode;
        for (int i = 0; i < nVehicles; i++) {
            vehicleInfo = vehicleInfoList.get(i);


            NodeType nextNodeType = nodeTypeChooser(vehicleInfo);

            possibleNextNode[i] = (choseNextNode(vehicleInfo, nextNodeType, notVisitedNodesMap));
        }
    }

    private NodeType nodeTypeChooser(VehicleInfo vehicleInfo)
    {
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
        return nextNodeType;
    }

    private void equalNodesDestinationChooser() throws Exception {
        System.out.println("equalNodesDestinationChooser");

        HashMap<Node,Node> newMap = new HashMap<>(notVisitedNodesMap);

        if(vehicleInfoList.get(0).getPossibleDistanceFromPossibleDestination()<vehicleInfoList.get(1).getPossibleDistanceFromPossibleDestination())
        {
            if(possibleNextNode[0].getNodeType().equals(NodeType.PICKUP))
                newMap.remove(possibleNextNode[0]);
            else if(possibleNextNode[0].getNodeType().equals(NodeType.DROPOFF))
                newMap.values().removeIf(value -> value.equals(possibleNextNode[0]));

            vehicleInfoList.get(1).getPossiblePassengerDestination().clear();
            vehicleInfoList.get(1).getPossiblePassengerDestination().addAll(vehicleInfoList.get(1).getPassengerDestination());
            possibleNextNode[1]= choseNextNode(vehicleInfoList.get(1),nodeTypeChooser(vehicleInfoList.get(1)),newMap);
        }
        else
        {
            if(possibleNextNode[1].getNodeType().equals(NodeType.PICKUP))
                newMap.remove(possibleNextNode[1]);
            else if(possibleNextNode[1].getNodeType().equals(NodeType.DROPOFF))
                newMap.values().removeIf(value -> value.equals(possibleNextNode[1]));

            vehicleInfoList.get(0).getPossiblePassengerDestination().clear();
            vehicleInfoList.get(0).getPossiblePassengerDestination().addAll(vehicleInfoList.get(0).getPassengerDestination());
            possibleNextNode[0]= choseNextNode(vehicleInfoList.get(0),nodeTypeChooser(vehicleInfoList.get(0)),newMap);
        }





    }

    /**
     *
     * @param vehicleInfo
     * @param nodes
     * @param nodeType
     * @return Ritorna una mappa con tutti i nodi del tipo passato come parametro raggiungibili al momento
     */
    public Map<Node,Double> getInTimeNode(VehicleInfo vehicleInfo,List<Node> nodes, NodeType nodeType){
        HashMap<Node,Double> map = new HashMap<>();
        if(nodeType.equals(NodeType.PICKUP_DROPOFF))

        for (Node n : nodes) {
            if (ifInTime(vehicleInfo, n, 0))
            {
                if(nodeType.equals(NodeType.PICKUP_DROPOFF))
                {
                    if(n.getNodeType().equals(NodeType.PICKUP) || n.getNodeType().equals(NodeType.DROPOFF))
                        map.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
                }
                else if(n.getNodeType().equals(nodeType))
                    map.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
            }
        }
        return map;
    }

    /**
     *
     * @param map
     * @param order
     * @return Mappa ordidina in ordine crescente per l'elemento order
     */


    public HashMap<Node,Node> orderNodeNodeMapBy(Map<Node,Node>map,Order order) {

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
 class PairNodeDouble
 {
     private Node node;
     private double value;

     public PairNodeDouble(Node node, double value) {
         this.node = node;
         this.value = value;
     }

     public PairNodeDouble() {
     }

     public Node getNode() {
         return node;
     }

     public void setNode(Node node) {
         this.node = node;
     }

     public double getValue() {
         return value;
     }

     public void setValue(double value) {
         this.value = value;
     }
 }

 class PairOfNodesDouble{
    private PairOfNodes pairOfNodes;
    private double value;

     public PairOfNodesDouble(PairOfNodes pairOfNodes, double value) {
         this.pairOfNodes = pairOfNodes;
         this.value = value;
     }

     public PairOfNodes getPairOfNodes() {
         return pairOfNodes;
     }

     public void setPairOfNodes(PairOfNodes pairOfNodes) {
         this.pairOfNodes = pairOfNodes;
     }

     public double getValue() {
         return value;
     }

     public void setValue(double value) {
         this.value = value;
     }
 }

class BoolDouble
{
    private boolean check;
    private double value;

    public BoolDouble(boolean check, double value) {
        this.check = check;
        this.value = value;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}



