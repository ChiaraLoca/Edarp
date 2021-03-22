package parser;

import model.Instance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstanceWriterTest {

    @Test
    void write() {
        InstanceReader instanceReader = InstanceReader.getInstanceReader();
        Instance instance = instanceReader.read("src/test/resources/a2-16-0.7.txt");


        InstanceWriter instanceWriter = InstanceWriter.getInstanceWriter();
        instanceWriter.write(instance);
    }
}