package solutionCheck;

// constraint 17: Lki = 0       ∀k ∈ K, i ∈ ok ∪ F ∪ S
// ensure that vehicles are empty at depots and charging stations
import model.Solution;

public class EmptyVehiclesAtDepotsAndStation extends AbstractConstraint {

    public EmptyVehiclesAtDepotsAndStation(Solution solution) {
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