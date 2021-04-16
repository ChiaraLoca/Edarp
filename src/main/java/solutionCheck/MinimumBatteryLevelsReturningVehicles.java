package solutionCheck;

// constraint 24: Bki ≥ r*Q ∀k ∈ K, i ∈ F
// impose minimum battery levels for all vehicles returning to the depots

import model.Solution;

public class MinimumBatteryLevelsReturningVehicles extends AbstractConstraint {

    public MinimumBatteryLevelsReturningVehicles(Solution solution) {
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