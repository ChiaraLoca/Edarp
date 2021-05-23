package solutionCheck;

// constraint 15: Lki ≥ max(0, li)      ∀k ∈ K, ∀i ∈ N
// set lower bound on the occupancy of all vehicles

import model.Solution;

public class OccupancyLowerBound extends AbstractConstraint {

    public OccupancyLowerBound(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int i=0; i<solution.getInstance().getPickupAndDropoffLocations().size(); i++) {
                if(solution.getLoadOfVehicleAtLocation()[k][solution.getInstance().getPickupAndDropoffLocations().get(i).getId()-1]
                        <Math.max(0,solution.getInstance().getPickupAndDropoffLocations().get(i).getLoad()))
                    return false;
            }
        }
        return true;
    }
}
