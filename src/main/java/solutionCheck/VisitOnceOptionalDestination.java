package solutionCheck;

// constraint 4:
// Allow each optional destination depot and charging station to be visited at most once
//   Such locations can be replicated in order to allow multiple visits to the nodes in F And S.

import model.Solution;

public class VisitOnceOptionalDestination extends AbstractConstraint{
    public VisitOnceOptionalDestination(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
