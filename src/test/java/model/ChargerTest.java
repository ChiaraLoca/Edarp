package model;

import org.junit.jupiter.api.Test;
import parser.InstanceReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChargerTest {

    @Test
    void getBestChargingStationTest() throws FileNotFoundException, ParseException {

        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
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
        BruteSolver bruteSolver = new BruteSolver(vehicleInfos, instance, unvisitedNodesMap);


        Charger charger = new Charger(bruteSolver);


        Node node = charger.getBestChargingStation((Node) unvisitedNodesMap.keySet().toArray()[1],(Node) unvisitedNodesMap.keySet().toArray()[3]);

        Util.printBlue(node.getId()+" - "+node.getLat()+" - "+node.getLon()+"\n");
        Util.printBlue(((Node) unvisitedNodesMap.keySet().toArray()[1]).getId()+" - "+((Node) unvisitedNodesMap.keySet().toArray()[1]).getLat()+" - "+((Node) unvisitedNodesMap.keySet().toArray()[1]).getLon()+"\n");
        Util.printBlue(((Node) unvisitedNodesMap.keySet().toArray()[3]).getId()+" - "+((Node) unvisitedNodesMap.keySet().toArray()[3]).getLat()+" - "+((Node) unvisitedNodesMap.keySet().toArray()[3]).getLon()+"\n");
    }

    @Test
    void optimizeTest() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
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
        BruteSolver bruteSolver = new BruteSolver(vehicleInfos, instance, unvisitedNodesMap);
        bruteSolver.start();

        Charger charger = new Charger(bruteSolver);

        charger.optimize();
        System.out.println();
    }
}