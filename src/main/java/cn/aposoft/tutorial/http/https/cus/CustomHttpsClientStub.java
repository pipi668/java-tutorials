/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.cus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.util.EntityUtils;

import cn.aposoft.http.conn.ssl.SSLConnectionSocketFactory;
import cn.aposoft.http.impl.client.MinimalHttpClient;
import cn.aposoft.http.impl.conn.BasicHttpClientConnectionManager;

/**
 * 自定义HTTPClienttrace
 * 
 * @author LiuJian
 * @date 2017年4月7日
 * 
 */
public class CustomHttpsClientStub {

    public static void main(String[] args) {
        final String gomefinanceUrl = "https://www.gomefinance.com.cn/";

        interrupt();
        SSLConnectionSocketFactory socketFacotry = SSLConnectionSocketFactory.getSocketFactory();
        interrupt();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", socketFacotry).build();
        interrupt();
        HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(registry);
        interrupt();
        try (MinimalHttpClient client = new MinimalHttpClient(connManager);) {
            interrupt();
            System.out.println();
            System.out.println(gomefinanceUrl);
            System.out.println();
            HttpGet httpGet = new HttpGet(gomefinanceUrl);
            interrupt();
            try (CloseableHttpResponse response = client.execute(httpGet);) {
                interrupt();

                interrupt();
                HttpEntity entity = response.getEntity();
                interrupt();
                String responseText = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                interrupt();
                System.out.println();
                System.out.println(responseText);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void interrupt() {
        // System.out.println("interrupt.");
        // try {
        // System.in.read();
        // } catch (IOException e1) {
        // e1.printStackTrace();
        // }
    }
}
