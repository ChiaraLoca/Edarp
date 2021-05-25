package parser;

import model.Instance;
import model.Solution;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelReader {
    private static ModelReader modelReader = null;

    private ModelReader() {
    }

    public static ModelReader getModelReader() {
        if (modelReader == null)
            modelReader = new ModelReader();
        return modelReader;
    }

    public Solution read(File file, Instance instance) throws ParseException, FileNotFoundException {
        Solution solution=new Solution(instance); 

        String title = file.getName();
        String name = "" + title.toCharArray()[0];
        title = title.substring(0, title.length() - 4);
        List<String[]> lines=null;
        if (!file.exists())
            throw new FileNotFoundException();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int i = 1;
            lines = bufferedReader.lines().collect(ArrayList::new,
                    (array, s) -> {
                        array.add(s.split("\\s+"));
                    },
                    ArrayList::addAll);
        } catch (IOException e) {
            e.printStackTrace();
        }

        char currentVariable=' ';
        int vehicleNum=0;
        int rowCount=0;
        for (String[] s:lines) {
            if(s[0].equals(""))
                continue;

            switch(s[0].charAt(0)) {
                case 'X':
                    currentVariable='X';
                    vehicleNum=Integer.parseInt(String.valueOf(s[0].charAt(2))); // change if number of vehicles > 5
                    rowCount=0;
                    break;
                case 'T':
                    currentVariable='T';
                    rowCount=0;
                    break;
                case 'L':
                    currentVariable='L';
                    rowCount=0;
                    break;
                case 'B':
                    currentVariable='B';
                    rowCount=0;
                    break;
                case 'E':
                    currentVariable='E';
                    rowCount=0;
                    break;
                case 'R':
                    currentVariable='R';
                    rowCount=0;
                    break;
                default:
                    printToMatrix(s, currentVariable, vehicleNum, solution, rowCount++);
            }
        }

        return solution;
    }

    void printToMatrix(String[] s, char currentVariable, int vehicleNum, Solution solution, int rowCount) {
        String[] stringValues=s[0].split(",");
        List<Double> values=new ArrayList<>();
        for(String ss:stringValues) {
            values.add(Double.parseDouble(ss));
        }
        switch(currentVariable) {
            case 'X':
                List<Integer> intValues=new ArrayList<>();
                for(String ss:stringValues) {
                    intValues.add(Integer.parseInt(ss));
                }
                for(int i=0; i<intValues.size(); i++) {
                    solution.vehicleSeqStopAtLocations[vehicleNum-1][rowCount][i]=intValues.get(i);
                }
                break;
            case 'T':
                for(int i=0; i<values.size(); i++) {
                    solution.timeVehicleStartsAtLocation[rowCount][i]=values.get(i);
                }
                break;
            case 'L':
                for(int i=0; i<values.size(); i++) {
                    solution.loadOfVehicleAtLocation[rowCount][i]=values.get(i);
                }
                break;
            case 'B':
                for(int i=0; i<values.size(); i++) {
                    solution.batteryLoadOfVehicleAtLocation[rowCount][i]=values.get(i);
                }
                break;
            case 'E':
                for(int i=0; i<values.size(); i++) {
                    solution.chargingTimeOfVehicleAtStation[rowCount][i]=values.get(i);
                }
                break;
            case 'R':
                for(int i=0; i<values.size(); i++) {
                    solution.excessRideTimeOfPassenger[rowCount]=values.get(i);
                }
                break;
        }
    }
}

