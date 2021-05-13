package solver;

import model.*;

import java.util.*;

public class OneVehicleSolver {

    final private Instance instance;
    final private boolean batteryCheat;
    final private int idVehicle;


    final private int nVehicles;
    final private double[][] travelTime;
    final private ArrayList<Node> chargingStation;
    final private int numberOfNodes;
    private Node possibleNextNode;

    final private Solution solution;

    private VehicleInfo vehicleInfo;
    private HashMap<Node, Node> unvisitedNodesMap = new HashMap<>();
    private List<ExpiredNodesTime> expiredNodes = new ArrayList<>();
    private List<ExpiredNodesTime> priortyNodes = new ArrayList<>();

    private int nVisitedNode=0;

    public OneVehicleSolver(Instance instance, boolean batteryCheat,int idVehicle,HashMap<Node,Node> unvisitedNodesMap,double timeAvailableToCharge,List<ExpiredNodesTime> priorityNodes) {
        this.instance = instance;
        this.batteryCheat = batteryCheat;
        this.idVehicle = idVehicle;
        this.unvisitedNodesMap = unvisitedNodesMap;


        this.nVehicles = instance.getnVehicles();
        this.travelTime = instance.getTravelTime();

        this.chargingStation = instance.getChargingStationNodes();


        this.possibleNextNode =null;
        this.solution = new Solution(instance);

        vehicleInfo= new VehicleInfo(
                idVehicle + 1,
                instance.getNodes().get(instance.getArtificialOriginDepotId()[idVehicle]),
                instance.getNodes().get(instance.getArtificialDestinationDepotId()[idVehicle]),
                batteryCheat?100:instance.getVehicleBatteryCapacity()[idVehicle],
                instance.getVehicleCapacity()[idVehicle]);

        vehicleInfo.setTimeAvailableToCharge(timeAvailableToCharge);
        this.numberOfNodes = unvisitedNodesMap.size()*2;
        this.priortyNodes = priorityNodes;

    }



    public void solve() throws Exception {

        for( nVisitedNode = 0; nVisitedNode< numberOfNodes;) {

            //seglie la tipologia di nodo a cui il veicolo è diretto e ci va.
            if(!deafultDestinationChooser()) {
                Util.printPurple("Vehicol time is over, Finish\n");
                solution.setSolutionComplete(false);
                solution.setDetails("equalNodesDestinationChooser, number of location: "+ nVisitedNode);

                return;
            }

            Node nextNode = possibleNextNode;
                if(nextNode==null && vehicleInfo.isTimeOver()) //TODO credo si possa togliere
                    continue;
                solution.getVehicleSeqStopAtLocations()[idVehicle][vehicleInfo.getCurrentPosition().getId() - 1][nextNode.getId() - 1] = 1;
                solution.getTimeVehicleStartsAtLocation()[idVehicle][nextNode.getId() - 1] = vehicleInfo.getPossibleTimeToArriveToNextNode();
                solution.getLoadOfVehicleAtLocation()[idVehicle][nextNode.getId() - 1] = vehicleInfo.getPossiblePassengerDestination().size();
                solution.getBatteryLoadOfVehicleAtLocation()[idVehicle][nextNode.getId() - 1] = vehicleInfo.getPossibleBatteryLevel();
                if (nextNode.getNodeType().equals(NodeType.CHARGE))
                    solution.getChargingTimeOfVehicleAtStation()[idVehicle][nextNode.getId() - (instance.getChargingStationId()[0])] = vehicleInfo.getTimeSpendAtCharging();
                //solution.getExcessRideTimeOfPassenger()

                if (nextNode.getNodeType().equals(NodeType.PICKUP) || nextNode.getNodeType().equals(NodeType.DROPOFF)) {
                    {
                        nVisitedNode++;
                        if(nextNode.getNodeType().equals(NodeType.PICKUP))
                        {
                            Node des = unvisitedNodesMap.remove(nextNode);

                        }
                    }
                }
                vehicleInfo.update(nextNode);

                    System.out.print("NVN:" + nVisitedNode +
                            " VId:" + idVehicle +
                            " Tempo:" + String.format("%f", vehicleInfo.getTimeOfMission()) +
                            " Pos:" + vehicleInfo.getCurrentPosition().getId() +
                            " Passeggeri: " + vehicleInfo.getPassengerDestination());
                    Util.printCyan(" Charge: " +
                            (batteryCheat ? 100-vehicleInfo.getCurrentBatteryLevel() : vehicleInfo.getCurrentBatteryLevel())+"\n");

                    Util.printGreen("Nodi scaduti");
                    for(Map.Entry e: unvisitedNodesMap.entrySet())
                    {
                        if(vehicleInfo.getTimeOfMission()>((Node)e.getValue()).getDeparture()) {
                            Util.printGreen(" - " + ((Node)e.getValue()).getId());

                            PairOfNodes pairOfNodes = new PairOfNodes((Node)e.getKey(),(Node)e.getValue());
                            boolean contains =false;
                            for(ExpiredNodesTime ent :expiredNodes)
                            {
                                if(ent.getExpiredNode().equals(pairOfNodes)){
                                    contains = true;
                                    break;
                                }
                            }
                            if(!contains) {
                                expiredNodes.add(new ExpiredNodesTime(
                                        pairOfNodes,
                                        null));
                            }
                            Util.printPurple("Nodo scaduto\n");
                            vehicleInfo.setTimeOver(true);
                        }
                    }
                    System.out.println("\n");
        }
        solution.setSolutionComplete(true);

        return ;
    }

