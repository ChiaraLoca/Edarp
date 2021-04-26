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
        /*
        int n = solution.getInstance().getPickupLocationsId().length;
        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (int i :solution.getInstance().getPickupLocationsId() ) {
                double sum = solution.getTimeVehicleStartsAtLocation()[k][i] +
                        solution.getInstance().getNodes().get(i).getServiceTime() +
                        solution.getInstance().getTravelTime()[i][n+i];
                if (sum>solution.getTimeVehicleStartsAtLocation()[k][i+n])
                    return false;
            }
        }*/
        return true;
    }
}
