package parser;

import model.Instance;

import java.io.File;
import java.io.IOException;

public class InstanceWriter {

    private static InstanceWriter instanceWriter=null;
    private InstanceWriter(){}
    public static InstanceWriter getInstanceWriter(){
        if (instanceWriter==null)
            instanceWriter=new InstanceWriter();
        return instanceWriter;
    }


    public void write(Instance instance)
    {
        File file = new File("Ampl/"+instance.getTitle()+".dat");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

}
