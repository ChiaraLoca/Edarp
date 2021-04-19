package solutionCheck;

// constraint 3:
// Guarantee that all vehicles return to a selected destination depot

import model.Solution;

public class ReturnToDestinationDepot extends AbstractConstraint{
    public ReturnToDestinationDepot(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
