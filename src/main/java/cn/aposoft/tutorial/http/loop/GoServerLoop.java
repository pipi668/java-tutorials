/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.loop;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author LiuJian
 * @date 2017年5月10日
 * 
 */
public class GoServerLoop {

    /**
     * 
     * 
     * @param args
     */
    public static void main(String[] args) {

        try (CloseableHttpClient httpClient = HttpClients.custom().setMaxConnPerRoute(200).setMaxConnTotal(200).build()) {
            HttpGet httpGet = new HttpGet("http://127.0.0.1");
            long begin = System.currentTimeMillis();
            for (int i = 0; i < /* 1000 **/5; i++) {
                System.out.println("begin:" + System.currentTimeMillis());
                try (CloseableHttpResponse resp = httpClient.execute(httpGet)) {
                    if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        System.out.println(resp.getFirstHeader("Connection"));
                        EntityUtils.consumeQuietly(resp.getEntity());
                    }
                    System.out.println("complete:" + System.currentTimeMillis());
                    try {
                        TimeUnit.MILLISECONDS.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("awake:" + System.currentTimeMillis());
                }

            }
            long end = System.currentTimeMillis();

            System.out.println("Duration:" + (end - begin));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
