package parser;

import model.Instance;
import model.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstanceReader {
    private static InstanceReader instanceReader=null;
    private InstanceReader(){

    }
    public static InstanceReader getInstanceReader(){
        if (instanceReader==null)
            instanceReader=new InstanceReader();
        return instanceReader;
    }
    public Instance read(String path){
        File file=new File(path);
        if(!file.exists())
            return null;
        try (BufferedReader bufferedReader= new BufferedReader(new FileReader(file))){
            List<String[]> lines=bufferedReader.lines().collect(() -> new ArrayList<String[]>(),
                    (array, s)->{
                        array.add(s.split("\\w"));
                    },
                    ArrayList::addAll);
            if (lines.get(0).length!=7)
                return null;
            Instance instance= new Instance(lines.get(0));
            int i=1;
            List<Node> nodes=new ArrayList<>();
            while (lines.get(i)[0].equals("")){

            }
            instance.setNodes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
