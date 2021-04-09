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

        Instance instance = instanceReader.read(new File("src/test/resources/instances/a2-16-0.7.txt"),false);
        assert instance!=null;
        System.out.println(instance);

    }
    @org.junit.jupiter.api.Test
    void readA() throws FileNotFoundException, ParseException {

        InstanceReader instanceReader = InstanceReader.getInstanceReader();
        Instance instance = instanceReader.read(new File("src/test/resources/instances/u2-16-0.7.txt"),false);

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
    @Test
    void impementDatChangesTest() throws FileNotFoundException, ParseException {
        InstanceReader instanceReader = InstanceReader.getInstanceReader();

        Instance instanceBase = instanceReader.read(new File("src/test/resources/instances/a2-16-0.7.txt"),false);
        assert instanceBase!=null;


        Instance instanceModificata = instanceReader.read(new File("src/test/resources/instances/a2-16-0.7.txt"),true);
        assert instanceModificata!=null;

        System.out.println("Time Horizon");
        System.out.println("instance Base "+instanceBase.getTimeHorizon());
        System.out.println("instance Modificata "+instanceModificata.getTimeHorizon());

        System.out.println("Time Travel");
        System.out.println("instance Base ");
        for (int i =0;i<5;i++)
        {
            for(int j =0;j<5;j++)
            {
                System.out.print(String.format("%1$,.2f" , instanceBase.getTravelTime()[i][j])+"\t");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("instance Modificata "+instanceModificata.getTimeHorizon());
        for (int i =0;i<5;i++)
        {
            for(int j =0;j<5;j++)
            {
                System.out.print(String.format("%1$,.2f" , instanceModificata.getTravelTime()[i][j])+"\t");
            }
            System.out.println();
        }
        System.out.println();
    }

}