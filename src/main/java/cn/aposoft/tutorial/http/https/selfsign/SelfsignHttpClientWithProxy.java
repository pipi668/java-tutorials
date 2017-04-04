/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.selfsign;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;

import cn.aposoft.tutorial.http.https.HttpsTools;

/**
 * @author LiuJian
 * @date 2017年4月3日
 * 
 */
public class SelfsignHttpClientWithProxy {

    /**
     * 经验证: 如果证书被替换,连接创建将失败
     * 
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static void main(String[] args)

            throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
        System.out.println("client with proxy start...");
        HttpHost proxy = new HttpHost("localhost", 8888);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        SSLContext sslContext = HttpsTools.createSSLContext();
        try (CloseableHttpClient client = HttpClients.custom()//
                .setRoutePlanner(routePlanner)//
                .setSSLContext(sslContext).build();) {
            final String aposoft_url = "https://aposoft.cn:8443/wx/index.jsp";
            HttpGet get = new HttpGet(aposoft_url);
            // 执行远程请求,并输出结果
            try (CloseableHttpResponse resp = client.execute(get);) {
                System.out.println(EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8));
            }
        }
    }
}
