package model;

import java.util.List;

public class SolutionHolder {
    private List<List<VehicleInfo>> solution;

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

    private double score;
}
