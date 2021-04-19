package solutionCheck;

// constraint 3:
// Guarantee that all vehicles return to a selected destination depot

import model.Solution;

public class ReturnToDestinationDepot extends AbstractConstraint{
    public ReturnToDestinationDepot(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {

        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            Integer[] DSOkUnion = arrayUnion(solution.getInstance().getDropoffLocationsId(),
                                            solution.getInstance().getChargingStationId(),
                                            new int[]{solution.getInstance().getArtificialDestinationDepotId()[k]});
            int sum=0;
            for (int j : solution.getInstance().getArtificialDestinationDepotId()) {
                for (int i : DSOkUnion) {
                    sum += solution.getVehicleSeqStopAtLocations()[k][i][j];
                }
            }
            if (sum != 1)
                return false;
        }
            return true;
    }
}
