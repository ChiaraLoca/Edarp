package scorer;

import model.Solution;
import model.VehicleInfo;
import model.WaitingInfo;

import java.util.List;

public interface Scorer {
    double score(Solution solution);
}
