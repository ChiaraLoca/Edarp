package solutionCheck;

// constraint 29: Eks ≥ 0   ∀k ∈ K, s ∈ S
// charging time of vehicle k at charging station s ∈ S non negativity constraint

import model.Solution;

public class ChargingTimeNonNegativity extends AbstractConstraint {

    public ChargingTimeNonNegativity(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int s=0; s<solution.getInstance().getChargingStationId().length;s++) {
                if(solution.getChargingTimeOfVehicleAtStation()[k]
                        [solution.getInstance().getChargingStationId()[s]]<0)
                    return false;
            }
        }
        return true;
    }
}