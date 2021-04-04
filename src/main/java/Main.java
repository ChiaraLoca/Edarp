import model.Instance;
import parser.InstanceReader;
import parser.InstanceWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, ParseException {
        File target = new File(args[0]);
        if(!target.exists()){
            System.out.println("File not found");
            return;
        }
        InstanceReader instanceReader= InstanceReader.getInstanceReader();
        if(target.isDirectory()){
            File[] files= target.listFiles();
            for(File f : files){
                new InstanceWriter(instanceReader.read(f)).write();
            }
        } else if(target.isFile()){

        } else return;

    }
}
