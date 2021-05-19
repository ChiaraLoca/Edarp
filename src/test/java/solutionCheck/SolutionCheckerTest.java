package solutionCheck;

import model.Instance;
import model.Solution;
import org.junit.jupiter.api.Test;
import parser.InstanceReader;
import solver.Solver;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolutionCheckerTest {

    @Test
    void solveTest() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        Solver solver = new Solver(instance);
        Solution solution = solver.solveProva();
        assert solution!=null;
        System.out.println(solution);

        SolutionChecker sc=new SolutionChecker(solution);
        assertTrue(sc.checkAll());


    }

    @Test
    void singleTest() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        Solver solver = new Solver(instance);
        Solution solution = solver.solveProva();
        assert solution!=null;
        System.out.println(solution);

        AbstractConstraint check=new VisitPickupBeforeDropoff(solution);
        assertTrue(check.check());

    }

}
