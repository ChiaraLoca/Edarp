package model;

import scorer.Scorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SolutionHolder {
    private List<List<VehicleInfo>> vehicleInfos;
    private List<List<WaitingInfo>> waits;

    public List<List<VehicleInfo>> getVehicleInfos() {
        return vehicleInfos;
    }

    public void setVehicleInfos(List<List<VehicleInfo>> vehicleInfos) {
        this.vehicleInfos = vehicleInfos;
    }

    public List<List<WaitingInfo>> getWaits() {
        return waits;
    }

    public void setWaits(List<List<WaitingInfo>> waits) {
        this.waits = waits;
    }

    public SolutionHolder(List<List<VehicleInfo>> vehicleInfos, List<List<WaitingInfo>> waits) {
        this.vehicleInfos = vehicleInfos;
        this.waits = waits;
    }


    public boolean equals(SolutionHolder solutionHolder) {
        for(List<VehicleInfo> v: vehicleInfos){
            if (v.size()!=solutionHolder.getVehicleInfos().get(vehicleInfos.indexOf(v)).size())
                return false;
        }
        for(List<VehicleInfo> v: vehicleInfos){
            for(VehicleInfo vehicleInfo: v){
                if(!vehicleInfo.getCurrentPosition().equals(
                        solutionHolder.getVehicleInfos().get(vehicleInfos.indexOf(v)).get(v.indexOf(vehicleInfo)).getCurrentPosition()
                )){
                   return false;
                }
            }
        }
        return true;


    }


}
