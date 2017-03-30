package cn.aposoft.tutorial.thread;

/**
 * 
 * @author LiuJian
 *
 */
public class ThreadStub {

    static class Command implements Runnable {

        @Override
        public void run() {
            throw new RuntimeException();
        }

    }

    /**
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) {

        /*
         * Thread t = new Thread(new Command()); t.start(); try {
         * Thread.sleep(10000); t.join(); } catch (InterruptedException e) {
         * e.printStackTrace(); }
         */

        new Command().run();

    }
}
