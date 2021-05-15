package scorer;

import model.VehicleInfo;
import model.WaitingInfo;

import java.util.List;

public interface Scorer {
    double score(List<List<VehicleInfo>> solution, List<List<WaitingInfo>> waits);
}
