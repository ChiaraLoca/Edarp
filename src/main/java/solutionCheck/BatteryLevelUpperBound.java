package solutionCheck;

// constraint 23: Q ≥ Bks + αs_Eks ∀k ∈ K, s ∈ S
// set upper bounds on the battery level states at charging stations

import model.Solution;

public class BatteryLevelUpperBound extends AbstractConstraint {

    public BatteryLevelUpperBound(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        /*for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; i<solution.getInstance().getPickupAndDropoffLocations().size(); i++) {
                if(solution.getLoadOfVehicleAtLocation()[k][i]>Math.min(solution.getInstance().getVehicleCapacity()[k],solution.getInstance().getVehicleCapacity()[k]+solution.getInstance().getPickupAndDropoffLocations().get(i).getLoad()))
                    return false;
            }
        }*/
        return true;
    }
}