package solutionCheck;

// constraint 9:
// Set time windows around the beginning of service at each location

import model.Node;
import model.Solution;

public class BeginningOfService extends AbstractConstraint{
    public BeginningOfService(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        /*for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (Node ni:solution.getInstance().getNodes()) {
                int i = ni.getId();
                if(ni.getArrival()>solution.getTimeVehicleStartsAtLocation()[k][i])
                    return false;
                if(solution.getTimeVehicleStartsAtLocation()[k][i]>ni.getDeparture())
                    return false;
            }
        }*/
        return true;
    }
}
