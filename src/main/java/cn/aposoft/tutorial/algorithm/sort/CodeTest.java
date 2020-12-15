package cn.aposoft.tutorial.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CodeTest {
    public static void main(String[] args) {

        Integer[] v = {5, 4, 8, 10, 3, 11, 20, 15, 1};
        List<Integer> vl = Arrays.asList(v);
        sort(v);
        System.out.println("Hello World!");
        System.out.println(Arrays.toString(v));

        sort(vl);
        System.out.println(vl);

    }

    public static void sort(Integer[] v) {
        int length = v.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                if (v[i] < v[j]) {
                    swap(v, i, j);
                    if (i == length - 1) {
                        System.out.println(  "last line:" + Arrays.toString(v));
                    }
                }
            }
            System.out.println(i + ":" + Arrays.toString(v));
        }
    }

    private static void swap(Integer[] v, int i, int j) {
        int tempV = v[i];
        v[i] = v[j];
        v[j] = tempV;
    }

    private static void swap(List v, int i, int j) {
        Object tempV = v.get(i);
        v.set(i, v.get(j));
        v.set(j, tempV);
    }

    public static void sort(List<? extends Comparable> v) {
        int length = v.size();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                Comparable vt = v.get(i);
                if (vt.compareTo(v.get(j)) < 0) {
                    swap(v, i, j);
                }
            }
        }
    }

}
