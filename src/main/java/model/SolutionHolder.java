package model;

import scorer.Scorer;

import java.util.List;

public class SolutionHolder {
    private List<List<VehicleInfo>> solution;
    private Scorer scorer;
    private double score;

    public List<List<WaitingInfo>> getWaitingInfos() {
        return waitingInfos;
    }

    public void setWaitingInfos(List<List<WaitingInfo>> waitingInfos) {
        this.waitingInfos = waitingInfos;
    }

    public SolutionHolder(List<List<VehicleInfo>> solution, List<List<WaitingInfo>> waitingInfos, Scorer scorer) {
        this.solution = solution;
        this.waitingInfos = waitingInfos;
        this.score= scorer.score(solution, waitingInfos);
    }

    private List<List<WaitingInfo>> waitingInfos;

    public List<List<VehicleInfo>> getSolution() {
        return solution;
    }

    public void setSolution(List<List<VehicleInfo>> solution) {
        this.solution = solution;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public SolutionHolder(List<List<VehicleInfo>> solution, double score) {
        this.solution = solution;
        this.score = score;
    }

}
