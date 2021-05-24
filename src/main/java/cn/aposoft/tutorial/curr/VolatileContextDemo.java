/**
 *
 */
package cn.aposoft.tutorial.curr;

/**
 * volatile data synchronizes-with 说明
 * 在下面的验证中，
 * jc < ic 的发生次数为 0
 * 		说明: 在每次读取n1.i的时候，在n1.i赋值前对n.j的赋值都是线程间同步的。
 * 	   	     jc 不可能小于ic,因volatile存在happens-before的传递规则
 * jc > ic 每次验证中必然出现,但不是在jc《10的时刻就出现，且差距是不确定的
 * 		说明: 两次读取间存在主线程对缓存的刷新。函数过程执行的两个语句间，另一个线程可能存在对主存数据的二次刷新。
 * 	         一般情况下，存在为1的差距，但偶然情况下，会存在大于1的的差，说明可能存在判断线程出现中断的现象。
 * @author Secoo
 *
 */
public class VolatileContextDemo {
    static volatile boolean running = true;

    static class Num {
        volatile long i;
        long j;
        Object a, b, c, d, e, f, g, h, k, l, m, n, o, p;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("beginning...");
        final Num n = new Num();

        n.i = 0;
        n.j = 0;
        final Num n1 = new Num();

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("t1 begin");
                long ic, jc;
                int count = 0;
                while (running) {
                    ic = n1.i;
                    jc = n.j;
                    if (jc > ic + 1) { // jc 不可能小于ic,因volatile存在happens-before的传递规则
                        // jc > ic ,说明在程序的连续赋值之间，存在主存的刷新，并且这种刷新被捕捉到了
                        System.out.printf("i,j:%d,%d\n", ic, jc);
                        if (++count > 10) {
                            running = false;
                        }
                    }
                }
                System.out.println("out of t1.");
            }
        });
        long l = 0;

        t1.start();
        while (running && (l++) > 1000 * 1000 * 1000 * 1000) {
            n1.i = n.j = (n.j + 1);
        }
        System.out.println("out of main loop");
        running = false;
    }

}
