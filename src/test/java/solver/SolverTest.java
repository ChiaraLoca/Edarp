package solver;

import model.Instance;
import model.Node;
import model.NodeType;
import model.Solution;
import org.junit.jupiter.api.Test;
import parser.InstanceReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    @Test
    void solveTest() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        Solver solver = new Solver(instance);
        Solution solution = solver.solveProva();
        assert solution!=null;
        System.out.println(solution);
    }

    @Test
    void mapOrderTest() throws FileNotFoundException, ParseException {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        Solver solver = new Solver(instance);

        Map<Node,Double> nodeMap = new HashMap<>();
        Map<Node,Double> nodeOrderMap1 = new HashMap<>();
        Map<Node,Double> nodeOrderMap2 = new HashMap<>();
        for(Node n :instance.getNodes())
        {
            if(n.getNodeType().equals(NodeType.PICKUP) || n.getNodeType().equals(NodeType.DROPOFF))
                nodeMap.put(n,Math.random()*10);
        }
        //nodeOrderMap1 = solver.orderMapBy(nodeMap,Order.DISTANCE);
        //nodeOrderMap2 = solver.orderMapBy(nodeMap,Order.ARRIVAL);

        /*System.out.println(nodeMap);
        System.out.println(nodeOrderMap1);
        System.out.println(nodeOrderMap2);*/


    }
}