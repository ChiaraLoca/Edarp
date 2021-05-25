package solutionCheck;

// constraint 2:
// Ensure that all vehicles exit their origin depots and visit
//  a pickup location P,relocate to a charging station S,or proceed to a destination depot F

import model.Solution;

public class ControlPath extends AbstractConstraint {
    public ControlPath(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        Integer[] PSFUnion = arrayUnion(solution.getInstance().getPickupLocationsId(),
                                        solution.getInstance().getChargingStationId(),
                                        solution.getInstance().getAllAvailableDestinationDepotsId());

        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            int sum = 0;
            for (int j : PSFUnion) {
                sum += solution.getVehicleSeqStopAtLocations()[k]
                        [solution.getInstance().getArtificialDestinationDepotId()[k]-1][j-1];
            }
            if (sum != 1)
                return false; // todo: bisogna guardarlo
        }

        return true;
    }
}