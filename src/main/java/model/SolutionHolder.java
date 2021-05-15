package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SolutionHolder {
    private List<List<VehicleInfo>> solution;
    private List<List<WaitingInfo>> waitingInfos;
    private double score;

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

    public List<List<WaitingInfo>> getWaitingInfos() {
        return waitingInfos;
    }

    public void setWaitingInfos(List<List<WaitingInfo>> waitingInfos) {
        this.waitingInfos = waitingInfos;
    }

    public SolutionHolder(List<List<VehicleInfo>> solution, List<List<WaitingInfo>> waitingInfos, double score) {
        this.solution = solution;
        this.waitingInfos = waitingInfos;
        this.score = score;
    }

    public SolutionHolder(SolutionHolder sh)
    {
        this.solution = new ArrayList<>();
        for (int i=0;i<sh.getSolution().size();i++)
            this.solution.add(new ArrayList<>(sh.getSolution().get(i)));

        this.waitingInfos = new ArrayList<>();
        for (int i=0;i<sh.getWaitingInfos().size();i++)
            this.waitingInfos.add(new ArrayList<>(sh.getWaitingInfos().get(i)));

        this.score = sh.getScore();


        if(!this.equals(sh))
        {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolutionHolder)) return false;
        SolutionHolder holder = (SolutionHolder) o;
        return Double.compare(holder.getScore(), getScore()) == 0 &&
                getSolution().equals(holder.getSolution()) &&
                getWaitingInfos().equals(holder.getWaitingInfos());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSolution(), getWaitingInfos(), getScore());
    }
}
