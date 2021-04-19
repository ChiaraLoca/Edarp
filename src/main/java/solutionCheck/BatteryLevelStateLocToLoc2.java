package solutionCheck;

// constraint 20: Bkj ≥ Bki − βi,j - Q(1 − xk,i,j)      ∀k ∈ K, i ∈ V \ S, j ∈ V \ {ok}, i != j
// set the battery level state from any location i ∈ V \ S to any location j ∈ Vo(k)

import model.Solution;

// TODO: todo

public class BatteryLevelStateLocToLoc2 extends AbstractConstraint {

    public BatteryLevelStateLocToLoc2(Solution solution) {
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