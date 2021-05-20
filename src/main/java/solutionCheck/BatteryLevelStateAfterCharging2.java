package solutionCheck;

// constraint 22: Bkj ≥ Bks + αs * Eks − βs,j - Q(1 − xk,s,j)   ∀k ∈ K, s ∈ S, j ∈ P ∪ F ∪ S, s != j
// set the battery level state after a visit to a charging facility s ∈ S to any location j ∈ P ∪ F ∪ S

import model.Solution;

// TODO: todo 

public class BatteryLevelStateAfterCharging2 extends AbstractConstraint {

    public BatteryLevelStateAfterCharging2(Solution solution) {
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
                    if(solution.getBatteryLoadOfVehicleAtLocation()[k][union[j]-1]
                            <solution.getBatteryLoadOfVehicleAtLocation()[k][solution.getInstance().getChargingStationId()[s]-1]
                            +solution.getInstance().getStationRechargingRate()[solution.getInstance().getChargingStationId()[s]-solution.getInstance().getChargingStationId()[0]]
                            *solution.getChargingTimeOfVehicleAtStation()[k][solution.getInstance().getChargingStationId()[s]-solution.getInstance().getChargingStationId()[0]]
                            -solution.getInstance().getBatteryConsumption()[solution.getInstance().getChargingStationId()[s]-1][union[j]-1]
                            -solution.getInstance().getVehicleBatteryCapacity()[k]
                            *(1-solution.getVehicleSeqStopAtLocations()[k][solution.getInstance().getChargingStationId()[s]-1][union[j]-1]))
                        return false;
                }
            }
        }
        return true;
    }
}