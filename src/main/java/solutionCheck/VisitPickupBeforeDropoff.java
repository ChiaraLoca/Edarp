package solutionCheck;

// constraint 8:
// Guarantee that each pickup location i is visited before its dropoff location n + i,
//  by means of the direct travel time between the two locations and service time at location i

import model.Node;
import model.Solution;

public class VisitPickupBeforeDropoff extends AbstractConstraint{
    public VisitPickupBeforeDropoff(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        int n = solution.getInstance().getPickupLocationsId().length;
        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (int i :solution.getInstance().getPickupLocationsId() ) {
                if (solution.getVehicleSeqStopAtLocations()[k][i-1][n+i-1]==0)
                    continue;

                double sum = solution.getTimeVehicleStartsAtLocation()[k][i-1] +
                        solution.getInstance().getNodes().get(i-1).getServiceTime() +
                        solution.getInstance().getTravelTime()[i-1][n+i-1];

                double diff = Math.abs(sum - solution.getTimeVehicleStartsAtLocation()[k][i + n - 1]);
                if (sum>solution.getTimeVehicleStartsAtLocation()[k][i+n-1] && diff < 1E-7) {
                    return false;
                }
            }
        }
        return true;
    }
}
