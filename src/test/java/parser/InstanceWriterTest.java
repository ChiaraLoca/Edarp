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

    }
    @Test
    void  timeBetweenNodesTest() {
        InstanceReader instanceReader = InstanceReader.getInstanceReader();
        Instance instance = instanceReader.read("src/test/resources/a2-16-0.7.txt");


        InstanceWriter instanceWriter = new InstanceWriter(instance);
        double [][] matrix = instanceWriter.calculateTravelTime(1);
        int size= instance.getNodes().size();
        System.out.println("Matrix:");

        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }



    }

}