    private boolean deafultDestinationChooser() throws Exception {

        NodeType nextNodeType = nodeTypeChooser();
        if (!vehicleInfo.isTimeOver()) {

            possibleNextNode = (choseNextNode(nextNodeType, unvisitedNodesMap));
            return true;
        } else {

            possibleNextNode = null;
            return false;

        }
    }

    private NodeType nodeTypeChooser(){
        Util.printBlue("TimeAvailableToCharge: "+vehicleInfo.getTimeAvailableToCharge()+"\n");
        NodeType nextNodeType = NodeType.NONE;
        //Node fartherChargingNode = getFartherChargingNode(vehicleInfo.getCurrentPosition());
        //if (getBatteryConsumptionFrom(vehicleInfo.getCurrentPosition(), fartherChargingNode) < vehicleInfo.getCurrentBatteryLevel()) {
        if (vehicleInfo.getCurrentBatteryLevel()>=1.6) {
            if (vehicleInfo.getPassengerDestination().size() == 0) {
                vehicleInfo.setLastTimeAtEmpty(vehicleInfo.getCurrentPosition());
                nextNodeType = NodeType.PICKUP;
            }
            else if (vehicleInfo.getPassengerDestination().size() == vehicleInfo.getMaxLoad())
                nextNodeType = NodeType.DROPOFF;
            else
                nextNodeType = NodeType.PICKUP_DROPOFF;
        } else {
            if(vehicleInfo.getTimeAvailableToCharge()<=0)
            {
                if(vehicleInfo.getPassengerDestination().size()==0)
                {
                    //nextNodeType = NodeType.CHARGE;
                    vehicleInfo.setTimeOver(true);
                }
                else
                    nextNodeType = NodeType.DROPOFF;
            }
            else
            {
                if(vehicleInfo.getPassengerDestination().size()==0) {
                    nextNodeType = NodeType.CHARGE;

                }
                else
                    nextNodeType = NodeType.DROPOFF;
            }



        }
        return nextNodeType;
    }

    private Node choseNextNode(NodeType nextNodeType, HashMap<Node,Node> nodes) throws Exception {
        PairNodeDouble pairNodeDouble=null;
        Node nextNode = null;

        switch (nextNodeType) {
            case PICKUP: {
                pairNodeDouble = getPickupNode(nodes);
                break;
            }
            case DROPOFF: {
                pairNodeDouble = getDropoffNode(nodes);
                break;
            }
            case CHARGE: {
                pairNodeDouble = getChargingNode();
                vehicleInfo.setTimeAvailableToCharge(0);
                break;
            }
            case PICKUP_DROPOFF: {
                pairNodeDouble = getPickupDropoffNode(nodes);
            }
        }
        if(pairNodeDouble==null && vehicleInfo.isTimeOver())
            return null;
        nextNode = pairNodeDouble.getNode();
        vehicleInfo.setPossibleTimeToArriveToNextNode(pairNodeDouble.getValue());


        return nextNode;
    }

