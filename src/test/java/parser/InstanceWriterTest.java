package parser;

import model.Instance;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

class InstanceWriterTest {

    @Test
    void write() throws FileNotFoundException, ParseException {
        InstanceReader instanceReader = InstanceReader.getInstanceReader();
        Instance instanceA = instanceReader.read(new File("src/test/resources/instances/a2-16-0.7.txt"));

        InstanceWriter instanceWriter = new InstanceWriter(instanceA);
        instanceWriter.write();

        Instance instanceU = instanceReader.read(new File("src/test/resources/instances/u2-16-0.7.txt"));
        instanceWriter = new InstanceWriter(instanceU);
        instanceWriter.write();

        System.out.println(instanceA);
        System.out.println(instanceU);

    }


}