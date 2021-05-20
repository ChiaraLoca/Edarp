package model;

import scorer.Scorer;
import scorer.TravelScorer;

import java.util.ArrayList;
import java.util.List;

public class Translate {

    private Instance instance;
    private List<List<List<VehicleInfo>>> allVehicleInfoList;
    List<Solution> solutions = new ArrayList<>();

    private int K;
    private int V;
    private int C;

    private Scorer scorer = new TravelScorer();
    public Translate(Instance instance, List<List<List<VehicleInfo>>> allVehicleInfoList ) {
        this.instance = instance;
        this.allVehicleInfoList = allVehicleInfoList;
        K = instance.getnVehicles();
        V = instance.getNodes().size();
        C = instance.getChargingStationId().length;
    }

    public List<Solution> translateAll(){


        for(List<List<VehicleInfo>> vehicleInfoList : allVehicleInfoList) {
            Solution s = translate(vehicleInfoList);
            s.setScore(scorer.score(s));
            solutions.add(s);
        }


        return solutions;
    }

    public Solution translate(List<List<VehicleInfo>> vehicleInfoList)
    {
        Solution solution = new Solution(instance);

        /*

        vehicleSeqStopAtLocations = new int[K][V][V];
        timeVehicleStartsAtLocation =  new double[K][V];
        loadOfVehicleAtLocation = new double [K][V];
        batteryLoadOfVehicleAtLocation = new double [K][V];
        chargingTimeOfVehicleAtStation = new double [K][instance.getChargingStationId().length];


         */


        //vehicleSeqStopAtLocations = new int[K][V][V];
        for(int i = 0; i<K; i++)
        {
            Node prima = vehicleInfoList.get(i).get(0).getCurrentPosition();
            Node ora =null;
            for(int n = 1; n<vehicleInfoList.get(i).size(); n++)
            {
                ora =vehicleInfoList.get(i).get(n).getCurrentPosition();
                solution.getVehicleSeqStopAtLocations()[i][prima.getId()-1][ora.getId()-1]=1;
                prima = ora;
            }
        }

        //timeVehicleStartsAtLocation =  new double[K][V];
        //loadOfVehicleAtLocation = new double [K][V];
        //batteryLoadOfVehicleAtLocation = new double [K][V];

        for(int i = 0; i<K; i++) {
            Node node;
            for(int n = 0; n<vehicleInfoList.get(i).size(); n++) {
                node = vehicleInfoList.get(i).get(n).getCurrentPosition();
                solution.getTimeVehicleStartsAtLocation()[i][node.getId()-1] = vehicleInfoList.get(i).get(n).getTimeOfMission();

                solution.getLoadOfVehicleAtLocation()[i][node.getId()-1] = vehicleInfoList.get(i).get(n).getPassengerDestination().size();

                solution.getBatteryLoadOfVehicleAtLocation()[i][node.getId()-1] = vehicleInfoList.get(i).get(n).getCurrentBatteryLevel();
            }
        }

        //chargingTimeOfVehicleAtStation = new double [K][instance.getChargingStationId().length];
        for(int i = 0; i<K; i++) {
            Node node;
            VehicleInfo info;
            for(int n = 1; n<vehicleInfoList.get(i).size(); n++) {
                info = vehicleInfoList.get(i).get(n);
                node = info.getCurrentPosition();

                if(info.getTimeSpendAtCharging()>0 && node.getNodeType().equals(NodeType.CHARGE)) {
                    int position = node.getId()-instance.getChargingStationId()[0];
                    solution.getChargingTimeOfVehicleAtStation()[i][position]+= info.getTimeSpendAtCharging();
                }
            }
        }





            return solution;

    }

    public void sort()
    {
        List<Double> maxTime = new ArrayList<>();
        for (Solution s:solutions) {
            double tmp =Double.MIN_VALUE;
            int indexK=0;
            int indexJ=0;
            for(int k = 0; k<K; k++)
            {
                for(int j = 0; j<s.getTimeVehicleStartsAtLocation()[k].length; j++)
                {
                    if(s.getTimeVehicleStartsAtLocation()[k][j]>tmp) {
                        tmp = s.getTimeVehicleStartsAtLocation()[k][j];
                        indexK =k;
                        indexJ =j;
                    }


                }
            }

            maxTime.add(s.getTimeVehicleStartsAtLocation()[indexK][indexJ]);

        }

        System.out.println();
    }
}
