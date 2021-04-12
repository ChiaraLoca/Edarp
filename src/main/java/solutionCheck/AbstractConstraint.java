package solutionCheck;

import model.Solution;

public abstract class AbstractConstraint {
    protected Solution solution;

    public AbstractConstraint(Solution solution) {
        this.solution=solution;
    }

    abstract boolean check();
}
