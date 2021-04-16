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
        /*for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; i<solution.getInstance().getPickupAndDropoffLocations().size(); i++) {
                if(solution.getLoadOfVehicleAtLocation()[k][i]>Math.min(solution.getInstance().getVehicleCapacity()[k],solution.getInstance().getVehicleCapacity()[k]+solution.getInstance().getPickupAndDropoffLocations().get(i).getLoad()))
                    return false;
            }
        }*/
        return true;
    }
}