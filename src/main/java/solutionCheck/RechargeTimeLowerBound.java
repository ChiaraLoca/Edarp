package solutionCheck;

// constraint 26: Eks ≥ Tks − ti,s − Tki - Mk,i,s * (1 − xk,i,s)    ∀k ∈ K, ∀s ∈ S, i ∈ D ∪ S ∪ {ok}, i = s
// define lower bounds on the recharge time at charging station s ∈ S

import model.Solution;

// TODO: todo

public class RechargeTimeLowerBound extends AbstractConstraint {

    public RechargeTimeLowerBound(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        Integer[] union=arrayUnion(solution.getInstance().getDropoffLocationsId(),solution.getInstance().getChargingStationId(),solution.getInstance().getArtificialOriginDepotId());
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int s=0; s<solution.getInstance().getChargingStationId().length;s++) {
                for(int i=0; i<union.length; i++) {
                    if(i==s)
                        continue;
                    if(solution.getChargingTimeOfVehicleAtStation()[k][solution.getInstance().getChargingStationId()[s]]
                            <solution.getTimeVehicleStartsAtLocation()[k][solution.getInstance().getChargingStationId()[s]]
                            -solution.getInstance().getTravelTime()[union[i]][solution.getInstance().getChargingStationId()[s]]
                            -solution.getTimeVehicleStartsAtLocation()[k][union[i]]
                            -solution.getInstance().getM()[union[i]][solution.getInstance().getChargingStationId()[s]]
                            *(1-solution.getVehicleSeqStopAtLocations()[k][union[i]][solution.getInstance().getChargingStationId()[s]]))
                        return false;
                }
            }
        }
        return true;
    }
}