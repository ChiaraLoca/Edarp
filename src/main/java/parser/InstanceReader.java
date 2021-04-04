package parser;

import model.Instance;
import model.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InstanceReader {
    private static InstanceReader instanceReader=null;
    private InstanceReader(){ }
    public static InstanceReader getInstanceReader(){
        if (instanceReader==null)
            instanceReader=new InstanceReader();
        return instanceReader;
    }
    public Instance read(String path){

        String str[] = path.split("/");
        String title = str[str.length-1];
        String name = "" +title.toCharArray()[0];
        title = title.substring(0,title.length()-4);

        File file=new File(path);
        if(!file.exists())
            return null;
            try (BufferedReader bufferedReader= new BufferedReader(new FileReader(file))){
            List<String[]> lines=bufferedReader.lines().collect(() -> new ArrayList<String[]>(),
                    (array, s)->{
                        array.add(s.split("\\s+"));
                    },
                    ArrayList::addAll);
            if (lines.get(0).length!=7)
                return null;

            int nVehicles = Integer.parseInt(lines.get(0)[0]);
            int nCustomers = Integer.parseInt(lines.get(0)[1]);
            int nOriginDepots =Integer.parseInt(lines.get(0)[2]);
            int nDestinationDepots = Integer.parseInt(lines.get(0)[3]);
            int nStations = Integer.parseInt(lines.get(0)[4]);
            Instance instance= new Instance(title,name,nVehicles, nCustomers,  nOriginDepots,  nDestinationDepots, nStations,  Integer.parseInt(lines.get(0)[5]),  Integer.parseInt(lines.get(0)[6]));
            int i=1;
            int nodeIndex= 1;
            List<Node> nodes=new ArrayList<>();
            int arrayPositionModifier=0;
            int nodeArrayLength=0;
            if(lines.get(i).length==7 && Integer.parseInt(lines.get(i)[0])==nodeIndex){
                arrayPositionModifier=0;
                nodeArrayLength=7;

            } else if(lines.get(i).length==8 && lines.get(i)[0].equals("")){
                arrayPositionModifier=1;
                nodeArrayLength=8;
            }
            else return null;
            while (lines.get(i).length==nodeArrayLength && Integer.parseInt(lines.get(i)[0+arrayPositionModifier])==nodeIndex){

                nodes.add(new Node(Integer.parseInt(lines.get(i)[0+arrayPositionModifier]),  Double.parseDouble(lines.get(i)[1+arrayPositionModifier]),    Double.parseDouble(lines.get(i)[2+arrayPositionModifier]),    Double.parseDouble(lines.get(i)[3+arrayPositionModifier]),
                        Double.parseDouble(lines.get(i)[4+arrayPositionModifier]),  Double.parseDouble(lines.get(i)[5+arrayPositionModifier]),      Double.parseDouble(lines.get(i)[6+arrayPositionModifier])));
                i++;
                nodeIndex++;
            }

            instance.setNodes(nodes);
            /**commonOriginDepotId*/
            if(lines.get(i).length!=nOriginDepots)
                return null;
            for(int k=0;k<nOriginDepots;k++)
            {
                instance.getCommonOriginDepotId()[k]= Integer.parseInt(lines.get(i)[k]);
            }i++;

            /**commonDestinationDepotId*/
            if(lines.get(i).length!=nDestinationDepots)
                return null;
            for(int k=0;k<nDestinationDepots;k++)
            {
                instance.getCommonDestinationDepotId()[k]= Integer.parseInt(lines.get(i)[k]);
            }i++;

            /**artificialOriginDepotId*/
            if(lines.get(i).length!=nVehicles)
                return null;
            for(int k=0;k<nVehicles;k++)
            {
                instance.getArtificialOriginDepotId()[k]= Integer.parseInt(lines.get(i)[k]);
            }i++;

            /**artificialDestinationDepotId*/
            //TODO errore nStation =3, nella linea solo 2 valori,
            //TODO non so quanto sia la dimenzione di sta array

            if(instance.getName().equals("a"))
            {
                if(lines.get(i).length!=nVehicles)
                    return null;
                for(int k=0;k<nVehicles;k++)
                {
                    instance.getArtificialDestinationDepotId()[k]= Integer.parseInt(lines.get(i)[k]);
                }i++;
            }
            else
            {
                if(lines.get(i).length!=5)
                    return null;
                for(int k=0;k<5;k++)
                {
                    instance.getArtificialDestinationDepotId()[k]= Integer.parseInt(lines.get(i)[k]);
                }i++;
            }


            /**chargingStationId*/
            if(lines.get(i).length!=nStations)
                return null;
            for(int k=0;k<nStations;k++)
            {
                instance.getChargingStationId()[k]= Integer.parseInt(lines.get(i)[k]);
            }i++;

            /**userMaxRideTime*/
            if(lines.get(i).length!=nCustomers)
                return null;
            for(int k=0;k<nCustomers;k++)
            {
                instance.getUserMaxRideTime()[k]= Integer.parseInt(lines.get(i)[k]);
            }i++;

            /**vehicleCapacity*/
            if(lines.get(i).length!=nVehicles)
                return null;
            for(int k=0;k<nVehicles;k++)
            {
                instance.getVehicleCapacity()[k]= Integer.parseInt(lines.get(i)[k]);
            }i++;

            /**vehicleInitBatteryInventory*/
            if(lines.get(i).length!=nVehicles)
                return null;
            for(int k=0;k<nVehicles;k++)
            {
                instance.getVehicleInitBatteryInventory()[k]= Double.parseDouble(lines.get(i)[k]);
            }i++;

            /**vehicleBatteryCapacity*/
            if(lines.get(i).length!=nVehicles)
                return null;
            for(int k=0;k<nVehicles;k++)
            {
                instance.getVehicleBatteryCapacity()[k]= Double.parseDouble(lines.get(i)[k]);
            }i++;

            /**minEndBatteryRatioLvl*/
            if(lines.get(i).length!=nVehicles)
                return null;
            for(int k=0;k<nVehicles;k++)
            {
                instance.getMinEndBatteryRatioLvl()[k]= Double.parseDouble(lines.get(i)[k]);
            }i++;

            /**stationRechargingRate*/
            if(lines.get(i).length!=nStations)
                return null;
            for(int k=0;k<nStations;k++)
            {
                instance.getStationRechargingRate()[k]= Double.parseDouble(lines.get(i)[k]);
            }i++;

            /**vehicleDischargingRate*/
            if(lines.get(i).length!=1)
                return null;
            instance.setVehicleDischargingRate(Double.parseDouble(lines.get(i)[0]));
            i++;

            /**weightFactor*/
            if(lines.get(i).length!=2)
                return null;
            for(int k=0;k<2;k++)
            {
                instance.getWeightFactor()[k]= Double.parseDouble(lines.get(i)[k]);
            }i++;


            if(name.equals("u")) {
                //TimeDistance Matrix
                double[][] timeDistance = new double[nodes.size()][nodes.size()];
                for (int row = 0; row < nodes.size(); row++) {
                    String[] line = lines.get(i);
                    for (int col = 0; col < nodes.size(); col++) {
                        timeDistance[row][col] = Double.parseDouble(line[col]);
                    }
                    i++;
                }
                instance.setTravelTime(timeDistance);

            }
            else
            {
                instance.setTravelTime(calculateTravelTime(1,instance));
            }

            if(lines.size() == i)
                return instance;
            return null;

            //instance.setNodes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double[][] calculateTravelTime(int speed,Instance instance)
    {
        List<Node> nodes = instance.getNodes();
        int size= nodes.size();
        double matrix[][] = new double[size][size];

        Node a;
        Node b;
        for(int i=0;i<size;i++)
        {
            a =nodes.get(i);
            for(int j=0;j<size;j++)
            {
                b = nodes.get(j);
                matrix[i][j] = Math.sqrt(Math.pow(a.getLat()-b.getLat(),2)+Math.pow(a.getLon()-b.getLon(),2))/speed;
            }
        }
        return matrix;
    }
}
