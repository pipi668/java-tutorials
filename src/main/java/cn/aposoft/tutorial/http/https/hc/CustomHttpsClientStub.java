/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.hc;

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
        System.setProperty("javax.net.debug", "ssl,handshake");

        SSLConnectionSocketFactory socketFacotry = SSLConnectionSocketFactory.getSocketFactory();

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", socketFacotry).build();

        HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(registry);

        try (MinimalHttpClient client = new MinimalHttpClient(connManager);) {

            System.out.println();
            System.out.println(gomefinanceUrl);
            System.out.println();
            HttpGet httpGet = new HttpGet(gomefinanceUrl);

            try (CloseableHttpResponse response = client.execute(httpGet);) {

                HttpEntity entity = response.getEntity();

                String responseText = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                System.out.println();
                System.out.println(responseText);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000 * 1000);
            } catch (InterruptedException e) {
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
