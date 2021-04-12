package solver;

import model.Instance;
import model.Solution;

public class Solver {
    public static Solver instance=null;
    private Solver(){};
    public static Solver getInstance() {
        if(instance ==null)
            instance = new Solver();
        return instance;
    }

    public Solution solve(Instance instance)
    {
        Solution solution = new Solution(instance);

        return solution;
    }



}
