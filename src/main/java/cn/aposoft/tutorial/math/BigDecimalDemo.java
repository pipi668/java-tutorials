/**
 * 
 */
package cn.aposoft.tutorial.math;

import java.math.BigDecimal;

/**
 * <p>
 * BigDecimalDemo.java
 * </p>
 * <p>
 * TODO
 * </p>
 * <p>
 * 国美小额贷款有限公司
 * </p>
 * <p>
 * author LiuJian
 * </p>
 * <p>
 * version 2016年8月24日 下午7:43:36
 * <p>
 * 
 */
public class BigDecimalDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {

        BigDecimal prin = new BigDecimal(3000);
        BigDecimal dst = new BigDecimal("3000.00");

        System.out.println(prin.compareTo(dst));
        System.out.println(prin.compareTo(dst) == 0);
    }

}
