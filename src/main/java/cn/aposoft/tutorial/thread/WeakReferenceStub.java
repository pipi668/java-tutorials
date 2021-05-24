package cn.aposoft.tutorial.thread;
import java.lang.ref.WeakReference;
public class WeakReferenceStub {
    public static void main(String[] args) {

        Salad salad = new Salad(new Apple("红富士"));
        //通过WeakReference的get()方法获取Apple
        System.out.println("Apple:" + salad.get());
        System.gc();
        try {
            //休眠一下，在运行的时候加上虚拟机参数-XX:+PrintGCDetails，输出gc信息，确定gc发生了。
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.gc();

        //如果为空，代表被回收了
        if (salad.get() == null) {
            System.out.println("clear Apple。");
        }
    }

    static class Apple {

        private String name;

        public Apple(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * 覆盖finalize，在回收的时候会执行。
         *
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("Apple： " + name + " finalize。");
        }

        @Override
        public String toString() {
            return "Apple{" +
                    "name='" + name + '\'' +
                    '}' + ", hashCode:" + this.hashCode();
        }
    }

    /**
     * Salad class
     * 继承WeakReference，将Apple作为弱引用。
     * 注意到时候回收的是Apple，而不是Salad
     *
     * @author BrightLoong
     * @date 2018/5/25
     */
    static class Salad extends WeakReference<Apple> {
        public Salad(Apple apple) {
            super(apple);
        }
    }
}
