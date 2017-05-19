/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.crypt.discretelogarithm;

/**
 * 模运算公式
 * <pre>
 *   k为常数.
     a = b + kn 记作:,如果a = b + kn,其中k为常数,则 a = b ( mod n);
             如果 k=1, 则 a = b
             如果 b=1, 则a = 1 ( mod n);
    
 * </pre> 
 *   <pre>
        (a+b) mod n = ((a mod n) + (b mod n)) mod n
        (a-b) mod n = ((a mod n) - (b mod n) ) mod n
        (a*b) mod n = ((a mod n) * (b mod n)) mod n
        (a*(b+c)) mod n = (((a*b) mod n ) + ((a*c) mod n )) mod n
     </pre>
 * @author LiuJian
 * @date 2017年5月2日
 * 
 */
public class ModOperation {

    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        
    }

}
