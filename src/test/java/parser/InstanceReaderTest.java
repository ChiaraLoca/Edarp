package parser;


import model.Instance;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class InstanceReaderTest {

    @org.junit.jupiter.api.Test
    void readU() throws FileNotFoundException, ParseException {

        InstanceReader instanceReader = InstanceReader.getInstanceReader();

        Instance instance = instanceReader.read(new File("src/test/resources/instances/a2-16-0.7.txt"));
        assert instance!=null;
        System.out.println(instance);

    }
    @org.junit.jupiter.api.Test
    void readA() throws FileNotFoundException, ParseException {

        InstanceReader instanceReader = InstanceReader.getInstanceReader();
        Instance instance = instanceReader.read(new File("src/test/resources/instances/u2-16-0.7.txt"));

        assert instance!=null;
        System.out.println(instance);


    }
    @Test
    void openResourcesFolder(){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource("instances");
        assertNotNull(url);
        System.out.println(url);



    }

}