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
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int i=0; i<solution.getInstance().getAllAvailableDestinationDepotsId().length; i++) {
                if(solution.getBatteryLoadOfVehicleAtLocation()[k][solution.getInstance().getAllAvailableDestinationDepotsId()[i]]<solution.getInstance().getMinBatteryRatioLvl()*solution.getInstance().getVehicleBatteryCapacity()[k])
                    return false;
            }
        }
        return true;
    }
}