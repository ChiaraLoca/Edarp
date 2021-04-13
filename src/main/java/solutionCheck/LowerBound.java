package solutionCheck;

// constraint 15: Lki ≥ max(0, li) ∀k ∈ K, ∀i ∈ N
// set lower bound on the occupancy of all vehicles

import model.Solution;

public class LowerBound extends AbstractConstraint {

    public LowerBound(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; i<solution.getInstance().getPickupAndDropoffLocations().size(); i++) {
                solution.getInstance().getPickupAndDropoffLocations().get(i).getLoad(); //li
            }
        }

        return false;
    }
}
