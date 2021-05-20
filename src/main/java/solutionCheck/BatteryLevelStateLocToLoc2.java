package solutionCheck;

// constraint 20: Bkj ≥ Bki − βi,j - Q(1 − xk,i,j)      ∀k ∈ K, i ∈ V \ S, j ∈ V \ {ok}, i != j
// set the battery level state from any location i ∈ V \ S to any location j ∈ Vo(k)

import model.Node;
import model.Solution;

import java.util.List;

public class BatteryLevelStateLocToLoc2 extends AbstractConstraint {

    public BatteryLevelStateLocToLoc2(Solution solution) {
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
                    if(solution.getBatteryLoadOfVehicleAtLocation()[k][union2[j]-1]
                            <solution.getBatteryLoadOfVehicleAtLocation()[k][union1[i]-1]
                            -solution.getInstance().getBatteryConsumption()[union1[i]-1][union2[j]-1]
                            -solution.getInstance().getVehicleBatteryCapacity()[k]
                            *(1-solution.getVehicleSeqStopAtLocations()[k][union1[i]-1][union2[j]-1]))
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