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
        String title = file.getName();
        String name = "" + title.toCharArray()[0];
        title = title.substring(0, title.length() - 4);
        if (!file.exists())
            throw new FileNotFoundException();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int i = 1;
            List<String[]> lines = bufferedReader.lines().collect(ArrayList::new,
                    (array, s) -> {
                        array.add(s.split("\\s+"));
                    },
                    ArrayList::addAll);

            for (String[] s:lines) {
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

