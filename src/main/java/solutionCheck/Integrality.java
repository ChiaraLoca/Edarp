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
        for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; i<solution.getInstance().getAllPossibleLocationsId().length; i++) {
                for(int j=0; j<solution.getInstance().getAllPossibleLocationsId().length;j++) {
                    if(solution.getVehicleSeqStopAtLocations()[k][i][j]!=0&&solution.getVehicleSeqStopAtLocations()[k][i][j]!=1)
                        return false;
                }
            }
        }
        return true;
    }
}