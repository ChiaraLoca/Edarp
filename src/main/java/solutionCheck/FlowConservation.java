package solutionCheck;

// constraint 5:
// Flow conservation

import model.Node;
import model.Solution;

import java.util.stream.Collectors;

public class FlowConservation extends AbstractConstraint{
    public FlowConservation(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        int[] N = solution.getInstance().getPickupAndDropoffLocations()
                .stream().mapToInt(Node::getId).toArray();
        Integer[] NSUnion = arrayUnion(N,
                solution.getInstance().getChargingStationId());

        for (int k = 0; k < solution.getInstance().getnVehicles(); k++) {
            for (int i:NSUnion) {
                int sum1 = 0,sum2 = 0;
                for (Node node:solution.getInstance().getNodes()) {
                    int j = node.getId();
                    if(i==j)
                        continue;

                    sum1 = solution.getVehicleSeqStopAtLocations()[k][i][j];
                    sum2 = solution.getVehicleSeqStopAtLocations()[k][j][i];
                }
                if(sum1-sum2 !=0){
                    return false;
                }
            }

        }
        return true;
    }
}
