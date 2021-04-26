package solutionCheck;

// constraint 28: Bki ≥ 0   ∀k ∈ K, s ∈ S
// battery load of vehicle k at location i ∈ V non negativity constraint

import model.Solution;

public class BatteryLoadNonNegativity extends AbstractConstraint {

    public BatteryLoadNonNegativity(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        /*for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int i=0; i<solution.getInstance().getNodes().size(); i++) {
                if(solution.getBatteryLoadOfVehicleAtLocation()[k][solution.getInstance().getNodes().get(i).getId()]<0)
                    return false;
            }
        }*/
        return true;
    }
}