package scorer;

import model.Solution;

public class TravelScorer implements Scorer {
    @Override
    public double score(Solution solution) {
        //w1*(sum {k in K,i in V,j in V} t[i,j] * X[k,i,j]) + w2 * sum {i in P} R[i];
        double w1 = solution.getInstance().getWeightFactor()[0];
        double t[][] = solution.getInstance().getTravelTime();
        int X[][][] = solution.getVehicleSeqStopAtLocations();
        int K = solution.getInstance().getnVehicles();
        int V = solution.getInstance().getNodes().size();
        double score =0;

        for (int k = 0; k < K ; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    score += t[i][j]* X[k][i][j];
                }
            }
        }
        score*=w1;
        return score;
    }
}
