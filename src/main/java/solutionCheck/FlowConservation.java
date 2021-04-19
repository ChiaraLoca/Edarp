package solutionCheck;

// constraint 5:
// Flow conservation

import model.Solution;

public class FlowConservation extends AbstractConstraint{
    public FlowConservation(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
