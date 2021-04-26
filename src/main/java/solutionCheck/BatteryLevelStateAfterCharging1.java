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
        Integer[] union=arrayUnion(solution.getInstance().getPickupLocationsId(), solution.getInstance().getAllAvailableDestinationDepotsId(), solution.getInstance().getChargingStationId());
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int s=0; s<solution.getInstance().getChargingStationId().length; s++) {
                for (int j=0; j<union.length;j++) {
                   if(s==j)
                       continue;
                    if(solution.getBatteryLoadOfVehicleAtLocation()[k][union[j]]>solution.getBatteryLoadOfVehicleAtLocation()[k][solution.getInstance().getChargingStationId()[s]]+solution.getInstance().getRechargeRate()[solution.getInstance().getChargingStationId()[s]]*solution.getChargingTimeOfVehicleAtStation()[k][solution.getInstance().getChargingStationId()[s]]-solution.getInstance().getBatteryConsumption()[solution.getInstance().getChargingStationId()[s]][union[j]]+solution.getInstance().getVehicleBatteryCapacity()[k]*(1-solution.getVehicleSeqStopAtLocations()[k][solution.getInstance().getChargingStationId()[s]][union[j]]))
                        return false;
                }
            }
        }
        return true;
    }
}