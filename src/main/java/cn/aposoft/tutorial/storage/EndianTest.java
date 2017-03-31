/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.storage;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @author LiuJian
 * @date 2016年9月20日
 * 
 */
public class EndianTest {

    // 使用方法
    private static Unsafe getUnsafeInstance() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        // return Unsafe.getUnsafe();
        return (Unsafe) theUnsafeInstance.get(Unsafe.class);

    }

    private long demo;

    private short sdemo;

    private int idemo;

    /**
     * @param args
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static void main(String[] args) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Unsafe unsafe = getUnsafeInstance();

        EndianTest testInstance = new EndianTest();

        long demoOffset = unsafe.fieldOffset(EndianTest.class.getDeclaredField("demo"));

        testInstance.demo = 0x1122233455667788L;
        System.out.printf("%x\r\n", testInstance.demo);
        for (int i = 0; i < 8; i++) {
            byte curr = unsafe.getByte(testInstance, demoOffset + i);
            System.out.printf("i:%d,v:%x\r\n", i, curr);
        }

        short s = (short) testInstance.demo;

        System.out.printf("%x\r\n", s);

        long sOffset = unsafe.fieldOffset(EndianTest.class.getDeclaredField("sdemo"));
        testInstance.sdemo = -32768;

        System.out.println();
        for (int i = 0; i < 4; i++) {
            byte curr = unsafe.getByte(testInstance, sOffset + i);
            System.out.printf("i:%d,v:%x\r\n", i, curr);
        }

        long iOffset = unsafe.fieldOffset(EndianTest.class.getDeclaredField("idemo"));
        testInstance.idemo = -32768;
        for (int i = 0; i < 4; i++) {
            byte curr = unsafe.getByte(testInstance, iOffset + i);
            System.out.printf("i:%d,v:%x\r\n", i, curr);
        }

    }

}
