package parser;

import model.Instance;
import model.Node;
import model.NodeType;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InstanceReader {
    private static InstanceReader instanceReader=null;
    private InstanceReader(){ }
    public static InstanceReader getInstanceReader(){
        if (instanceReader==null)
            instanceReader=new InstanceReader();
        return instanceReader;
    }

    public Instance read(File file,boolean implementChanges) throws ParseException, FileNotFoundException {

        String title = file.getName();
        String name = "" + title.toCharArray()[0];
        title = title.substring(0, title.length() - 4);
        if (!file.exists())
            throw new FileNotFoundException();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int i = 1;
            List<String[]> lines = bufferedReader.lines().collect(() -> new ArrayList<String[]>(),
                    (array, s) -> {
                        array.add(s.split("\\s+"));
                    },
                    ArrayList::addAll);
            if (lines.get(0).length != 7)
                throw new ParseException("First line not Parsable", i);

            //TODO time-service 15 minuti
            int nVehicles = Integer.parseInt(lines.get(0)[0]);
            int nCustomers = Integer.parseInt(lines.get(0)[1]);
            int nOriginDepots = Integer.parseInt(lines.get(0)[2]);
            int nDestinationDepots = Integer.parseInt(lines.get(0)[3]);
            int nStations = Integer.parseInt(lines.get(0)[4]);
            int timeHorizon = Integer.parseInt(lines.get(0)[6]);


            Instance instance = new Instance(title, name, nVehicles, nCustomers, nOriginDepots, nDestinationDepots, nStations, Integer.parseInt(lines.get(0)[5]));
            int nodeIndex = 1;
            List<Node> nodes = new ArrayList<>();
            int arrayPositionModifier = 0;

            while ((lines.get(i).length == 7 && Integer.parseInt(lines.get(i)[0]) == nodeIndex) || (lines.get(i).length == 8 && lines.get(i)[0].equals("") && Integer.parseInt(lines.get(i)[1]) == nodeIndex)) {
                if (lines.get(i).length == 7) {
                    arrayPositionModifier = 0;

                } else if (lines.get(i).length == 8) {
                    arrayPositionModifier = 1;
                } else throw new ParseException("Invalid node line length", i);
                nodes.add(new Node(Integer.parseInt(lines.get(i)[0 + arrayPositionModifier]), Double.parseDouble(lines.get(i)[1 + arrayPositionModifier]), Double.parseDouble(lines.get(i)[2 + arrayPositionModifier]), Double.parseDouble(lines.get(i)[3 + arrayPositionModifier]),
                        Double.parseDouble(lines.get(i)[4 + arrayPositionModifier]), Double.parseDouble(lines.get(i)[5 + arrayPositionModifier]), Double.parseDouble(lines.get(i)[6 + arrayPositionModifier])));
                i++;
                nodeIndex++;
            }

            instance.setNodes(nodes);
            int indexP=0;
            int indexD=0;
            for(Node n:nodes) {

                if(n.getLoad()!=0) {
                    instance.getPickupAndDropoffLocations().add(n);
                    if(n.getLoad()>0) {
                        instance.getPickupLocationsId()[indexP] = n.getId();
                        indexP++;
                    }
                    else if(n.getLoad()<0) {
                        instance.getDropoffLocationsId()[indexD] = n.getId();
                        indexD++;
                    }
                }

            }
            /**commonOriginDepotId*/

            if(lines.get(i).length==nOriginDepots)
                arrayPositionModifier=0;
            else if(lines.get(i).length==nOriginDepots+1 && lines.get(i)[0].equals(""))
                arrayPositionModifier=1;
            else
                throw new ParseException("Common origin depot id", i);
            for(int k=0;k<nOriginDepots;k++)
            {
                instance.getCommonOriginDepotId()[k]= Integer.parseInt(lines.get(i)[k+arrayPositionModifier]);
            }i++;

            /**commonDestinationDepotId*/

                if(lines.get(i).length==nDestinationDepots)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nDestinationDepots+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Common destination depot id", i);
            for(int k=0;k<nDestinationDepots;k++)
            {
                instance.getCommonDestinationDepotId()[k]= Integer.parseInt(lines.get(i)[k+arrayPositionModifier]);
            }i++;

            /**artificialOriginDepotId*/
                if(lines.get(i).length==nVehicles)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nVehicles+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Artificial origin depot id", i);
            for(int k=0;k<nVehicles;k++)
            {
                instance.getArtificialOriginDepotId()[k]= Integer.parseInt(lines.get(i)[k+arrayPositionModifier]);
            }i++;

            /**artificialDestinationDepotId*/

            if(instance.getName().equals("a"))
            {
                if(lines.get(i).length==nVehicles)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nVehicles+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Artificial destination depot id", i);
                for(int k=0;k<nVehicles;k++)
                {
                    instance.getArtificialDestinationDepotId()[k]= Integer.parseInt(lines.get(i)[k+arrayPositionModifier]);
                }i++;
            }
            else
            {
                if(lines.get(i).length==5)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==6 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Artificial destination depot id", i);
                for(int k=0;k<5;k++)
                {
                    instance.getArtificialDestinationDepotId()[k]= Integer.parseInt(lines.get(i)[k+arrayPositionModifier]);
                }i++;
            }


            /**chargingStationId*/
                if(lines.get(i).length==nStations)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nStations+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Charging station id", i);
            for(int k=0;k<nStations;k++)
            {
                int id= Integer.parseInt(lines.get(i)[k+arrayPositionModifier]);
                instance.getChargingStationId()[k]= id;
                instance.getChargingStationNodes().add(nodes.get(id-1));
            }i++;

            /**userMaxRideTime*/
                if(lines.get(i).length==nCustomers)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nCustomers+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("User max ride time", i);

            for(int k=0;k<nCustomers;k++)
            {
                if(implementChanges)
                    instance.getUserMaxRideTime()[k]= 8;
                else
                    instance.getUserMaxRideTime()[k]= Integer.parseInt(lines.get(i)[k+arrayPositionModifier]);
            }i++;

            /**vehicleCapacity*/
                if(lines.get(i).length==nVehicles)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nVehicles+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Vehicle capacity", i);
            for(int k=0;k<nVehicles;k++)
            {
                instance.getVehicleCapacity()[k]= Integer.parseInt(lines.get(i)[k+arrayPositionModifier]);
            }i++;

            /**vehicleInitBatteryInventory*/
            if(lines.get(i).length==nVehicles)
                arrayPositionModifier=0;
            else if(lines.get(i).length==nVehicles+1 && lines.get(i)[0].equals(""))
                arrayPositionModifier=1;
            else
                throw new ParseException("Vehicle initial battery inventory", i);

            for(int k=0;k<nVehicles;k++)
            {
                if(implementChanges)
                    instance.getVehicleInitBatteryInventory()[k]=3.5;
                else
                    instance.getVehicleInitBatteryInventory()[k]= Double.parseDouble(lines.get(i)[k+arrayPositionModifier]);
            } i++;

            /**vehicleBatteryCapacity*/
                if(lines.get(i).length==nVehicles)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nVehicles+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Vehicle battery capacity", i);

                for(int k=0;k<nVehicles;k++)
                {
                    if(implementChanges)
                        instance.getVehicleBatteryCapacity()[k]= 3.5;
                    else
                        instance.getVehicleBatteryCapacity()[k]= Double.parseDouble(lines.get(i)[k+arrayPositionModifier]);
                }i++;

            /**minEndBatteryRatioLvl*/
                if(lines.get(i).length==nVehicles)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nVehicles+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Minimum final battery ratio level", i);

            for(int k=0;k<nVehicles;k++)
            {
                instance.getMinEndBatteryRatioLvl()[k]= Double.parseDouble(lines.get(i)[k+arrayPositionModifier]);
            }i++;

            /**stationRechargingRate*/
                if(lines.get(i).length==nStations)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==nStations+1 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Station recharging rate", i);
            for(int k=0;k<nStations;k++)
            {
                instance.getStationRechargingRate()[k]= Double.parseDouble(lines.get(i)[k+arrayPositionModifier]);
            }i++;

            /**vehicleDischargingRate*/
            if(lines.get(i).length==1)
                arrayPositionModifier=0;
            else if(lines.get(i).length==1+1 && lines.get(i)[0].equals(""))
                arrayPositionModifier=1;
            else
                throw new ParseException("Station discharging rate", i);
            double value =  Double.parseDouble(lines.get(i)[0+arrayPositionModifier]);

            if(implementChanges)
                instance.setVehicleDischargingRate(value+(value*0.3));
            else
                instance.setVehicleDischargingRate(value);
            i++;

            /**weightFactor*/
                if(lines.get(i).length==2)
                    arrayPositionModifier=0;
                else if(lines.get(i).length==3 && lines.get(i)[0].equals(""))
                    arrayPositionModifier=1;
                else
                    throw new ParseException("Weight factor", i);

            for(int k=0;k<2;k++)
            {
                instance.getWeightFactor()[k]= Double.parseDouble(lines.get(i)[k+arrayPositionModifier]);
            }i++;


            if (name.equals("u")) {
                //TimeDistance Matrix
                double[][] timeDistance = new double[nodes.size()][nodes.size()];
                for (int row = 0; row < nodes.size(); row++) {
                    String[] line = lines.get(i);
                    for (int col = 0; col < nodes.size(); col++) {
                        if(implementChanges)
                            timeDistance[row][col] = Double.parseDouble(line[col])*2;
                        else
                            timeDistance[row][col] = Double.parseDouble(line[col]);
                    }
                    i++;
                }
                instance.setTravelTime(timeDistance);

            } else {
                instance.setTravelTime(calculateTravelTime(1, instance,implementChanges));
            }

            /**time horizon*/
            if(!implementChanges)
                instance.setTimeHorizon(timeHorizon);
            else{
                double th;
                Node node= instance.getNodes().get(instance.getnStations());//node = dropoff station with last start time service;
                for(Node n: instance.getNodes())
                {
                    if(n.getLoad()==-1){
                       if(n.getArrival()>node.getArrival())
                           node = n;
                    }
                }
                th = node.getArrival();
                double distance=0;//travel time to the furthest charging station
                double [][] travelTime = instance.getTravelTime();
                for(int station :  instance.getChargingStationId())
                {
                    if(travelTime[station-1][node.getId()-1]>distance)
                        distance =  travelTime[station-1][node.getId()-1];
                }
                th+=distance;
                instance.setTimeHorizon(th);
            }


            for(Node n : instance.getNodes()){
                if(n.getLoad()>0)
                    n.setNodeType(NodeType.PICKUP);
                else if(n.getLoad()<0)
                    n.setNodeType(NodeType.DROPOFF);
                else
                {
                    for(int k=0;k<instance.getChargingStationId().length;k++)
                    {
                        if(instance.getChargingStationId()[k]==n.getId())
                            n.setNodeType(NodeType.CHARGE);
                    }

                }
            }
            instance.setM(calculateM(nodes.size(),instance));
            instance.setG(calculateG(instance));

            instance.setBatteryConsumption(calculateBatteryConsumption(instance));
            if (lines.size() == i){
                return instance;
            }
            throw new ParseException("Time distance matrix", i);

            //instance.setNodes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double[][] calculateTravelTime(int speed, Instance instance,boolean implementChanges) {
        List<Node> nodes = instance.getNodes();
        int size = nodes.size();
        double matrix[][] = new double[size][size];

        Node a;
        Node b;
        for (int i = 0; i < size; i++) {
            a = nodes.get(i);
            for (int j = 0; j < size; j++) {
                b = nodes.get(j);
                if(implementChanges)
                    matrix[i][j] = (Math.sqrt(Math.pow(a.getLat() - b.getLat(), 2) + Math.pow(a.getLon() - b.getLon(), 2)) / speed)*2;
                else
                    matrix[i][j] = Math.sqrt(Math.pow(a.getLat() - b.getLat(), 2) + Math.pow(a.getLon() - b.getLon(), 2)) / speed;
            }
        }
        return matrix;
    }

    public double[][] calculateBatteryConsumption(Instance instance)
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

    public double[][] calculateM(int V,Instance instance){
        double arr[] = new double[instance.getNodes().size()];
        int i =0;
        for(Node n: instance.getNodes())
        {
            arr[i]= n.getArrival();
            i++;
        }
        double dep[] = new double[instance.getNodes().size()];  
        i =0;
        for(Node n: instance.getNodes())
        {
            dep[i]= n.getDeparture();
            i++;
        }
        double d[] = new double[instance.getNodes().size()];
        i=0;
        for(Node n: instance.getNodes())
        {
            d[i]= n.getServiceTime();
            i++;
        }




        double M[][] = new double[V][V];
        for(int k =0;k<V;k++)
        {
            for(int j =0;j<V;j++) {
                M[k][j] = (dep[k]+d[k]+instance.getTravelTime()[k][j] - arr[j])>0 ? (dep[k]+d[k]+instance.getTravelTime()[k][j] - arr[j]) :0;
            }
        }
        return M;
    }
    public double[][] calculateG(Instance instance){
        double G[][] = new double[instance.getnVehicles()][instance.getNodes().size()];
        for(int k =0;k<instance.getnVehicles();k++)
        {
            for(int j = 0 ; j<instance.getNodes().size() ;j++)
            {
                if(instance.getVehicleCapacity()[k] < instance.getVehicleCapacity()[k] + instance.getNodes().get(j).getLoad())
                {
                    G[k][j] = instance.getVehicleCapacity()[k];
                }
                else {
                    G[k][j] = instance.getVehicleCapacity()[k] + instance.getNodes().get(j).getLoad();
                }
            }
        }
        return G;

    }

}
