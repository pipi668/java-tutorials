package cn.aposoft.tutorial.algorithm.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface CollectionsSort {

    public static void main(String[] args) {
        Integer[] v = {5, 4, 8, 3, 10, 3, 11, 20, 15, 1};
        List<Integer> vl = Arrays.asList(v);
        System.out.println(vl);
        Collections.sort(vl);
        System.out.println(vl);
        Arrays.sort(v);
        System.out.println(Arrays.toString(v));
    }
}
