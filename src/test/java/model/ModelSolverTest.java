package model;

import org.junit.jupiter.api.Test;
import parser.InstanceReader;
import parser.ModelReader;
import solutionCheck.SolutionChecker;

import java.io.File;

public class ModelSolverTest {
    @Test
    void solveTest() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        ModelReader modelReader = ModelReader.getModelReader();
        File file = new File("src/test/resources/outputFiles/outForParser.txt");
        Solution s=modelReader.read(file, instance);

        SolutionChecker solutionChecker = new SolutionChecker(s);
        solutionChecker.checkAll();
    }
}
