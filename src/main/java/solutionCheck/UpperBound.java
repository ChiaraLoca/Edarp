package solutionCheck;

// constraint 16: Lki ≤ min(Ck,Ck + li)     ∀k ∈ K, ∀i ∈ N
// set upper bound on the occupancy of all vehicles

import model.Solution;

public class UpperBound extends AbstractConstraint {

    public UpperBound(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; i<solution.getInstance().getPickupAndDropoffLocations().size(); i++) {
                if(solution.getLoadOfVehicleAtLocation()[k][i]>Math.min(solution.getInstance().getVehicleCapacity()[k],solution.getInstance().getVehicleCapacity()[k]+solution.getInstance().getPickupAndDropoffLocations().get(i).getLoad()))
                    return false;
            }
        }
        return true;
    }
}