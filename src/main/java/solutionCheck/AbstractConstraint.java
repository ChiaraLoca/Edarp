package solutionCheck;

import model.Solution;

import java.util.*;

public abstract class AbstractConstraint {
    protected Solution solution;

    public AbstractConstraint(Solution solution) {
        this.solution=solution;
    }

    abstract boolean check();

    protected static Integer[] arrayUnion(int[] ... param) {
        Set<Integer> set=new HashSet<>();
        for(int[] p:param) {
            set.addAll(Arrays.asList(toInteger(p)));
        }
        Integer[] unionArray = set.toArray(new Integer[set.size()]);
        Arrays.sort(unionArray);
        return unionArray;
    }

    protected static Integer[] arrayDiff(int[] ... param) {
        List<Integer> list=new ArrayList<>();
        for(int[] p:param) {
            list.addAll(Arrays.asList(toInteger(p)));
        }
        List<Integer> currencyList=new ArrayList<>();
        for(Integer i:list) {
            int occ=Collections.frequency(list,i);
            if(occ==1)
                currencyList.add(i);
        }
        return currencyList.toArray(new Integer[currencyList.size()]);
    }

    private static Integer[] toInteger(int[] a) {
        Integer[] arr=new Integer[a.length];
        for(int i=0; i<a.length;i++) {
            arr[i]=a[i];
        }
        return arr;
    }

}
