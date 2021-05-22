package solver;

import model.*;
import solutionCheck.SolutionChecker;
import util.Order;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceSolver {

    private final Instance instance;

    private List<List<VehicleInfo>> solutionWithoutCharge = new ArrayList<>();
    private List<List<WaitingInfo>> infos = new ArrayList<>();

    private List<List<VehicleInfo>> solutionWithCharge = new ArrayList<>();

    private Solution theSolution;

    public InstanceSolver(Instance instance) {
        this.instance = instance;

    }

    public void solve() throws Exception {

        //inizializzazione vehicleInfo
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
        //inizializzazione e ordinamento map dei nodi di pickup con relativa dropoff
        Map<Node, Node> unvisitedNodesMap= new HashMap<>();
        for (Node n : instance.getPickupAndDropoffLocations()) {
            if (n.getNodeType().equals(NodeType.PICKUP))
                unvisitedNodesMap.put(n, instance.getPickupAndDropoffLocations().get(n.getId() + instance.getnCustomers() - 1));
        }
        unvisitedNodesMap = Util.orderNodeNodeMapBy(unvisitedNodesMap, Order.DESTINATION_DEPARTURE);

        //SOLVER
        Solver bruteSolver = new Solver(vehicleInfos, instance, unvisitedNodesMap);
        bruteSolver.start();
        solutionWithoutCharge = bruteSolver.getSolution();
        infos = bruteSolver.getWaits();

        //CHARGER
        Charger charger = new Charger(instance,solutionWithoutCharge,infos);
        solutionWithCharge = charger.optimize();

        //TRANSLATE
        Translator translate = new Translator(instance, solutionWithCharge);
        theSolution = translate.translate();

        //CHECKER
        SolutionChecker solutionChecker = new SolutionChecker(theSolution);
        solutionChecker.checkAll();

    }








    public Instance getInstance() {
        return instance;
    }

    public Solution getTheSolution() {
        return theSolution;
    }
}

