package solutionCheck;

// constraint 7:
// Ensure that every pickup location is visited exactly once and
//  that each pickup and dropoff pair is served by the same vehicle

import model.Node;
import model.Solution;

public class MissionExecuteSameCar2 extends AbstractConstraint{
    public MissionExecuteSameCar2(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for (int k = 0; k <solution.getInstance().getnVehicles(); k++) {
            for (int i:solution.getInstance().getPickupLocationsId()) {
                int sum1 = 0,sum2 = 0;
                int n = solution.getInstance().getPickupLocationsId().length;
                for (Node node:solution.getInstance().getPickupAndDropoffLocations()) {
                    int j=node.getId();
                    if(j!=i)
                        sum1+= solution.getVehicleSeqStopAtLocations()[k][i][j];
                    if(j!=n+i)
                        sum2+= solution.getVehicleSeqStopAtLocations()[k][j][n+i];
                }
                if(sum1-sum2 !=0)
                    return false;
            }
        }
        return true;
    }
}
