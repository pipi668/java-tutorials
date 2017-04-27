/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.loop;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author LiuJian
 * @date 2017年4月25日
 * 
 */
public class MultiHostHttpRequestDemo {

    final static CloseableHttpClient client = HttpClients.createDefault();

    final static ConcurrentMap<String, AtomicInteger> urlMaps = new ConcurrentHashMap<>();
    final static AtomicInteger totalConn = new AtomicInteger(0);

    /**
     * @param args
     */
    public static void main(String[] args) {
        final String[] URLS = new String[] { //
                "https://www.gomefinance.com", //
                "https://www.gomemyf.com", //
                "https://www.aposoft.cn", //
                "https://www.baidu.com", //
                "https://www.sina.com.cn", //
                "https://cn.bing.com", //
                "https://www.sohu.com", //
                "https://www.163.com", //
                "https://www.ifeng.com" };
        for (String url : URLS) {
            urlMaps.put(url, new AtomicInteger(0));
        }

        ExecutorService service = Executors.newFixedThreadPool(100);
        Random r = new Random();
        service.execute(new Worker(URLS[r.nextInt(URLS.length)]));

    }

    static class Worker implements Runnable {

        private String url;

        // 执行一个URL的请求
        public Worker(String url) {
            this.url = url;
        }

        @Override
        public void run() {

            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(httpGet);) {
                urlMaps.get(url).incrementAndGet();
                totalConn.incrementAndGet();
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlMaps.get(url).decrementAndGet();
                totalConn.decrementAndGet();
            }
        }

    }

}
