package parser;

import model.Instance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

            /**n = number of stations*/
            printWriter.println("param n := "+instance.getnStations()+";");

            /**numVehicles = number of vehicles*/
            printWriter.println("param numVehicles := "+instance.getnVehicles()+";");

            /**set F = All avaible destinations depots*/
            printWriter.print("set F :=");
            for(int i =0;i<instance.getnDestinationDepots();i++)
            {
                printWriter.print(" "+instance.getCommonDestinationDepotId()[i]);
            }
            printWriter.println(";");

            /**set S = Charging stations*/
            printWriter.print("set S :=");
            for(int i =0;i<instance.getChargingStationId().length;i++)
            {
                printWriter.print(" "+instance.getChargingStationId()[i]);
            }
            printWriter.println(";");

            //TODO
            /**    param t{V,V}; 		# Travel time from location V to location V
             param arr{V}; 		# Earliest time at which service can begin at V
             param dep{V}; 		# Latest time at which service can begin at V
             param d{V};	 		# Service duration at location V
             param l{N};			# Change in load at location N
             param u{P};			# Maximum ride-time for customer with pickup at P*/

            /**param c{K};			# Capacity of vehicle K*/
            printWriter.print("param c{K} :=");
            for(int i =0;i<instance.getChargingStationId().length;i++)
            {
                printWriter.print(" "+instance.getChargingStationId()[i]);
            }
            printWriter.println(";");



            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

/*


        param c{K};			# Capacity of vehicle K
        param Q;			# Effective battery capacity
        param Binit{K};		# Initial battery capacity of vehicle K
        param r;			# Final minimum battery level ratio
        param beta{V,V};	# Battery consumption between nodes i, j in V
        param alpha{S};		# Recharge rate at charging facility S
        param Tp;			# Planning horizon
        */
