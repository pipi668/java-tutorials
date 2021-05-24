package cn.aposoft.tutorial.thread;

public class ThreadLocalStub {
    public static void main(String[] args) throws InterruptedException {
        // ThreadLocal 不会被回收的原因是在 ThreadLocalMap外部存在强引用,而当强引用存在时，Entry的Key不会被回收。
        // 强引用不存在时，从外部无法访问到ThreadLocalMap内部的entry，因此该弱引用可能被回收，但value不会被回收。
        // 需要额外的处理对 key为null的Entry进行remove()
        // Entry 对key的引用是弱引用(ThreadLocal<?>)
        // Entry 对value的引用是强引用
        ThreadLocal<Integer> t = new ThreadLocal<>();
        t.set(100);
        System.out.println(t.get() );

        System.gc();
        Thread.sleep(1000);

        Integer i = t.get();
        System.out.println(i);

        t.remove();


    }
}
