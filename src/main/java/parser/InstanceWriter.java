package parser;

import model.Instance;
import model.Node;

import java.io.File;

import java.io.IOException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class InstanceWriter {

    private static InstanceWriter instanceWriter=null;
    private InstanceWriter(){}
    public static InstanceWriter getInstanceWriter(){
        if (instanceWriter==null)
            instanceWriter=new InstanceWriter();
        return instanceWriter;
    }


    public void write(Instance instance)
    {
        File file = new File("Ampl/"+instance.getTitle()+".dat");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }




        try {

            PrintWriter printWriter = new PrintWriter(file);

            //n = number of stations*/
            printWriter.println("param n := "+instance.getnStations()+";");

            //numVehicles = number of vehicles*/
            printWriter.println("param numVehicles := "+instance.getnVehicles()+";");

            //set F = All avaible destinations depots*/
            printWriter.print("set F :=");
            for(int i =0;i<instance.getnDestinationDepots();i++)
            {
                printWriter.print(" "+instance.getCommonDestinationDepotId()[i]);
            }
            printWriter.println(";");

            //set S = Charging stations*/
            printWriter.print("set S :=");
            for(int i =0;i<instance.getChargingStationId().length;i++)
            {
                printWriter.print(" "+instance.getChargingStationId()[i]);
            }
            printWriter.println(";");


            //TODO   param t{V,V}; 		# Travel time from location V to location V
             //param arr{V}; 		# Earliest time at which service can begin at V*/
            printWriter.print("param arr{V} :=");
            for(Node n: instance.getNodes())
            {
                printWriter.print(" "+n.getId()+ " "+ n.getArrival());
            }
            printWriter.println(";");

            //param dep{V}; 		# Latest time at which service can begin at V*/
            printWriter.print("param dep{V} :=");
            for(Node n: instance.getNodes())
            {
                printWriter.print(" "+n.getId()+ " "+ n.getDeparture());
            }
            printWriter.println(";");

             //param d{V};	 		# Service duration at location V*/
            printWriter.print("param d{V} :=");
            for(Node n: instance.getNodes())
            {
                printWriter.print(" "+n.getId()+ " "+ n.getServiceTime());
            }
            printWriter.println(";");

             //param l{N};			# Change in load at location N*/
            printWriter.print("param l{V} :=");
            for(Node n: instance.getNodes())
            {
                if(n.getLoad()!=0)
                    printWriter.print(" "+n.getId()+ " "+ n.getLoad());
            }
            printWriter.println(";");

            ///param u{P};			# Maximum ride-time for customer with pickup at P*/
            int i =0;
            printWriter.print("param u{P} :=");
            for(Node n: instance.getNodes())
            {
                if(n.getLoad()>0) {
                    printWriter.print(" " + n.getId() + " " + instance.getUserMaxRideTime()[i]);
                    i++;
                }
            }
            printWriter.println(";");


             ///param c{K};			# Capacity of vehicle K*/
            printWriter.print("param c{K} :=");
            for(i =0;i<instance.getChargingStationId().length;i++)
            {
                printWriter.print(" "+instance.getChargingStationId()[i]);
            }
            printWriter.println(";");

            //param Q;			# Effective battery capacity*/
            printWriter.println("param Q := "+instance.getVehicleBatteryCapacity()[0]+";");

            //*param Binit{K};		# Initial battery capacity of vehicle K*/
            printWriter.print("param Binit{K} :=");
            for(i =0;i<instance.getVehicleInitBatteryInventory().length;i++)
            {
                printWriter.print(" "+(i+1)+" "+instance.getVehicleInitBatteryInventory()[i]);
            }
            printWriter.println(";");

           //* param r;			# Final minimum battery level ratio*/
            printWriter.println("param r := "+instance.getMinBatteryRatioLvl()+";");

            //*param beta{V,V};	# Battery consumption between nodes i, j in V*/

            //*param alpha{S};		# Recharge rate at charging facility S*/
            printWriter.print("param alpha{S} :=");
            for(i =0;i<instance.getStationRechargingRate().length;i++)
            {
                printWriter.print(" "+instance.getChargingStationId()[i]+" "+instance.getStationRechargingRate()[i]);
            }
            printWriter.println(";");

            //*param Tp;			# Planning horizon*/
            printWriter.println("param Tp := "+instance.getTimeHorizon()+";");


            //*param w1;*/
            printWriter.println("param w1 := "+instance.getWeightFactor()[0]+";");
            //*param w2;*/
            printWriter.println("param w2 := "+instance.getWeightFactor()[1]+";");

            //TODO param M{V,V};		#M[i,j] = max{0, dep[i]+d[i]+t[i,j] *arr[j] } --> in file .dat*/

            //*param G{K,V};		#G[k,j] = min{C[k],C[k]+l[i]}*/
            //K= numero veicoli
            int K = instance.getnVehicles();
            //V = all nodes
            int V =instance.getNodes().size();
            double G[][] = new double[K][V];
            for(int k =0;k<K;k++)
            {
                for(int j =0;j<V;j++)
                {
                    if(instance.getVehicleCapacity()[k]<instance.getVehicleCapacity()[k]+instance.getNodes().get(j).getLoad())
                    {
                        G[k][j] =instance.getVehicleCapacity()[k];
                    }
                    else {
                        G[k][j] =instance.getVehicleCapacity()[k]+ instance.getNodes().get(j).getLoad();
                    }
                }
            }

            printWriter.println("param G{K,V}:");
            for(i =0;i<V;i++)
            {
                printWriter.print(" "+(i+1));
            }
            printWriter.println(":=");
            for(int k =0;k<K;k++)
            {
                printWriter.print((k+1));

                for(int j =0;j<V;j++)
                {
                    printWriter.print(" "+G[k][j]);
                }
                printWriter.println();
            }
            printWriter.println(";");

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}



