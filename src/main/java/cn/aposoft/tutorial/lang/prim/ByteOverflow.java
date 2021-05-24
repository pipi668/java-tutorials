/**
 * Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.lang.prim;

/**
 * @author LiuJian
 * @date 2017年4月20日
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
        byte c = 127;
        System.out.println(0xFF & c);

        System.out.println((a & 0x80) == 0x80);

        short msb = 128;
        short lsb = 9;
        int moved = msb << 8;
        int result = msb << 8 | lsb;
        System.out.println(result);
        byte ab = 8;
        byte movedab = (byte) (ab << 1); // << return an int;
        System.out.println(movedab);
        long moved24 = msb << 24; // -2147483648
        System.out.println(moved24);
        long moved24Or = (msb << 24) & (0xFFFFFFFF); // 2147483648
        System.out.println(moved24Or); //
        long moved24Upgrade = ((long) msb << 24); // 2147483648
        System.out.println(moved24Upgrade);
    }

}
