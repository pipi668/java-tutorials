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
public class BigIntegerModPowDemo {

    /**
     * mod pow
     * 
     * @param args
     */
    public static void main(String[] args) {
        // 3 e 5 mod 7
        System.out.println(BigInteger.valueOf(3).modPow(BigInteger.valueOf(5), BigInteger.valueOf(7)));
        ;
        System.out.println(((int) Math.pow(3, 5)) % 7);
    }

}
