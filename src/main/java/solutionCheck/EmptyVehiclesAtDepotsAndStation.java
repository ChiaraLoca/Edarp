package solutionCheck;

// constraint 17: Lki = 0       ∀k ∈ K, i ∈ ok ∪ F ∪ S
// ensure that vehicles are empty at depots and charging stations
import model.Solution;

public class EmptyVehiclesAtDepotsAndStation extends AbstractConstraint {

    public EmptyVehiclesAtDepotsAndStation(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        Integer[] union = arrayUnion(solution.getInstance().getArtificialOriginDepotId(),solution.getInstance().getAllAvailableDestinationDepotsId(),solution.getInstance().getChargingStationId());
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int i=0; i<union.length; i++) {
                if(solution.getLoadOfVehicleAtLocation()[k][union[i]-1]!=0)
                    return false;
            }
        }
        return true;
    }
}