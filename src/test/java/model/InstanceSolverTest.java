package model;

import org.junit.jupiter.api.Test;
import parser.InstanceReader;
import parser.InstanceWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class InstanceSolverTest {

    @Test
    void solveTest() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);

        InstanceSolver instanceSolver = new InstanceSolver(instance);
        instanceSolver.solve();
    }
}