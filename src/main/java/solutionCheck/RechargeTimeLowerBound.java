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
        /*for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; i<solution.getInstance().getPickupAndDropoffLocations().size(); i++) {
                if(solution.getLoadOfVehicleAtLocation()[k][i]>Math.min(solution.getInstance().getVehicleCapacity()[k],solution.getInstance().getVehicleCapacity()[k]+solution.getInstance().getPickupAndDropoffLocations().get(i).getLoad()))
                    return false;
            }
        }*/
        return true;
    }
}