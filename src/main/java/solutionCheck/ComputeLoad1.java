package solutionCheck;

// constraint 13:
// Load at location j ∈ V is computed from the load at the preceding location i ∈ V
//  and the change in load at location j

import model.Node;
import model.Solution;

public class ComputeLoad1 extends AbstractConstraint{
    public ComputeLoad1(Solution solution) {
        super(solution);
    }
    // TODO: index-1

    @Override
    boolean check() {
        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (Node ni: solution.getInstance().getNodes()) {
                int i = ni.getId();
                for (int j : solution.getInstance().getNodes().stream().mapToInt(Node::getId).toArray()) {
                    if (i == j)
                        continue;
                    double v1 = solution.getLoadOfVehicleAtLocation()[k][i-1]+
                            ni.getLoad();
                    double v2 = -solution.getInstance().getG()[k][i-1]
                            * (1-solution.getVehicleSeqStopAtLocations()[k][i-1][j-1]);

                    if(v1+v2>solution.getLoadOfVehicleAtLocation()[k][i-1])
                        return false;
                }
            }
        }

        return true;
    }
}
