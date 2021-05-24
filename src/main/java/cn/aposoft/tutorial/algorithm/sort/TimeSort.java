package cn.aposoft.tutorial.algorithm.sort;

import java.util.Arrays;

public class TimeSort {
    public static void main(String[] args) {
        Integer[] v = {5, 4, 8, 10, 3, 11, 4, 20, 15, 1};
        System.out.println(Arrays.toString(v));
        Arrays.sort(v);// use tim-sort
        System.out.println(Arrays.toString(v));
    }
}
