package parser;

import model.Instance;

import static org.junit.jupiter.api.Assertions.*;

class InstanceReaderTest {

    @org.junit.jupiter.api.Test
    void read() {

        InstanceReader instanceReader = InstanceReader.getInstanceReader();

        Instance instance = instanceReader.read("src/test/resources/u2-16-0.7.txt");
        assert instance!=null;
        System.out.println(instance);



    }
}