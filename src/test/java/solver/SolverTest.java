package solver;

import model.Instance;
import model.Node;
import model.NodeType;
import model.VehicleInfo;
import org.junit.jupiter.api.Test;
import parser.InstanceReader;
import solver.Solver;
import util.Order;
import util.PairOfNodes;
import util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SolverTest {

    @Test
    void isPossibleNodeTest() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/main/resources/instances/u2-16-0.7.txt"),true);
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
        unvisitedNodesMap = Util.orderNodeNodeMapBy(unvisitedNodesMap, Order.DESTINATION_DEPARTURE);
        Solver bruteSolver = new Solver(vehicleInfos, instance, unvisitedNodesMap);
        for (Map.Entry<Node, Node> e: unvisitedNodesMap.entrySet()) {
            System.out.println(bruteSolver.isPossibleNode(new PairOfNodes(e.getKey(), e.getValue()), 0, vehicleInfos.get(0))+
                    "\t"+e.getKey()+"\t"+e.getValue()+"\t" +
                    "" +bruteSolver.computeTimeToArriveToNextNode(vehicleInfos.get(0).getCurrentPosition(), e.getKey(), 0, vehicleInfos.get(0))+
                    "\t"+bruteSolver.computeTimeToArriveToNextNode(e.getKey(), e.getValue(), 0, vehicleInfos.get(0)));
        }
        System.out.println();
    }
}