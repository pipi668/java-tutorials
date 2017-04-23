/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.lang.prim;

/**
 * @author LiuJian
 * @date 2017年4月20日
 * 
 */
public class ByteOverflow {

    /**
     * @param args
     */
    public static void main(String[] args) {
        byte a = (byte) 255;
        System.out.println(a);
        byte b = (byte) 0b10000000;
        System.out.println(b);
        System.out.println(0xFF & b);
        System.out.println(0xFF & a);
        System.out.println(0xFF & a);
        byte c = 127;
    }

}
