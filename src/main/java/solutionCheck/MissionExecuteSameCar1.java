package solutionCheck;

// constraint 6:
// Ensure that every pickup location is visited exactly once and
//  that each pickup and dropoff pair is served by the same vehicle

import model.Solution;

public class MissionExecuteSameCar1 extends AbstractConstraint{
    public MissionExecuteSameCar1(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
