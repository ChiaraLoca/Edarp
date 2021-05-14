package solutionCheck;

// constraint 4:
// Allow each optional destination depot and charging station to be visited at most once
//   Such locations can be replicated in order to allow multiple visits to the nodes in F And S.

import model.Solution;

public class VisitOnceOptionalDestination extends AbstractConstraint{
    public VisitOnceOptionalDestination(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        Integer[] FSUnion = arrayUnion(solution.getInstance().getAllAvailableDestinationDepotsId(),
                solution.getInstance().getPickupLocationsId());

        for (int j:FSUnion) {
            int sum=0;
            for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
                Integer[] DSOkUnion = arrayUnion(solution.getInstance().getDropoffLocationsId(),
                        solution.getInstance().getChargingStationId(),
                        new int[]{solution.getInstance().getArtificialDestinationDepotId()[k]});
                for (int i:DSOkUnion) {
                    sum+=solution.getVehicleSeqStopAtLocations()[k][i][j];
                }
            }
            if(sum>1)
                return false;
        }
        return true;
    }
}
