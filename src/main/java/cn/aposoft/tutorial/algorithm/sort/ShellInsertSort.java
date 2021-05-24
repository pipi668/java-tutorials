package cn.aposoft.tutorial.algorithm.sort;


import java.util.Arrays;

public class ShellInsertSort {
    public static void main(String[] args) {

        Integer[] v = {5, 4, 8, 10, 3, 11, 20, 15, 1};
        System.out.println(Arrays.toString(v));
        ShellSort(v);
        System.out.println(Arrays.toString(v));
    }

    /**
     * 先按增量d（n/2,n为要排序数的个数进行希尔排序
     */
    static void ShellSort(Integer v[]) {
        int factor = 2;
        int n = v.length;
        int dk = n / factor;
        while (dk >= 1) {

            ShellInsertSort(v, n, dk);
            System.out.println(dk + ":" + Arrays.toString(v));
            dk /= factor;

        }
    }

    /**
     * 直接插入排序的一般形式
     *
     * @param  dk int 缩小增量，如果是直接插入排序，dk=1
     */
    static void ShellInsertSort(Integer a[], int n, int dk) {
        for (int i = dk; i < n; ++i) {
            if (a[i] < a[i - dk]) {          //若第i个元素大于i-1元素，直接插入。小于的话，移动有序表后插入
                int j = i - dk;
                int x = a[i];           //复制为哨兵，即存储待排序元素
                a[i] = a[i - dk];         //首先后移一个元素
                while (j >= 0 && x < a[j]) {     //查找在有序表的插入位置
                    a[j + dk] = a[j];
                    j -= dk;             //元素后移
                }
                a[j + dk] = x;            //插入到正确位置
            }
        }

    }


}
