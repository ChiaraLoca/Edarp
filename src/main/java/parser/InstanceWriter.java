package parser;

import model.Instance;
import model.Node;

import java.io.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class InstanceWriter {

    private  PrintWriter printWriter;
    private  Instance instance;
    private  String destination;
    public InstanceWriter(Instance instance, String destination){
        this.instance = instance;
        this.destination= destination;
        File file = new File(destination+"/"+instance.getTitle()+".dat");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void write()
    {



        writeSingleParam("n",instance.getnCustomers());//n = number of customer*/

        writeSingleParam("numVehicles",instance.getnVehicles());//n = number of stations*/

        int[] res =new int[instance.getCommonOriginDepotId().length+ instance.getArtificialOriginDepotId().length];
        System.arraycopy(instance.getCommonOriginDepotId(),0,res,0,instance.getCommonOriginDepotId().length);
        System.arraycopy(instance.getArtificialOriginDepotId(),0,res,instance.getCommonOriginDepotId().length,instance.getArtificialOriginDepotId().length);
        writeSet("Origin",res);//set Origin;
        // 								# Origin depots
        writeSet("O",instance.getArtificialOriginDepotId());//set Origin;

        writeKeyValueParam("Ok",instance.getArtificialOriginDepotId());
        writeSet("S",instance.getChargingStationId());//set S = Charging stations*/

        int[] res2 =new int[instance.getCommonDestinationDepotId().length+ instance.getArtificialDestinationDepotId().length];
        System.arraycopy(instance.getCommonDestinationDepotId(),0,res2,0,instance.getCommonDestinationDepotId().length);
        System.arraycopy(instance.getArtificialDestinationDepotId(),0,res2,instance.getCommonDestinationDepotId().length,instance.getArtificialDestinationDepotId().length);

        writeSet("F",res2);//set F = All avaible destinations depots*/

        int V = instance.getNodes().size();
        writeMatrixParam("t",instance.getTravelTime(),V,V);// param t{V,V}; 		# Travel time from location V to location V

        double arr[] = new double[instance.getNodes().size()];
        int i=0;
        for(Node n: instance.getNodes())
        {
            arr[i]= n.getArrival();
            i++;
        }
        writeKeyValueParam("arr",arr);//param arr{V}; 		# Earliest time at which service can begin at V*/


        double dep[] = new double[instance.getNodes().size()];
        i=0;
        for(Node n: instance.getNodes())
        {
            dep[i]= n.getDeparture();
            i++;
        }
       writeKeyValueParam("dep",dep);//param dep{V}; 		# Latest time at which service can begin at V*/

        double d[] = new double[instance.getNodes().size()];
        i=0;
        for(Node n: instance.getNodes())
        {
            d[i]= n.getServiceTime();
            i++;
        }
        writeKeyValueParam("d",d);//param d{V};	 		# Service duration at location V*/

        double l[] = new double[instance.getNodes().size()];
        i=0;
        for(Node n: instance.getNodes())
        {
            l[i]= n.getLoad();
            i++;
        }
        writeKeyValueParam("l",l); //param l{N};			# Change in load at location N*/

        writeKeyValueParam("u",instance.getUserMaxRideTime()); ///param u{P};			# Maximum ride-time for customer with pickup at P*/

        writeKeyValueParam("c",instance.getVehicleCapacity());///param c{K};			# Capacity of vehicle K*/

        writeSingleParam("Q",instance.getVehicleBatteryCapacity()[0]); //param Q;			# Effective battery capacity*/

        writeKeyValueParam("Binit",instance.getVehicleInitBatteryInventory());//*param Binit{K};		# Initial battery capacity of vehicle K*/

        writeSingleParam("r",instance.getMinBatteryRatioLvl());//* param r;			# Final minimum battery level ratio*/

        double [][] batteryConsumption = calculateBatteryConsumption();
        writeMatrixParam("beta",batteryConsumption,V,V);//*param beta{V,V};	# Battery consumption between nodes i, j in V*/

        writeKeyValueParam("alpha",instance.getChargingStationId(),instance.getStationRechargingRate());//*param alpha{S};		# Recharge rate at charging facility S*/

        writeSingleParam("Tp",instance.getTimeHorizon()); //*param Tp;			# Planning horizon*/

        writeSingleParam("w1",instance.getWeightFactor()[0]); //*param w1;*/

        writeSingleParam("w2",instance.getWeightFactor()[1]); //*param w2;*/

        double M[][] = new double[V][V];
        double t[][] = instance.getTravelTime();
        for(int k =0;k<V;k++)
        {
            for(int j =0;j<V;j++) {
                M[k][j] = (dep[k]+d[k]+t[k][j] - arr[j])>0 ? (dep[k]+d[k]+t[k][j] - arr[j]) :0;
            }
        }
        writeMatrixParam("M",M,V,V); //#M[i,j] = max{0,dep[i]+d[i]+t[i,j]-arr[j]} --> in file .dat

        int K = instance.getnVehicles();
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
        writeMatrixParam("G",G,K,V);


        close();


    }

    private void close(){
        printWriter.close();
    }

    private void writeSet(String name,double data[])
    {
        printWriter.print("set " + name +" :=");
        for(int i =0;i<data.length;i++)
        {
            printWriter.print(" "+data[i]);
        }
        printWriter.println(";");
    }
    private void writeSet(String name,int data[])
    {
        printWriter.print("set " + name +" :=");
        for(int i =0;i<data.length;i++)
        {
            printWriter.print(" "+data[i]);
        }
        printWriter.println(";");
    }

    private void writeSingleParam(String name,double data)
    {
        printWriter.println("param "+name+" := "+data+";");
    }
    private void writeSingleParam(String name,int data)
    {
        printWriter.println("param "+name+" := "+data+";");
    }

    private void writeMatrixParam(String name,double data[][],int row,int column)
    {
        printWriter.println("param "+name+":");
        for(int i =0;i<column;i++)
        {
            printWriter.print(" "+(i+1));
        }
        printWriter.println(":=");
        for(int k =0;k<row;k++)
        {
            printWriter.print((k+1));

            for(int j =0;j<column;j++)
            {
                printWriter.print(" "+data[k][j]);
            }
            printWriter.println();
        }
        printWriter.println(";");

    }

    private void writeMatrixParam(String name,int data[][],int row,int column)
    {
        printWriter.println("param "+name+":");
        for(int i =0;i<column;i++)
        {
            printWriter.print(" "+(i+1));
        }
        printWriter.println(":=");
        for(int k =0;k<row;k++)
        {
            printWriter.print((k+1));

            for(int j =0;j<column;j++)
            {
                printWriter.print(" "+data[k][j]);
            }
            printWriter.println();
        }
        printWriter.println(";");

    }

    private void writeKeyValueParam(String name,int[] value)
    {
        printWriter.print("param "+name+" :=");
        for(int i =0;i<value.length;i++)
        {
            printWriter.print(" "+(i+1)+ " "+ value[i]);
        }
        printWriter.println(";");
    }
    private void writeKeyValueParam(String name,double[] value)
    {
        printWriter.print("param "+name+" :=");
        for(int i =0;i<value.length;i++)
        {
            printWriter.print(" "+(i+1)+ " "+ value[i]);
        }
        printWriter.println(";");
    }

    private void writeKeyValueParam(String name,int key[],int[] value)
    {
        printWriter.print("param "+name+" :=");
        for(int i =0;i<value.length;i++)
        {
            printWriter.print(" "+key[i]+ " "+ value[i]);
        }
        printWriter.println(";");
    }

    private void writeKeyValueParam(String name,int key[],double[] value)
    {
        printWriter.print("param "+name+" :=");
        for(int i =0;i<value.length;i++)
        {
            printWriter.print(" "+key[i]+ " "+ value[i]);
        }
        printWriter.println(";");
    }



    public double[][] calculateBatteryConsumption()
    {
        List<Node> nodes = instance.getNodes();
        int size= nodes.size();
        double matrix[][] = new double[size][size];
        double timeTravel[][] = instance.getTravelTime();
        double dischargingRate = instance.getVehicleDischargingRate();

        Node a;
        Node b;
        for(int i=0;i<size;i++)
        {
            a =nodes.get(i);
            for(int j=0;j<size;j++)
            {
                b = nodes.get(j);
                matrix[i][j] = timeTravel[i][j]*dischargingRate;
            }
        }

        return matrix;
    }


}



