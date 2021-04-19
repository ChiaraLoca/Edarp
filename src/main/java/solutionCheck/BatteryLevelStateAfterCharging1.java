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
        for(int k=0; k<solution.getList().size(); k++) {
            for(int s=0; s<solution.getInstance().getChargingStationId().length; s++) {
                for (int j=0; ;j++) { // TODO: fix
                   if(s==j)
                       continue;
                    if(solution.getBatteryLoadOfVehicleAtLocation()[k][j]>solution.getBatteryLoadOfVehicleAtLocation()[k][s]+solution.getInstance().getRechargeRate()[s]*solution.getChargingTimeOfVehicleAtStation()[k][s]-solution.getInstance().getBatteryConsumption()[s][j]+solution.getInstance().getVehicleBatteryCapacity()[k]*(1-solution.getVehicleSeqStopAtLocations()[k][s][j]))
                        return false;
                }
            }
        }
        return true;
    }
}