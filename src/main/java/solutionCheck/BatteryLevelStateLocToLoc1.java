package solutionCheck;

// constraint 19: Bkj ≤ Bki − βi,j + Q(1 − xk,i,j)      ∀k ∈ K, i ∈ V \ S, j ∈ V \ {ok}, i != j
// set the battery level state from any location i ∈ V \ S to any location j ∈ Vo(k)

import model.Solution;

public class BatteryLevelStateLocToLoc1 extends AbstractConstraint {

    public BatteryLevelStateLocToLoc1(Solution solution) {
        super(solution);
    }

    // TODO: come rappresentare diff?

    @Override
    boolean check() {
        for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; ; i++) {
                for(int j=0; ; j++) {
                    if(i==j)
                        continue;
                    if(solution.getBatteryLoadOfVehicleAtLocation()[k][j]>solution.getBatteryLoadOfVehicleAtLocation()[k][i]-solution.getInstance().getBatteryConsumption()[i][j]+solution.getInstance().getVehicleBatteryCapacity()[k]*(1-solution.getVehicleSeqStopAtLocations()[k][i][j])) // TODO: check Q index
                        return false;
                }
            }
        }
        return true;
    }
}