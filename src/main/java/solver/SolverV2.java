package solver;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolverV2 implements ISolver {

    private final Instance instance;
    private Solution solution;
    private  Map<Node,Node> unvisitedNodesMap = new HashMap<>();
    private List<OneVehicleSolver> oneVehicleSolvers = new ArrayList<>();




    public SolverV2(Instance instance) {
        this.instance = instance;
        this.solution = new Solution(instance);

        for (Node n : instance.getPickupAndDropoffLocations()) {
            if (n.getNodeType().equals(NodeType.PICKUP))
                unvisitedNodesMap.put(n, instance.getPickupAndDropoffLocations().get(n.getId() + instance.getnCustomers() - 1));
        }
        unvisitedNodesMap = Util.orderNodeNodeMapBy(unvisitedNodesMap,Order.DESTINATION_DEPARTURE);
    }


    public void step(int id,int iteration,double time,Map<Node,Node> nodes,List<ExpiredNodesTime> priorityNodes) throws Exception {
        Util.printStepinfo(id,iteration,time,nodes);

        OneVehicleSolver solver =
                new OneVehicleSolver(instance,true,id,new HashMap<>(nodes),time,priorityNodes==null?null:new ArrayList<>(priorityNodes));
        solver.solve();
        oneVehicleSolvers.add(solver);

    }

    public void firstStep(Map<Node,Node> nodes) throws Exception {
        for(int i =0;i<instance.getnVehicles();i++)
        {
            step(i,0,0,i==0?nodes:oneVehicleSolvers.get(i-1).getUnvisitedNodesMap(),null);
        }
    }


    /*public void otherStep(Map<Node,Node> nodes,int iteration) throws Exception{

        int offset = iteration*instance.getnVehicles();
        for(int i =0;i<instance.getnVehicles();i++){
            double time = oneVehicleSolvers.get(i+offset-1).getVehicleInfo().getTimeOfMission() -
                    oneVehicleSolvers.get(i+offset-2).getVehicleInfo().getTimeOfMission();

            step(i,iteration,time,i==0?nodes:oneVehicleSolvers.get(i-1).getUnvisitedNodesMap(),
                    i==0

            );

        }
    }*/

    @Override
    public Solution solve()throws Exception {

       /* int iteration =0;
        firstStep(unvisitedNodesMap);
        iteration++;


        int nodiRimasti=unvisitedNodesMap.size();
        while(nodiRimasti>0 && iteration<10)
        {

            otherStep(unvisitedNodesMap,iteration);
            iteration++;
            nodiRimasti= oneVehicleSolvers.get(iteration*instance.getnVehicles()-1).getUnvisitedNodesMap().size();
            Util.printGreen("nodiRimasti: "+nodiRimasti);
        }*/

        step(0,0,0,unvisitedNodesMap,null);
        step(1,0,0,unvisitedNodesMap,null);
        List<ExpiredNodesTime> tmp = oneVehicleSolvers.get(0).getExpiredNodes();
        tmp.sort((o1, o2) -> o1.getExpiredNode().getDropoff().getArrival()>o2.getExpiredNode().getDropoff().getArrival() ? 1:-1 );
        step(0,0,0,unvisitedNodesMap,tmp);
        tmp = oneVehicleSolvers.get(1).getExpiredNodes();
        tmp.sort((o1, o2) -> o1.getExpiredNode().getDropoff().getArrival()>o2.getExpiredNode().getDropoff().getArrival() ? 1:-1 );
        step(1,0,0,unvisitedNodesMap,tmp);





        //step(1,0,0,oneVehicleSolvers.get(1).getUnvisitedNodesMap(),tmp);






        /*tmp = oneVehicleSolvers.get(0).getExpiredNodes();
        tmp.sort((o1, o2) -> o1.getExpiredNode().getDropoff().getArrival()>o2.getExpiredNode().getDropoff().getArrival() ? 1:-1 );

        step(0,0,20,unvisitedNodesMap,tmp);

        tmp = oneVehicleSolvers.get(2).getExpiredNodes();
        tmp.sort((o1, o2) -> o1.getExpiredNode().getDropoff().getArrival()>o2.getExpiredNode().getDropoff().getArrival() ? 1:-1 );

        step(1,0,20,oneVehicleSolvers.get(1).getUnvisitedNodesMap(),tmp);*/


        return solution;
    }







}
