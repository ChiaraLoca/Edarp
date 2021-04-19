package solutionCheck;

// constraint 8:
// Guarantee that each pickup location i is visited before its dropoff location n + i,
//  by means of the direct travel time between the two locations and service time at location i

import model.Solution;

public class VisitPickupBeforeDropoff extends AbstractConstraint{
    public VisitPickupBeforeDropoff(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
