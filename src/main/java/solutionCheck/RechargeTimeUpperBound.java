package solutionCheck;

// constraint 25: Eks ≤ Tks − ti,s − Tki + Mk,i,s * (1 − xk,i,s)    ∀k ∈ K, ∀s ∈ S, i ∈ D ∪ S ∪ {ok}, i = s
// define upper bounds on the recharge time at charging station s ∈ S

import model.Solution;

public class RechargeTimeUpperBound extends AbstractConstraint {

    public RechargeTimeUpperBound(Solution solution) {
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
                    if(solution.getChargingTimeOfVehicleAtStation()[k][solution.getInstance().getChargingStationId()[s]-solution.getInstance().getChargingStationId()[0]]
                            >solution.getTimeVehicleStartsAtLocation()[k][solution.getInstance().getChargingStationId()[s]-1]
                            -solution.getInstance().getTravelTime()[union[i]-1][solution.getInstance().getChargingStationId()[s]-1]
                            -solution.getTimeVehicleStartsAtLocation()[k][union[i]-1]
                            +solution.getInstance().getM()[union[i]-1][solution.getInstance().getChargingStationId()[s]-1]
                            *(1-solution.getVehicleSeqStopAtLocations()[k][union[i]-1][solution.getInstance().getChargingStationId()[s]-1]))
                        return false;
                }
            }
        }
        return true;
    }
}