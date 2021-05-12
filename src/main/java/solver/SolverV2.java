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


    public void step(int id,int iteration,double time,Map<Node,Node> nodes) throws Exception {
        Util.printStepinfo(id,iteration,time,nodes);

        OneVehicleSolver solver = new OneVehicleSolver(instance,false,id,new HashMap<>(nodes),time);
        solver.solve();
        oneVehicleSolvers.add(solver);

    }

    public void firstStep(Map<Node,Node> nodes) throws Exception {
        for(int i =0;i<instance.getnVehicles();i++)
        {
            step(i,0,0,i==0?nodes:oneVehicleSolvers.get(i-1).getUnvisitedNodesMap());
        }
    }

    public void otherStep(Map<Node,Node> nodes,int iteration) throws Exception{

        int offset = iteration*instance.getnVehicles();
        for(int i =0;i<instance.getnVehicles();i++){
            double time = oneVehicleSolvers.get(i+offset-1).getVehicleInfo().getTimeOfMission() -
                    oneVehicleSolvers.get(i+offset-2).getVehicleInfo().getTimeOfMission();

            step(i,iteration,time,i==0?nodes:oneVehicleSolvers.get(i-1).getUnvisitedNodesMap());

        }
    }

    @Override
    public Solution solve()throws Exception
    {

        int iteration =0;
        firstStep(unvisitedNodesMap);
        iteration++;

        int nodiRimasti=unvisitedNodesMap.size();
        while(nodiRimasti>0 && iteration<10)
        {

            otherStep(unvisitedNodesMap,iteration);
            iteration++;
            nodiRimasti= oneVehicleSolvers.get(iteration*instance.getnVehicles()-1).getUnvisitedNodesMap().size();
            Util.printGreen("nodiRimasti: "+nodiRimasti);
        }



        /*

        for(int i =0;i<instance.getnVehicles();i++)
        {
            firstStep(i,0,iteration);

        }
        iteration+=instance.getnVehicles();

        for (Node n : instance.getPickupAndDropoffLocations()) {
            if (n.getNodeType().equals(NodeType.PICKUP))
                unvisitedNodesMap.put(n, instance.getPickupAndDropoffLocations().get(n.getId() + instance.getnCustomers() - 1));
        }
        unvisitedNodesMap = Util.orderNodeNodeMapBy(unvisitedNodesMap,Order.DESTINATION_DEPARTURE);


        firstStep(0,oneVehicleSolvers.get(1).getVehicleInfo().getTimeOfMission()-oneVehicleSolvers.get(0).getVehicleInfo().getTimeOfMission(),iteration);

firstStep(1,oneVehicleSolvers.get(0+iteration).getVehicleInfo().getTimeOfMission()-oneVehicleSolvers.get(1).getVehicleInfo().getTimeOfMission(),iteration);






        Util.printRed("Nodi rimasti ");
        for(Map.Entry e: partialSolution.get(3).getUnvisitedNodeMap().entrySet() )
        {
            Util.printRed(((Node)e.getKey()).getId()+" - ");
        }
        System.out.println("\n\n");*/

        return solution;
    }



    public void firstStep(int id,double time,int iteration) throws Exception {

       /* Util.printRed("\n\nVEICOLO NUMERO "+id+"-----------------------------------------------\n");

        Map<Node,Node> tmp = (id==0?unvisitedNodesMap : partialSolution.get(id-1+iteration).getUnvisitedNodeMap());
        Util.printRed("Nodi rimasti ");
        for(Map.Entry e: tmp.entrySet() )
        {
            Util.printRed(((Node)e.getKey()).getId()+" - ");
        }
        System.out.println("\n\n");


        oneVehicleSolvers.add( new OneVehicleSolver(instance,false,id, (HashMap<Node, Node>) tmp,time));
        oneVehicleSolvers.get(id+iteration).solve();*/
    }







}