    private PairNodeDouble getPickupNode(HashMap<Node,Node> nodes ) throws Exception {
        if(vehicleInfo.isTimeOver())
            return null;
        int vehicleId = vehicleInfo.getVehicleId();
        HashMap<PairOfNodes, Double> mapPair = new HashMap<>();

        double wait = 0;
        if(priortyNodes!=null
                &&priortyNodes.size()>0
                && (priortyNodes.get(0).getLastEmptyAtNode()!=null
                        && vehicleInfo.getCurrentPosition().equals(priortyNodes.get(0).getLastEmptyAtNode()))
                && !expiredNodes.contains(priortyNodes.get(0)))
        {


            while (!mapPair.containsKey(priortyNodes.get(0).getExpiredNode())) {
                mapPair = getInTimePickupNodes(nodes, wait);
                if (vehicleInfo.isTimeOver())
                    return null;

                if (!mapPair.containsKey(priortyNodes.get(0).getExpiredNode()))
                    wait += 0.1;
            }
        }
        else {

            while (mapPair.size() == 0) {
                mapPair = getInTimePickupNodes(nodes, wait);
                if (vehicleInfo.isTimeOver())
                    return null;

                if (mapPair.size() == 0)
                    wait += 0.1;
            }

        }
        //Util.printGreen("Wait: "+wait+"\n");
        /*if(wait>0 && batteryCheat)
        {
            if(chargingOccasions[vehicleId-1]==null)
                chargingOccasions[vehicleId-1]=new WaitingTimeInstances(vehicleInfo.getCurrentPosition(),wait,lastEmptyLocation[vehicleId-1]);

        }*/

        PairOfNodesDouble pairOfNodesDouble = getBestPickupNode(mapPair,wait);
        vehicleInfo.getPossiblePassengerDestination().add(pairOfNodesDouble.getPairOfNodes().getDropoff());
        vehicleInfo.setPossibleDistanceFromPossibleDestination(pairOfNodesDouble.getValue());
        return new PairNodeDouble(pairOfNodesDouble.getPairOfNodes().getPickup(),pairOfNodesDouble.getValue());


    }

    private PairNodeDouble getDropoffNode(HashMap<Node,Node> nodes) throws Exception {
        if(vehicleInfo.isTimeOver())
            return null;


        System.out.println(vehicleInfo.getVehicleId()+"-->getDropoffNode");

        HashMap<Node, Double> mapDestination = new HashMap<Node, Double>();

        double wait = 0;

        while (mapDestination.size() == 0) {
            mapDestination = getInTimeDropoffNodes(wait);
            if(vehicleInfo.isTimeOver())
                return null;
            if (mapDestination.size() == 0)
                wait += 0.1;
        }
        //Util.printGreen("Wait: "+wait+"\n");
        /*if(wait>0 && batteryCheat)
        {
            if(chargingOccasions[vehicleId-1]==null)
                chargingOccasions[vehicleId-1]=new WaitingTimeInstances(vehicleInfo.getCurrentPosition(),wait,lastEmptyLocation[vehicleId-1]);
        }*/

        /*if(wait>0 && !vehicleInfo.isFullyCharged()) {
            PairNodeDouble chargingOccasion = chargingOccasion(vehicleInfo,wait,mapDestination);
            if((chargingOccasion!=null))
                return chargingOccasion(vehicleInfo,wait,mapDestination);
        }*/
        PairNodeDouble pairNodeDouble = getBestDropoffNode(mapDestination, wait);
        vehicleInfo.getPossiblePassengerDestination().remove(pairNodeDouble.getNode());
        return pairNodeDouble;




    }

