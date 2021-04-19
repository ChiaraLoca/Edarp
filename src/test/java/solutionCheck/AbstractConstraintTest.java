package solutionCheck;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AbstractConstraintTest {
    @Test
    void arrayUnionTest(){
        int[] intArray = new int[]{ 1,2,3,4,5,6,7,8,9,10 };
        int[] intArr2 = new int[]{ 2,3,5,6,7,8,9 };
        int[] intArr3 = new int[]{ 1,2,3,7,8,10,11};
        int[] intArr4 = new int[]{ 1,2,3,7,8,10,56,87,99};

        int[] solution = new int[]{ 1,2,3,4,5,6,7,8,9,10,11,56,87,99};


        Integer[] union=AbstractConstraint.arrayUnion(intArray,intArr2,intArr3,intArr4);
        for(int i=0; i<solution.length;i++) {
            assertEquals(solution[i],union[i]);
        }
    }

    @Test
    void arrayDiffTest(){
        int[] intArray = new int[]{ 1,2,3,4,5,6,7,8,9,10 };
        int[] intArr2 = new int[]{ 2,3,5,6,7,8,9 };
        int[] intArr3 = new int[]{ 1,2,3,7,8,10,11};
        int[] intArr4 = new int[]{ 1,2,3,7,8,10,56,87,99};

        int[] solution = new int[]{ 4,11,56,87,99};


        Integer[] union=AbstractConstraint.arrayDiff(intArray,intArr2,intArr3,intArr4);
        for(int i=0; i<solution.length;i++) {
            assertEquals(solution[i],union[i]);
        }
    }
}
