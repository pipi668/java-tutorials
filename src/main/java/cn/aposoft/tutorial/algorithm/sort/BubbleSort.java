package cn.aposoft.tutorial.algorithm.sort;

import java.util.Arrays;

/**
 *
 */
public class BubbleSort {


    public static void main(String[] args) {

        Integer[] v = {5, 4, 8, 10, 3, 11, 4, 20, 15, 1};
        System.out.println(Arrays.toString(v));
        bubbleSort(v);
        System.out.println(Arrays.toString(v));
    }

    private static void swap(Integer[] v, int i, int j) {
        int tempV = v[i];
        v[i] = v[j];
        v[j] = tempV;
    }

    public static void bubbleSort(Integer[] array) {
        for (int i = 0; i < array.length; i++) {
            //第i冒泡,一次冒泡，会确定一个最大值
            for (int j = 0; j < array.length - i - 1; j++) {
                //从头一直到已经确定的位置前，两两比较
                if (array[j] > array[j + 1]) {
                    swap(array, j, j + 1);
                }
            }
        }
    }
}
