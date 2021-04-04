import model.Instance;
import parser.InstanceReader;
import parser.InstanceWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

public class DatCreator {
    public static void main(String[] args) {
        if(args.length!=2){
            System.out.println("Invalid arguments. Correct use:\n" +
                    "./DatCreator <Source folder> <Destination folder>");
            return;
        }
        File target = new File(args[0]);
        if(!target.exists()){
            System.out.println("File not found");
            return;
        }
        InstanceReader instanceReader= InstanceReader.getInstanceReader();
        if(target.isDirectory()){
            File[] files= target.listFiles();
            for(File f : files){
                try {
                    new InstanceWriter(instanceReader.read(f), args[1]).write();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if(target.isFile()){

        } else return;
        System.out.println("Conversion completed");
    }
}
