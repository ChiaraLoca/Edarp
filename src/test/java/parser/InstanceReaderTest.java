package parser;

import model.Instance;

import static org.junit.jupiter.api.Assertions.*;

class InstanceReaderTest {

    @org.junit.jupiter.api.Test
    void readU() {

        InstanceReader instanceReader = InstanceReader.getInstanceReader();

        Instance instance = instanceReader.read("src/test/resources/u2-16-0.7.txt");
        assert instance!=null;
        System.out.println(instance);

    }
    @org.junit.jupiter.api.Test
    void readA() {

        InstanceReader instanceReader = InstanceReader.getInstanceReader();
        Instance instance = instanceReader.read("src/test/resources/a2-16-0.7.txt");

        assert instance!=null;
        System.out.println(instance);


    }

}