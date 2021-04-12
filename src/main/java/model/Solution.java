package model;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private Instance instance;
    private List<Mission> list;

    public Solution(Instance instance) {
        this.instance = instance;
        this.list= new ArrayList<>();
    }

    public List<Mission> getList() {
        return list;
    }

}
