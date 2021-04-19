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
        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (Node node:solution.getInstance().getNodes()) {
                int i = node.getId();
                if(0>solution.getTimeVehicleStartsAtLocation()[k][i]) //todo arr[i]
                    return false;
                if(solution.getTimeVehicleStartsAtLocation()[k][i]>0) //todo dep[i]
                    return false;
            }
        }
        return true;
    }
}
