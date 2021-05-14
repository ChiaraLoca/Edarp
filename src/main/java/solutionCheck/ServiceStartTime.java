package solutionCheck;

// constraint 11:
// Lower bound on the service start time at location j ∈ V, which is visited right after location i ∈ V

import model.Node;
import model.Solution;

public class ServiceStartTime extends AbstractConstraint{
    public ServiceStartTime(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (Node ni: solution.getInstance().getNodes()) {
                int i = ni.getId();
                for (int j: solution.getInstance().getNodes().stream().mapToInt(Node::getId).toArray()) {
                    if(i==j)
                        continue;
                    double v1 = solution.getTimeVehicleStartsAtLocation()[k][i]
                            +solution.getInstance().getTravelTime()[i][j]
                            +ni.getServiceTime();
                    double v2 = -solution.getInstance().getM()[i][j] * (1-solution.getVehicleSeqStopAtLocations()[k][i][j]);

                    if(v1+v2>solution.getTimeVehicleStartsAtLocation()[k][j])
                        return false;
                }
            }
        }
        return true;
    }
}
