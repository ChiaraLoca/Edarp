package solver;

import model.Instance;
import model.Solution;
import org.junit.jupiter.api.Test;
import parser.InstanceReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    @Test
    void solveTest() throws FileNotFoundException, ParseException {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        Solver solver = new Solver(instance);
        Solution solution = solver.solve();
        assert solution!=null;
        System.out.println(solution);
    }
}