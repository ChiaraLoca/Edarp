package solver;

import model.*;

public class SolverV2 implements ISolver {

private final Instance instance;

    public SolverV2(Instance instance) {
        this.instance = instance;
    }



    @Override
    public Solution solve()throws Exception
    {
        IterationSolver iterationSolver = new IterationSolver(instance,true,0);

        Solution solution = iterationSolver.solve();

        return solution;


    }



}