    private PairNodeDouble getPickupDropoffNode( HashMap<Node,Node> nodes) throws Exception {
        if(vehicleInfo.isTimeOver())
            return null;
        int vehicleId = vehicleInfo.getVehicleId();


        Node node = null;


        HashMap<Node, Double> map = new HashMap<>();

        HashMap<Node, Double> resultDeparture;
        HashMap<Node, Double> resultDistance;

        HashMap<PairOfNodes, Double> mapStarts = new HashMap<>();
        HashMap<Node, Double> mapDestination = new HashMap<Node, Double>();
        double wait = 0;
        while (mapDestination.size()+mapStarts.size() == 0) {

            mapStarts = getInTimePickupNodes(nodes,wait);
            if(vehicleInfo.isTimeOver())
                return null;


            if(vehicleInfo.getPassengerDestination().size()!=0){
                mapDestination = getInTimeDropoffNodes(wait);
                if(vehicleInfo.isTimeOver())
                    return null;

            }

            if (mapDestination.size() +mapStarts.size() == 0)
                wait += 0.1;
        }
        //Util.printGreen("Wait: "+wait+"\n");
        /*if(wait>0 && batteryCheat)
        {
            if(chargingOccasions[vehicleId-1]==null)
                chargingOccasions[vehicleId-1]=new WaitingTimeInstances(vehicleInfo.getCurrentPosition(),wait,lastEmptyLocation[vehicleId-1]);
        }*/
        /*if(mapDestination.size()!=0 && wait>0 && !vehicleInfo.isFullyCharged()) {

            PairNodeDouble chargingOccasion = chargingOccasion(vehicleInfo,wait,mapDestination);
            if((chargingOccasion!=null))
                return chargingOccasion(vehicleInfo,wait,mapDestination);
        }*/




        PairOfNodesDouble pickup  = null;
        PairNodeDouble dropoff = null;
        if(mapDestination.size()==0)
        {
            pickup = getBestPickupNode(mapStarts,wait);
            node = pickup.getPairOfNodes().getPickup();
            vehicleInfo.getPossiblePassengerDestination().add(pickup.getPairOfNodes().getDropoff());
            return new PairNodeDouble(node,pickup.getValue());
        }
        else if(mapStarts.size()==0)
        {
            dropoff  = getBestDropoffNode(mapDestination,wait);
            node = dropoff.getNode();
            vehicleInfo.getPossiblePassengerDestination().remove(node);
            return dropoff;
        }
        else {
            pickup = getBestPickupNode(mapStarts,wait);
            dropoff = getBestDropoffNode(mapDestination, wait);


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
                        computeTimeToArriveToNextNode(favoriteDestination, possibleDestination, wait) +
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

    private PairNodeDouble getChargingNode() throws Exception {

        if(vehicleInfo.isTimeOver())
            return null;
        PairNodeDouble closestChargingNode = getClosestChargingNode();
        ifInTime(closestChargingNode.getNode(),vehicleInfo.getTimeAvailableToCharge());
        return new PairNodeDouble(closestChargingNode.getNode(),vehicleInfo.getPossibleTimeToArriveToNextNode());
    }

    private PairNodeDouble getClosestChargingNode() throws Exception {
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

    private HashMap<PairOfNodes,Double> getInTimePickupNodes(HashMap<Node,Node> map,double wait) throws Exception {
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

            ifInTime(start,0);
            double timeToPickup = vehicleInfo.getPossibleTimeToArriveToNextNode();
            double timeTodropoff = computeTimeToArriveToNextNode(start,destination,wait);
            double userMaxRideTime = instance.getUserMaxRideTime()[start.getId()-1];

            double d1=(timeToPickup+timeTodropoff+wait);
            double d2=(timeToPickup+timeTodropoff+wait+userMaxRideTime);
            double d3=(destination.getArrival());
            //Util.printRed(""+d1+", "+d2+", "+d3);
            if((timeToPickup+timeTodropoff+wait+vehicleInfo.getTimeOfMission())<destination.getDeparture() && (wait+timeToPickup+timeTodropoff+userMaxRideTime+vehicleInfo.getTimeOfMission())>destination.getArrival())
            {
                //Util.printRed("---"+d1+", "+d2+", "+d3);
                mapPair.put(new PairOfNodes(start,destination),timeToPickup+timeTodropoff);

            }



        }
        return mapPair;
    }

    private HashMap<Node,Double> getInTimeDropoffNodes(double wait) throws Exception {

        HashMap<Node,Double> mapDestination = new HashMap<>();
        if(!Util.isTimeHorizonRespected(vehicleInfo.getVehicleId(),instance.getTimeHorizon(),vehicleInfo.getTimeOfMission(),wait))
        {
            vehicleInfo.setTimeOver(true);
            return null;
        }
        vehicleInfo.setWaitingTime(wait);
        for (Node n : vehicleInfo.getPassengerDestination()) {
            double time= computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),n,wait);
            if (time<n.getDeparture())
                mapDestination.put(n, vehicleInfo.getPossibleTimeToArriveToNextNode());
        }
        return mapDestination;
    }

