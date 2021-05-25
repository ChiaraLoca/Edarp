package solutionCheck;

// constraint 10:
// Impose maximum ride-times for the users

import model.Solution;

public class MaxRideTime extends AbstractConstraint {

    public MaxRideTime(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        int n = solution.getInstance().getPickupLocationsId().length;
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for (int i: solution.getInstance().getPickupLocationsId()) {
                double value = solution.getTimeVehicleStartsAtLocation()[k][n+i-1]
                        -solution.getTimeVehicleStartsAtLocation()[k][i-1]
                        -solution.getInstance().getNodes().get(i-1).getServiceTime();
                if (value>solution.getInstance().getUserMaxRideTime()[i-1])
                    return false;
            }
        }
        return true;
    }
}