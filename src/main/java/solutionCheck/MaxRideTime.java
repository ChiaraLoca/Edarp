package solutionCheck;

// constraint 10:
// Impose maximum ride-times for the users

import model.Solution;

public class MaxRideTime extends AbstractConstraint {

    public MaxRideTime(Solution solution) {
        super(solution);
    }

    // TODO: check index

    @Override
    boolean check() {
        for(int k=0; k<solution.getList().size(); k++) {
            for(int i=0; i<solution.getInstance().getArtificialOriginDepotId().length+solution.getInstance().getAllAvailableDestinationDepotsId().length+solution.getInstance().getChargingStationId().length; i++) {
                if(solution.getLoadOfVehicleAtLocation()[k][i]!=0)
                    return false;
            }
        }
        return true;
    }
}