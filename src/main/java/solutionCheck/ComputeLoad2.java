package solutionCheck;

// constraint 14:
// Load at location j ∈ V is computed from the load at the preceding location i ∈ V
//  and the change in load at location j
// Redundant with respect to the model formulation. However, they help strengthening the LP relaxations

import model.Node;
import model.Solution;

public class ComputeLoad2 extends AbstractConstraint{
    public ComputeLoad2(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (Node ni : solution.getInstance().getNodes()) {
                int i = ni.getId();
                for (int j : solution.getInstance().getNodes().stream().mapToInt(Node::getId).toArray()) {
                    if (i == j)
                        continue;
                    double v1 = solution.getLoadOfVehicleAtLocation()[k][i] +
                            ni.getLoad();
                    double v2 = solution.getInstance().getG()[k][i] * (1 - solution.getVehicleSeqStopAtLocations()[k][i][j]);

                    if (v1 + v2 < solution.getLoadOfVehicleAtLocation()[k][i])
                        return false;
                }
            }
        }

        return true;
    }
}
