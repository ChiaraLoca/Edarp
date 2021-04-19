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
        for(int k=0; k<solution.getList().size(); k++) {
            for (int i: solution.getInstance().getPickupLocationsId()) {
                double value = solution.getTimeVehicleStartsAtLocation()[k][n+i] -
                        solution.getTimeVehicleStartsAtLocation()[k][i] -
                        0; //todo d[i]
                if (value>0) // todo u[i]
                    return false;
            }
        }
        return true;
    }
}