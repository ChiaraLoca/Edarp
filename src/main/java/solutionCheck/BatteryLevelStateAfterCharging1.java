package solutionCheck;

// constraint 21: Bkj ≤ Bks + αs * Eks − βs,j + Q(1 − xk,s,j)   ∀k ∈ K, s ∈ S, j ∈ P ∪ F ∪ S, s != j
// set the battery level state after a visit to a charging facility s ∈ S to any location j ∈ P ∪ F ∪ S

import model.Solution;

public class BatteryLevelStateAfterCharging1 extends AbstractConstraint {

    public BatteryLevelStateAfterCharging1(Solution solution) {
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