    private PairOfNodesDouble getBestPickupNode(HashMap<PairOfNodes,Double> mapPair,double wait) {
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
            if(priortyNodes!=null &&priortyNodes.size()>0 && resultPairDistance.containsKey(priortyNodes.get(0).getExpiredNode()))
            {

                node = priortyNodes.get(0).getExpiredNode().getPickup();
                PairOfNodesDouble tmp = new PairOfNodesDouble(priortyNodes.get(0).getExpiredNode(),computeTimeToArriveToNextNode(vehicleInfo.getCurrentPosition(),node,wait));
                priortyNodes.remove(0);
                return tmp;
            }
            else {
                resultPairDeparture = Util.orderPairOfNodesDoubleMapBy(mapPair, Order.DESTINATION_DEPARTURE);

                PairOfNodes favouritePairOfNodes = (PairOfNodes) resultPairDistance.keySet().toArray()[0];
                double distanceBetweenFavouritePairOfNodes = (Double) resultPairDistance.values().toArray()[0];

                PairOfNodes possiblePairOfNodes = (PairOfNodes) resultPairDeparture.keySet().toArray()[0];
                double distanceBetweenPossiblePairOfNodes = (Double) resultPairDeparture.values().toArray()[0];

                if (!possiblePairOfNodes.equals(favouritePairOfNodes)) {
                    double d = distanceBetweenFavouritePairOfNodes +
                            computeTimeToArriveToNextNode(favouritePairOfNodes.getDropoff(), possiblePairOfNodes.getPickup(), wait) +
                            computeTimeToArriveToNextNode(possiblePairOfNodes.getPickup(), possiblePairOfNodes.getDropoff(), wait);

                    if (d > possiblePairOfNodes.getDropoff().getDeparture()) {
                        favouritePairOfNodes = possiblePairOfNodes;
                        distanceBetweenFavouritePairOfNodes = distanceBetweenPossiblePairOfNodes;
                    }

                }


                node = favouritePairOfNodes.getPickup();
            /*vehicleInfo.getPossiblePassengerDestination().add(favouritePairOfNodes.getDropoff());
            vehicleInfo.setPossibleDistanceFromPossibleDestination(distanceBetweenFavouritePairOfNodes);*/
                //return new PairNodeDouble(node,distanceBetweenFavouritePairOfNodes);
                return new PairOfNodesDouble(favouritePairOfNodes, distanceBetweenFavouritePairOfNodes);
            }
        }
    }

    private PairNodeDouble getBestDropoffNode(HashMap<Node,Double> mapDestination,double wait) {

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
                        computeTimeToArriveToNextNode(favoriteDestination,possibleDestination,wait)+
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

    private double computeTimeToArriveToNextNode( Node start, Node arrive, double wait) {
        double standardTime = solution.getTimeVehicleStartsAtLocation()[idVehicle][start.getId() - 1];
        double travelTime = getTravelTimeFrom(start, arrive) + wait;
        double additionalTime = start.getServiceTime();
        double tdij = standardTime + travelTime + additionalTime;
        return tdij;
    }

    private boolean ifInTime(Node possibleNode, double time) {


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

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public Solution getSolution() {
        return solution;
    }

    public HashMap<Node, Node> getUnvisitedNodesMap() {
        return unvisitedNodesMap;
    }

    public List<ExpiredNodesTime> getExpiredNodes() {
        return expiredNodes;
    }

    public void setPriortyNodes(List<ExpiredNodesTime> priortyNodes) {
        this.priortyNodes = priortyNodes;
    }
}
