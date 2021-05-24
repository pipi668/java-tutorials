package cn.aposoft.tutorial.algorithm.sort;

import java.util.Arrays;

public class SelectSort {

    public static void main(String[] args) {

        Integer[] v = {5, 4, 8, 10, 3, 11, 20, 15, 1};
        System.out.println(Arrays.toString(v));
        SelectSort(v);
        System.out.println(Arrays.toString(v));
    }


    public static void SelectSort(Integer[] array) {
        int len = array.length;
        for (int i = 0; i < len; i++) {//确定每次开始的位置
            int min = array[i];//设定开始数字为最小的值最小值
            int flag = i;
            for (int j = i + 1; j < len; j++) {//把最小值存放到min,从开始数字向后一个个和min比较，再把最小值存放到min
                if (min > array[j]) {
                    min = array[j];
                    flag = j;
                }
            }
            if (flag != i) {
                array[flag] = array[i];
                array[i] = min;
            }
        }
    }
}
