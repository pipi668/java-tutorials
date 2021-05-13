/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.loop;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import cn.aposoft.tutorial.http.impl.conn.BasicHttpClientConnectionManager;

/**
 * @author LiuJian
 * @date 2017年4月18日
 * 
 */
public class ClosedConnection {

    /**
     * elapse:18719
     * 19633 for connection closed request for 10000 times
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static void main(String[] args) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet("http://www.aposoft.cn/");
        get.addHeader("Connection", "close");
        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        try (CloseableHttpClient client = HttpClients.createMinimal(connectionManager)) {
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
