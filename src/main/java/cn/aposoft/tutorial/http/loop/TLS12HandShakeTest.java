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
public class TLS12HandShakeTest {

    /**
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static void main(String[] args) throws ClientProtocolException, IOException {
        System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.trustStore", "jssecacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        HttpGet get = new HttpGet("https://www.aposoft.cn:8443/");
        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            long begin = System.currentTimeMillis();
            for (int i = 0; i < 2; i++) {
                CloseableHttpResponse resp = client.execute(get);
                EntityUtils.consumeQuietly(resp.getEntity());
                System.out.println("one-time..." );
            }
            long end = System.currentTimeMillis();
            System.out.println("elapse:" + (end - begin));
        }
    }

}
