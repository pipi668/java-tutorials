/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.crypt.rsa;

import java.math.BigInteger;

/**
 * @author LiuJian
 * @date 2017年4月22日
 * 
 */
public class RsaKnowledge {

    /**
     * RSA算法
     * 
     * @param args
     */
    public static void main(String[] args) {
        // 定义两个质数
        final int p = 19;
        final int q = 17;
        // 计算质数的乘积
        final int n = p * q;
        // 定义 公钥 exponent ,因为3无法计算私钥,所以用5
        final int e = 5;

        final int p_1q_1 = (p - 1) * (q - 1);
        // 计算私钥使用的 exponent
        // modInverse: ( e * d ) mod ((p-1)*(q-1)) = 1;
        //  记作:  e * d = 1 (mod ((p-1)*(q-1)));
        //  可以分解为: 
        
        // 
        // 
        // 
        final int d = BigInteger.valueOf(e).modInverse(BigInteger.valueOf(p_1q_1)).intValue();
        // 定义原始的值
        int ov = 10;
        // 加密
        int ev = BigInteger.valueOf(ov).modPow(BigInteger.valueOf(e), BigInteger.valueOf(n)).intValue();
        // 解密
        int dv = BigInteger.valueOf(ev).modPow(BigInteger.valueOf(d), BigInteger.valueOf(n)).intValue();
        System.out.println("ov:" + ov + ",ev:" + ev + "dv:" + dv);
    }

}
