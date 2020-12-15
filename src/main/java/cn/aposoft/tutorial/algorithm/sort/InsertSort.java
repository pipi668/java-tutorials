package cn.aposoft.tutorial.algorithm.sort;

import java.util.Arrays;
import java.util.List;

public class InsertSort {

    public static void main(String[] args) {

        Integer[] v = {5, 4, 8, 3, 10, 3, 11, 20, 15, 1};
        List<Integer> vl = Arrays.asList(v);
        sort(v);
        System.out.println(Arrays.toString(v));
    }



    public static void sort(Integer[] v) {
        int n = v.length;
        for (int i = 1; i < n; i++) {
            if (v[i] < v[i - 1]) {               //若第i个元素大于i-1元素，直接插入。小于的话，移动有序表后插入
                int j = i - 1;
                int x = v[i];        //复制为哨兵，即存储待排序元素
                v[i] = v[i - 1];           //先后移一个元素
                //查找在有序表的插入位置
                while (j>=0 && x < v[j]) {
                    v[j + 1] = v[j];
                    j--;         //元素后移
                }
                v[j + 1] = x;      //插入到正确位置
            }
            System.out.println(Arrays.toString(v));
        }
    }

    private static void swap(Integer[] v, int i, int j) {
        int tempV = v[i];
        v[i] = v[j];
        v[j] = tempV;
    }

}
