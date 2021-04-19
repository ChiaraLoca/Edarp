package solutionCheck;

// constraint 11:
// Lower bound on the service start time at location j ∈ V, which is visited right after location i ∈ V

import model.Solution;

public class ServiceStartTime extends AbstractConstraint{
    public ServiceStartTime(Solution solution) {
        super(solution);
    }

    @Override
    boolean check() {
        return false;
    }
}
