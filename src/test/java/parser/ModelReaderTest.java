package parser;


import model.Instance;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelReaderTest {

    @Test
    void readTest() throws FileNotFoundException, ParseException {

        ModelReader modelReader = ModelReader.getModelReader();
        File file = new File("src/test/resources/modelOutput/outputParser.txt");
        modelReader.read(file, null);
    }
}