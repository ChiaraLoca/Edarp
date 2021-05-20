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
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int i=0; i<solution.getInstance().getNodes().size(); i++) {
                for(int j=0; j<solution.getInstance().getNodes().size();j++) {
                    if(solution.getVehicleSeqStopAtLocations()[k][solution.getInstance().getNodes().get(i).getId()-1][solution.getInstance().getNodes().get(j).getId()-1]
                            !=0
                            &&solution.getVehicleSeqStopAtLocations()[k][solution.getInstance().getNodes().get(i).getId()-1][solution.getInstance().getNodes().get(j).getId()-1]
                            !=1)
                        return false;
                }
            }
        }
        return true;
    }
}