package cn.aposoft.tutorial.files;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FileLoading {

    public static void main(String[] args) throws IOException {
        File file = new File("d:/a.txt");
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        Counter counter = new Counter(queue);
        Thread t = new Thread(counter);
        t.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String s;
            System.out.println(System.currentTimeMillis());
            while ((s = reader.readLine()) != null) {
                queue.put(s);
                if (queue.size() > 10 * 1000) {
                    try {
                        System.out.println("queue size:" + queue.size());
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(System.currentTimeMillis());
            //设置读取结束
            counter.queueComplete = true;
            while (!counter.finished()) {
//                System.out.println("queue size:" + queue.size());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("counter:" + counter.get() + ",queue size:" + queue.size());
            System.out.println(System.currentTimeMillis());
//            System.out.println((spre != null ? spre.length() : 0) + ":" + spre);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Counter implements Runnable {
        final BlockingQueue<String> queue;
        volatile boolean queueComplete = false;
        volatile boolean runFinished = false;
        private AtomicInteger count = new AtomicInteger(0);

        public Counter(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        public boolean finished() {
            return runFinished;
        }

        public int get() {
            return count.get();
        }

        @Override
        public void run() {
            System.out.println("run:start.");
            while (!queueComplete || !queue.isEmpty()) {
                try {
//                    System.out.println(queue.size() + ":" + count.get());
                    String s = queue.poll(5, TimeUnit.MILLISECONDS);
                    parse(s);
//                    System.out.println("counter:"+count.get());
                } catch (InterruptedException e) {
                    Thread t = Thread.currentThread();
                    if (t.isInterrupted()) {
                        t.interrupted();
                    }
                }
            }
            runFinished = true;
            System.out.println("finished count:" + count.get());
        }

        private void parse(String s) {
            int index = -5;
            int localCount = 0;
            while (s != null && (index = s.indexOf("redis", index + 5)) != -1) {
                localCount++;
            }
            count.addAndGet(localCount);
        }
    }
}
