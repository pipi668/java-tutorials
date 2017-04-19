/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.loop;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author LiuJian
 * @date 2017年4月17日
 * 
 */
public class NormalLoopStub {
    /**
     * 7761 ms for 10000 times
     * elapse:9126
     * 
     * @param args
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void main(String[] args) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet("http://localhost/");
        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            long begin = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                CloseableHttpResponse resp = client.execute(get);
                EntityUtils.consumeQuietly(resp.getEntity());
            }
            long end = System.currentTimeMillis();
            System.out.println("elapse:" + (end - begin));
        }

    }
}
