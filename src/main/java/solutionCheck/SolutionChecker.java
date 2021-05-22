package solutionCheck;

import model.Solution;
import util.Util;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class SolutionChecker {
    private final Solution solution;

    public SolutionChecker(Solution solution) {
        this.solution = solution;
    }

    public boolean checkAll() {
        int total=0;
        int pass=0;
        Reflections reflections = new Reflections("solutionCheck");
        Set<Class<? extends AbstractConstraint>> classes = reflections.getSubTypesOf(AbstractConstraint.class);
        for (Class<? extends AbstractConstraint> aClass : classes) {
            try {
                Constructor<?> constructor = aClass.getConstructor(Solution.class);

                AbstractConstraint constraint = (AbstractConstraint) constructor.newInstance(solution);
                if(!constraint.check()) {
                    Util.printRed("Error constraint: "+aClass.getSimpleName()+"\n");
                    //return false;
                } else {
                    Util.printGreen("Passed constraint: "+aClass.getSimpleName()+"\n");
                    pass++;
                }
                total++;
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        solution.setConstraint("Constraint: "+pass+"/"+total);
        return true;
    }
}

