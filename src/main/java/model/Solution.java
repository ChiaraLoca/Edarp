package model;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private Instance instance;
    private List<Mission> missions;

    public Solution(Instance instance) {
        this.instance = instance;
        this.missions= new ArrayList<>();
    }

    public List<Mission> getMissions() {
        return missions;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("SOLUZIONE: "+instance.getTitle()+"\n");
        for(Mission m:missions)
        {
            str.append(m.toString());
        }
        return str.toString();
    }
}
