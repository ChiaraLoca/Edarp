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
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int s=0; s<solution.getInstance().getChargingStationId().length;s++) {

                double sum = solution.getBatteryLoadOfVehicleAtLocation()[k][solution.getInstance().getChargingStationId()[s]-1]
                        +solution.getInstance().getStationRechargingRate()[solution.getInstance().getChargingStationId()[s]
                        -solution.getInstance().getChargingStationId()[0]]
                        *solution.getChargingTimeOfVehicleAtStation()[k][s];

                double diff = Math.abs(sum- solution.getInstance().getVehicleBatteryCapacity()[k]);

                if(solution.getInstance().getVehicleBatteryCapacity()[k] < sum && diff > TOLERANCE)
                    return false;
            }
        }
        return true;
    }
}