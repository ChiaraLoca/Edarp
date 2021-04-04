package parser;

import model.Instance;
import org.junit.jupiter.api.Test;

class InstanceWriterTest {

    @Test
    void write() {
        InstanceReader instanceReader = InstanceReader.getInstanceReader();
        Instance instanceA = instanceReader.read("src/test/resources/a2-16-0.7.txt");

        InstanceWriter instanceWriter = new InstanceWriter(instanceA);
        instanceWriter.write(instanceA);

        Instance instanceU = instanceReader.read("src/test/resources/u2-16-0.7.txt");
        instanceWriter = new InstanceWriter(instanceU);
        instanceWriter.write(instanceU);

        System.out.println(instanceA);
        System.out.println(instanceU);

    }


}