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
 * @date 2017年4月18日
 * 
 */
public class SslLoopStub {

    /**
     * 12990 for 10000 times
     * 
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static void main(String[] args) throws ClientProtocolException, IOException {
        
        System.setProperty("javax.net.ssl.trustStore", "jssecacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        HttpGet get = new HttpGet("https://www.aposoft.cn:8443/");
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
