package solutionCheck;

// constraint 14:
// Load at location j ∈ V is computed from the load at the preceding location i ∈ V
//  and the change in load at location j
// Redundant with respect to the model formulation. However, they help strengthening the LP relaxations

import model.Solution;

public class ComputeLoad2 extends AbstractConstraint{
    public ComputeLoad2(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
