package scorer;

import model.VehicleInfo;
import model.WaitingInfo;

import java.util.List;

public class WaitsScorer {

    public double score(List<List<VehicleInfo>> solution, List<List<WaitingInfo>> waits) {
        double score= Double.MAX_VALUE;
        for(List<WaitingInfo> list : waits){
            double cw=0;
            for(WaitingInfo w : list){
                cw= Math.max(w.getWaitTime(), cw);
            }
            score= Math.min(score, cw);
        }
        return score;
    }
}
