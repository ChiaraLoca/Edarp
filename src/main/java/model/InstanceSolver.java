package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceSolver {

    private final Instance instance;

    private List<List<List<VehicleInfo>>> listOfPossibleSolution = new ArrayList<>();
    private List<SolutionHolder> listOfPossibleSolutionHolders = new ArrayList<>();
    private List<List<List<WaitingInfo>>> infos = new ArrayList<>();

    private List<List<List<VehicleInfo>>> listOfPossibleSolutionOptimized;
    private List<Solution> listOfSolution;
    private Solution theSolution;

    public InstanceSolver(Instance instance) {
        this.instance = instance;
        listOfPossibleSolution = new ArrayList<>();
    }

    public void solve() throws Exception {

        List<VehicleInfo> vehicleInfos = new ArrayList<>();
        boolean batteryCheat = false;
        for (int i = 0; i < instance.getnVehicles(); i++) {
            vehicleInfos.add(new VehicleInfo(
                    i + 1,
                    instance.getNodes().get(instance.getArtificialOriginDepotId()[i]),
                    instance.getNodes().get(instance.getArtificialDestinationDepotId()[i]),
                    batteryCheat ? 100 : instance.getVehicleBatteryCapacity()[i],
                    instance.getVehicleCapacity()[i])) ;
        }
        Map<Node, Node> unvisitedNodesMap= new HashMap<>();
        for (Node n : instance.getPickupAndDropoffLocations()) {
            if (n.getNodeType().equals(NodeType.PICKUP))
                unvisitedNodesMap.put(n, instance.getPickupAndDropoffLocations().get(n.getId() + instance.getnCustomers() - 1));
        }
        unvisitedNodesMap = Util.orderNodeNodeMapBy(unvisitedNodesMap,Order.DESTINATION_DEPARTURE);
        BruteSolver bruteSolver = new BruteSolver(vehicleInfos, instance, unvisitedNodesMap, 10);
        bruteSolver.start();


        listOfPossibleSolution.add(bruteSolver.getSolution());
        infos.add(bruteSolver.getWaits());


        listOfPossibleSolutionHolders=bruteSolver.getSolutionList();

        Charger charger = new Charger(instance,listOfPossibleSolution,infos);
        listOfPossibleSolutionOptimized = charger.optimizeAll();

        Translate translate = new Translate(instance, listOfPossibleSolutionOptimized);

        listOfSolution = translate.translateAll();

        System.out.println();
    }
}
