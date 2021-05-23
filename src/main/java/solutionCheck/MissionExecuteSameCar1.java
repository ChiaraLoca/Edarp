package solutionCheck;

// constraint 6:
// Ensure that every pickup location is visited exactly once and
//  that each pickup and dropoff pair is served by the same vehicle

import model.Node;
import model.Solution;

public class MissionExecuteSameCar1 extends AbstractConstraint{
    public MissionExecuteSameCar1(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for (int i: solution.getInstance().getPickupLocationsId()) {
            int sum=0;
            for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
                for (Node node: solution.getInstance().getPickupAndDropoffLocations()) {
                    int j = node.getId();
                    if(i==j)
                        continue;
                    sum += solution.getVehicleSeqStopAtLocations()[k][i-1][j-1];
                }
            }
            if (sum!=1)
                return false;
        }
        return true;
    }
}
