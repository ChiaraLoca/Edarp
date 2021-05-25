package solutionCheck;

// constraint 12:
// Calculate the excess ride-time for user i

import model.Solution;

public class ExcessRideTime extends AbstractConstraint{
    public ExcessRideTime(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        int n = solution.getInstance().getPickupLocationsId().length;

        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (int i:solution.getInstance().getPickupLocationsId()) {
                double value = solution.getTimeVehicleStartsAtLocation()[k][n+i-1]
                        -solution.getTimeVehicleStartsAtLocation()[k][n+i-1]
                        -solution.getInstance().getNodes().get(i-1).getServiceTime()
                        -solution.getInstance().getTravelTime()[i-1][n+i-1];

                double diff = Math.abs(value-solution.getExcessRideTimeOfPassenger()[i-1]);
                if (solution.getExcessRideTimeOfPassenger()[i-1]<value && diff > TOLERANCE)
                    return false;
            }
        }
        return true;
    }
}
