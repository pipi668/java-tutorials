package cn.aposoft.tutorial.thread;

/**
 * @author LiuJian
 */
public class ThreadStub {

    static class Command implements Runnable {

        @Override
        public void run() {
            throw new RuntimeException();
        }

    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) {

        /*
         * Thread t = new Thread(new Command()); t.start(); try {
         * Thread.sleep(10000); t.join(); } catch (InterruptedException e) {
         * e.printStackTrace(); }
         */
        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
        // 设置打断状态
        Thread.currentThread().interrupt();
        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
        // 清除打断状态，并返回清除前状态
        System.out.println("isInterrupted:" + Thread.interrupted()); ;
        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
//        new Command().run();

    }
}
