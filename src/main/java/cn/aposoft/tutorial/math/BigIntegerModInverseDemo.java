/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.math;

import java.math.BigInteger;

/**
 * @author LiuJian
 * @date 2017年4月21日
 * 
 */
public class BigIntegerModInverseDemo {

    public static void main(String[] args) {
        fixModInverse(9);
    }

    public static void fixModInverse(int m) {
        BigInteger bm = BigInteger.valueOf(m);

        for (int i = 2; i < m; i++) {
            if (!bm.mod(BigInteger.valueOf(i)).equals(BigInteger.ZERO) && !(bm.gcd(BigInteger.valueOf(i)).compareTo(BigInteger.ONE) > 0))
                System.out.println(i + " mod Inverse " + m + " is " + BigInteger.valueOf(i).modInverse(bm));
        }

    }

    /**
     * @param args
     */
    public static void fixValue() {
        BigInteger m = BigInteger.valueOf(19);
        BigInteger e2 = BigInteger.valueOf(2);
        System.out.println(e2 + " mod Inverse" + m + " is " + e2.modInverse(m));
        BigInteger e3 = BigInteger.valueOf(3);

        System.out.println(e3 + " mod Inverse" + m + " is " + e3.modInverse(m));

        BigInteger e4 = BigInteger.valueOf(4);
        System.out.println(e4 + " mod Inverse" + m + " is " + e4.modInverse(m));
        BigInteger e5 = BigInteger.valueOf(5);
        System.out.println(e5 + " mod Inverse" + m + " is " + e5.modInverse(m));
        BigInteger e6 = BigInteger.valueOf(6);
        System.out.println(e6 + " mod Inverse" + m + " is " + e6.modInverse(m));
        BigInteger e7 = BigInteger.valueOf(7);
        System.out.println(e7 + " mod Inverse" + m + " is " + e7.modInverse(m));
        BigInteger e8 = BigInteger.valueOf(8);
        System.out.println(e8 + " mod Inverse" + m + " is " + e8.modInverse(m));
        BigInteger e9 = BigInteger.valueOf(9);
        System.out.println(e9 + " mod Inverse" + m + " is " + e9.modInverse(m));
        BigInteger e10 = BigInteger.valueOf(10);
        System.out.println(e10 + " mod Inverse" + m + " is " + e10.modInverse(m));
        BigInteger e11 = BigInteger.valueOf(11);
        System.out.println(e11 + " mod Inverse" + m + " is " + e11.modInverse(m));

        BigInteger e12 = BigInteger.valueOf(12);
        System.out.println(e12 + " mod Inverse" + m + " is " + e12.modInverse(m));

        BigInteger e13 = BigInteger.valueOf(13);
        System.out.println(e13 + " mod Inverse" + m + " is " + e13.modInverse(m));

        BigInteger e14 = BigInteger.valueOf(14);
        System.out.println(e14 + " mod Inverse" + m + " is " + e14.modInverse(m));

        BigInteger e15 = BigInteger.valueOf(15);
        System.out.println(e15 + " mod Inverse" + m + " is " + e15.modInverse(m));

        BigInteger e16 = BigInteger.valueOf(16);
        System.out.println(e16 + " mod Inverse" + m + " is " + e16.modInverse(m));
    }

}
