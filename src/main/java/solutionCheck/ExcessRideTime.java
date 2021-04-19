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
                double value = solution.getTimeVehicleStartsAtLocation()[k][n+i] -
                        solution.getTimeVehicleStartsAtLocation()[k][n+i] -
                        solution.getInstance().getNodes().get(i).getServiceTime() -
                        solution.getInstance().getTravelTime()[i][n+i];
                if (solution.getExcessRideTimeOfPassenger()[i]<value)
                    return false;
            }
        }
        return true;
    }
}
