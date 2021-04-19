package solutionCheck;

// constraint 2:
// Ensure that all vehicles exit their origin depots and visit
//  a pickup location P,relocate to a charging station S,or proceed to a destination depot F

import model.Solution;

public class ControlPath extends AbstractConstraint{
    public ControlPath(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
