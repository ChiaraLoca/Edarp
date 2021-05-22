package model;

import org.junit.jupiter.api.Test;
import parser.InstanceReader;
import parser.InstanceWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InstanceSolverTest {

    @Test
    void solveTestU() throws Exception {
        Instance instance = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);

        InstanceSolver instanceSolver = new InstanceSolver(instance);
        instanceSolver.solve();
    }

    @Test
    void solveTestA() throws Exception {
        Instance instanceU = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/u2-16-0.7.txt"),true);
        Instance instanceA = InstanceReader.getInstanceReader().read(new File("src/test/resources/instances/a2-16-0.7.txt"),true);

        InstanceSolver instanceSolver = new InstanceSolver(instanceA);
        instanceSolver.solve();
    }

    @Test
    void solveAllTest() throws Exception
    {
        ArrayList<InstanceSolver> instanceSolvers = new ArrayList<>();
        ArrayList<String> output = new ArrayList<>();



        File target = new File("src/test/resources/instances");
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