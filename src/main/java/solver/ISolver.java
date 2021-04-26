package solver;

import model.Instance;
import model.Solution;

public interface ISolver {

    Solution solve(Instance instance) throws Exception;
}
