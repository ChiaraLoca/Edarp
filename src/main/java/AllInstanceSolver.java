import model.Instance;
import parser.InstanceReader;
import solver.InstanceSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

public class AllInstanceSolver {

    public static void main(String[] args) throws Exception {

        ArrayList<InstanceSolver> instanceSolvers = new ArrayList<>();
        ArrayList<String> output = new ArrayList<>();

        File target = new File("src/main/resources/instances");
        File[] files= target.listFiles();

        for(File f : files) {
            Instance instance = InstanceReader.getInstanceReader().read(f,true);
            instanceSolvers.add(new InstanceSolver(instance));
        }

        for(InstanceSolver i: instanceSolvers)
        {

            long startTime = System.currentTimeMillis();
            i.solve();
            long endTime = System.currentTimeMillis();
            output.add(""+i.getInstance().getTitle()+"\t"+i.getTheSolution().getScore()+"\t"+i.getTheSolution().getConstraint()+"\t"+(endTime-startTime));
        }

        for(String s : output)
        {
            System.out.println(s);
        }
        System.out.println("fine");
    }
}
