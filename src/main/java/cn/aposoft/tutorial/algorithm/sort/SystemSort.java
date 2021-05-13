package cn.aposoft.tutorial.algorithm.sort;

import java.util.Arrays;
import java.util.Random;

public class SystemSort {

    public static void main(String[] args) {
        Random r = new Random();
        int[] arr = new int[1000 * 1000 * 10];

        for (int j = 0; j < 10; j++) {

            for (int i = 0; i < arr.length; i++) {
                arr[i] = r.nextInt();
            }
            System.out.println("arr.length:" + arr.length);
            long begin = System.currentTimeMillis();
            Arrays.sort(arr);
            long end = System.currentTimeMillis();
            System.out.println("time:" + (end - begin));
            /*
                time:969
                time:782
                time:785
                time:846
                time:833
                time:838
                time:850
                time:824
                time:837
                time:844
             */
        }
    }


}
