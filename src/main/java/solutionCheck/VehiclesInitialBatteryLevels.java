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
        for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; i<solution.getInstance().getArtificialOriginDepotId().length; i++) { // TODO: non penso possa funzionare partendo con i=0 e incrementando
                if(solution.getBatteryLoadOfVehicleAtLocation()[k][i]!=solution.getBatteryLoadOfVehicleAtLocation()[k][0])
                    return false;
            }
        }
        return true;
    }
}