package parser;


import model.Instance;
import model.Solution;
import org.junit.jupiter.api.Test;
import solutionCheck.SolutionChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelReaderTest {

    @Test
    void readTest() throws FileNotFoundException, ParseException {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/main/resources/instances/u2-16-0.7.txt"),true);

        ModelReader modelReader = ModelReader.getModelReader();
        File file = new File("src/main/resources/outputFiles/outForParser.txt");
        modelReader.read(file, instance);

        SolutionChecker solutionChecker = new SolutionChecker(modelReader.read(file, instance));
        solutionChecker.checkAll();
    }
}