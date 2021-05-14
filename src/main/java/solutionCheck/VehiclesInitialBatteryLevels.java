package solutionCheck;

// constraint 18: Bki = Bk0     ∀k ∈ K, i ∈ ok
// set initial battery levels for vehicles at origin depots ok

import model.Solution;

public class VehiclesInitialBatteryLevels extends AbstractConstraint {

    public VehiclesInitialBatteryLevels(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int i=0; i<solution.getInstance().getArtificialOriginDepotId().length; i++) {
                if(solution.getBatteryLoadOfVehicleAtLocation()[k][solution.getInstance().getArtificialOriginDepotId()[i]]
                        !=solution.getInstance().getVehicleInitBatteryInventory()[k])
                    return false;
            }
        }
        return true;
    }
}