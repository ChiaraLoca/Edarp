package solutionCheck;

// constraint 27: xk,i,j ∈ {0, 1}       ∀k ∈ K, i ∈ V, j ∈ V
// set integrality constraint

import model.Solution;

public class Integrality extends AbstractConstraint {

    public Integrality(Solution solution) {
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