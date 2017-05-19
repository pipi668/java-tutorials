/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.curr;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author LiuJian
 * @date 2017年5月5日
 * 
 */
public class CompleteServiceDemo {
    static class Task implements Callable<Integer> {
        final int[] values;

        public Task(final int[] values) {
            this.values = values;
        }

        @Override
        public Integer call() throws Exception {
            return sum(values);
        }
    }

    /**
     * 完成类似 Go语言Channel的并行操作
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newCachedThreadPool();
        try {
            CompletionService<Integer> compService = new ExecutorCompletionService<Integer>(service);
            final int[] values = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
            compService.submit(new Task(Arrays.copyOfRange(values, 0, values.length / 2)));
            compService.submit(new Task(Arrays.copyOfRange(values, values.length / 2, values.length)));
            int totalSum = channel(compService, 2);
            System.out.println(totalSum);
        } finally {
            service.shutdown();
        }
    }

    static int sum(int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }

    private static int channel(CompletionService<Integer> compService, final int count) throws InterruptedException, ExecutionException {
        int sum = 0;
        for (int i = 0; i < count; i++) {
            Future<Integer> f = compService.take();
            sum += f.get();
        }
        return sum;
    }
}
