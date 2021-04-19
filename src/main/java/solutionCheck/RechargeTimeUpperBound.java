package solutionCheck;

// constraint 25: Eks ≤ Tks − ti,s − Tki + Mk,i,s * (1 − xk,i,s)    ∀k ∈ K, ∀s ∈ S, i ∈ D ∪ S ∪ {ok}, i = s
// define upper bounds on the recharge time at charging station s ∈ S

import model.Solution;

// TODO: fix index and check M

public class RechargeTimeUpperBound extends AbstractConstraint {

    public RechargeTimeUpperBound(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for(int k=0; k<solution.getList().size(); k++) {
            for(int s=0; s<solution.getInstance().getChargingStationId().length;s++) {
                for(int i=0; ; i++) {
                    if(i==s)
                        continue;
                    if(solution.getChargingTimeOfVehicleAtStation()[k][s]>solution.getTimeVehicleStartsAtLocation()[k][s]-solution.getInstance().getTravelTime()[i][s]-solution.getTimeVehicleStartsAtLocation()[k][i]+solution.getInstance().getM()[k][i]*(1-solution.getVehicleSeqStopAtLocations()[k][i][s]))
                        return false;
                }
            }
        }
        return true;
    }
}