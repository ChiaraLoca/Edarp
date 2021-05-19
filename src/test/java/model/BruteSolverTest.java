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

class BruteSolverTest {

    @Test
    void isPossibleNodeTest() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        List<VehicleInfo> vehicleInfos = new ArrayList<>();
        boolean batteryCheat = true;
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
        BruteSolver bruteSolver = new BruteSolver(vehicleInfos, instance, unvisitedNodesMap, 1);
        for (Map.Entry<Node, Node> e: unvisitedNodesMap.entrySet()) {
            System.out.println(bruteSolver.isPossibleNode(new PairOfNodes(e.getKey(), e.getValue()), 0, vehicleInfos.get(0))+
                    "\t"+e.getKey()+"\t"+e.getValue()+"\t" +
                    "" +bruteSolver.computeTimeToArriveToNextNode(vehicleInfos.get(0).getCurrentPosition(), e.getKey(), 0, vehicleInfos.get(0))+
                    "\t"+bruteSolver.computeTimeToArriveToNextNode(e.getKey(), e.getValue(), 0, vehicleInfos.get(0)));
        }
        System.out.println();
    }
}