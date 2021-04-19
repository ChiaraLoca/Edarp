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
        for(int k=0; k<solution.getList().size(); k++) {
            for(int s=0; s<solution.getInstance().getChargingStationId().length;s++) {
                if(solution.getInstance().getVehicleBatteryCapacity()[k]<solution.getBatteryLoadOfVehicleAtLocation()[k][s]+solution.getInstance().getRechargeRate()[s]*solution.getChargingTimeOfVehicleAtStation()[k][s])
                    return false;
            }
        }
        return true;
    }
}