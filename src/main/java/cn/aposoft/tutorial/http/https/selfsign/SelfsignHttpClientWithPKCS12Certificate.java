/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.selfsign;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
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
public class SelfsignHttpClientWithPKCS12Certificate {

    /**
     * 
     * 自签名证书签名域名为 aposoft.cn
     * <p>
     * 将导出的公钥放入客户端TrustManager的证书内
     * 
     * @param args
     * @throws IOException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws UnrecoverableKeyException 
     */
    public static void main(String[] args)
            throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException {

        SSLContext sslContext = HttpsTools.createPKCS12CertificateSnifferSSLContext();
        try (CloseableHttpClient client = HttpClients.custom()//
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
