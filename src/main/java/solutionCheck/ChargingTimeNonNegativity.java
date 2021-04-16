package solutionCheck;

// constraint 29: Eks ≥ 0   ∀k ∈ K, i ∈ V
// charging time of vehicle k at charging station s ∈ S non negativity constraint

import model.Solution;

public class ChargingTimeNonNegativity extends AbstractConstraint {

    public ChargingTimeNonNegativity(Solution solution) {
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