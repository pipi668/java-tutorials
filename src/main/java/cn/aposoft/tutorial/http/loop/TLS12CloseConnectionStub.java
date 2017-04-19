/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.loop;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * elapse:300367 for 10000 times Close_Connection for TLS1.2
 * 
 * elapse:36822
 * 
 * @author LiuJian
 * @date 2017年4月19日
 * 
 */
public class TLS12CloseConnectionStub {

    /**
     * elapse:299200 for TLSv1 Close_Connection elapse:283293 elapse:37275
     * elapse:36237
     * 
     * @param args
     */
    public static void main(String[] args)
            throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
        System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.trustStore", "StartComRoot1.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        HttpGet get = new HttpGet("https://www.aposoft.cn:8443/");
        // HttpGet get = new HttpGet("https://www.gomemyf.com/");
        get.addHeader("Connection", "close");
        SSLContext sslContext = SSLContexts.createDefault();
        final String[] enabledProtocols = new String[] { "TLSv1.2" };
        final String[] supportedCipherSuites = new String[] { "TLS_RSA_WITH_AES_256_CBC_SHA" };
        // cn.aposoft.tutorial.http.https.verifier
        HostnameVerifier hostNameVerifier = new DefaultHostnameVerifier();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, enabledProtocols, supportedCipherSuites,
                hostNameVerifier);

        try (CloseableHttpClient client = HttpClients.custom()//
                .setSSLContext(sslContext) //
                .setSSLSocketFactory(sslSocketFactory).build()) {
            long begin = System.currentTimeMillis();

            for (int i = 0; i < 2; i++) {
                CloseableHttpResponse resp = client.execute(get);
                {
                    // System.out.println(EntityUtils.toString(resp.getEntity()));
                    EntityUtils.consumeQuietly(resp.getEntity());

                }
            }
            long end = System.currentTimeMillis();
            System.out.println("elapse:" + (end - begin));
        }
    }

}
