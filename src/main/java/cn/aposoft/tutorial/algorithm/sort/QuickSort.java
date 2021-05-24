package cn.aposoft.tutorial.algorithm.sort;

import java.util.Arrays;

public class QuickSort {


    public static void main(String[] args) {

        Integer[] v = {5, 4, 8, 10, 3, 11, 4, 20, 15, 1};
        System.out.println(Arrays.toString(v));
        quickSprt(v, 0, v.length - 1);
        System.out.println(Arrays.toString(v));
    }

    private static void swap(Integer[] v, int i, int j) {
        int tempV = v[i];
        v[i] = v[j];
        v[j] = tempV;
    }

    public static void quickSprt(Integer[] array, int low, int high) {
        if (low >= high) return;
        System.out.println("low:" + low + ",high:" + high + "," + Arrays.toString(array));
        // only two number
        if (high - low == 1) {

            if (array[low] > array[high]) {
                swap(array, low, high);
            }
        }

        int left = low;
        int right = high;
        int pivot = array[left];//设立基准点

        while (left < right) {
            while (left < right && array[right] > pivot)//从右向左，大数位置不变
                right--;
            array[left] = array[right];//把小数移到左边
            while (left < right && array[left] <= pivot) //从左向右，小数位置不变
                left++;
            array[right] = array[left];//把大数移到右边
        }
        array[left] = pivot;

        quickSprt(array, low, left - 1);
        quickSprt(array, left + 1, high);
    }
}
