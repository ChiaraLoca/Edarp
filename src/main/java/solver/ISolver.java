package solver;

import model.Instance;
import model.Solution;

public interface ISolver {

    Solution solve() throws Exception;
}
