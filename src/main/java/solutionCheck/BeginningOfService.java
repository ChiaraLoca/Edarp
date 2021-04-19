package solutionCheck;

// constraint 9:
// Set time windows around the beginning of service at each location

import model.Solution;

public class BeginningOfService extends AbstractConstraint{
    public BeginningOfService(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
