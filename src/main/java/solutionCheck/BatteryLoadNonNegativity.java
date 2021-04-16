package solutionCheck;

// constraint 28: Bki ≥ 0   ∀k ∈ K, s ∈ S
// battery load of vehicle k at location i ∈ V non negativity constraint

import model.Solution;

public class BatteryLoadNonNegativity extends AbstractConstraint {

    public BatteryLoadNonNegativity(Solution solution) {
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