package solutionCheck;

// constraint 19: Bkj ≤ Bki − βi,j + Q(1 − xk,i,j)      ∀k ∈ K, i ∈ V \ S, j ∈ V \ {ok}, i != j
// set the battery level state from any location i ∈ V \ S to any location j ∈ Vo(k)

import model.Node;
import model.Solution;

import java.util.List;

public class BatteryLevelStateLocToLoc1 extends AbstractConstraint {

    public BatteryLevelStateLocToLoc1(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        Integer[] union1=arrayDiff(nodeToArray(solution.getInstance().getNodes()),solution.getInstance().getChargingStationId());
        Integer[] union2=arrayDiff(nodeToArray(solution.getInstance().getNodes()),solution.getInstance().getArtificialOriginDepotId());
        for(int k=0; k<solution.getInstance().getnVehicles(); k++) {
            for(int i=0; i<union1.length; i++) {
                for(int j=0; j<union2.length; j++) {
                    if(i==j)
                        continue;
                    if(solution.getBatteryLoadOfVehicleAtLocation()[k][union2[j]]
                            >solution.getBatteryLoadOfVehicleAtLocation()[k][union1[i]]
                            -solution.getInstance().getBatteryConsumption()[union1[i]][union2[j]]
                            +solution.getInstance().getVehicleBatteryCapacity()[k]
                            *(1-solution.getVehicleSeqStopAtLocations()[k][union1[i]][union2[j]]))
                        return false;
                }
            }
        }
        return true;
    }

    private int[] nodeToArray(List<Node> list) {
        int[] array=new int[list.size()];
        int cont=0;
        for(Node n:list) {
            array[cont++]=n.getId();
        }
        return array;
    }
}