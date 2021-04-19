package solutionCheck;

// constraint 13:
// Load at location j ∈ V is computed from the load at the preceding location i ∈ V
//  and the change in load at location j

import model.Solution;

public class ComputeLoad1 extends AbstractConstraint{
    public ComputeLoad1(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
