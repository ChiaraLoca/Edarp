package model;

import scorer.Scorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SolutionHolder {
    private Solution solution;
    private Scorer scorer;
    private double score;



    public SolutionHolder(Solution solution, List<List<WaitingInfo>> waitingInfos, Scorer scorer) {
        this.solution = solution;
        this.waitingInfos = waitingInfos;
        this.score= scorer.score(solution);
    }

    private List<List<WaitingInfo>> waitingInfos;



    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<List<WaitingInfo>> getWaitingInfos() {
        return waitingInfos;
    }

    public void setWaitingInfos(List<List<WaitingInfo>> waitingInfos) {
        this.waitingInfos = waitingInfos;
    }






}
