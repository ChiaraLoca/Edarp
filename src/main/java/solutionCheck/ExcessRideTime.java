package solutionCheck;

// constraint 12:
// Calculate the excess ride-time for user i

import model.Solution;

public class ExcessRideTime extends AbstractConstraint{
    public ExcessRideTime(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
