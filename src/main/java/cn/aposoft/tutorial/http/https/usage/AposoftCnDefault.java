/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.usage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import cn.aposoft.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import cn.aposoft.tutorial.http.https.verifier.DefaultHostnameVerifier;

/**
 * @author LiuJian
 * @date 2017年4月4日
 * 
 */
public class AposoftCnDefault {
    public static void main(String[] args)
            throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {

//        System.setProperty("javax.net.debug", "all,ssl");
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // for (X509Certificate cert : chain) {
                // HttpsTools.visitCertificate(cert);
                // }
                return false;
            }
            // startcom_certification_authority
        };
        SSLContext sslContext = null;
        sslContext = SSLContexts.custom()//
                // .loadTrustMaterial(new File("jssecacerts"),
                // "changeit".toCharArray(), null)//
                .loadTrustMaterial(new File("StartComRoot1.jks"), "changeit".toCharArray(), trustStrategy) //
                .build();
        String[] allProtocols = null;
        String[] supportedCipherSuites = null; // 设为null 取默认值

        String[] enabledProtocols = new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" };

        // cn.aposoft.tutorial.http.https.verifier
        HostnameVerifier hostNameVerifier = new DefaultHostnameVerifier();
        SSLConnectionSocketFactory sslSocketFactory;

        sslSocketFactory = new SSLConnectionSocketFactory(sslContext, enabledProtocols, supportedCipherSuites, hostNameVerifier);

        try (CloseableHttpClient client = HttpClients.custom()//
                .setSSLContext(sslContext) //
                .setSSLSocketFactory(sslSocketFactory).build()) {
            HttpGet httpGet = new HttpGet("https://www.aposoft.cn/");
            // HttpGet httpGet = new HttpGet("https://www.startssl.com/");
            try (CloseableHttpResponse resp = client.execute(httpGet)) {
                String respText = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
                System.out.println(respText);
            }
            System.out.println("open javax.net.debug");
            System.setProperty("javax.net.debug", "all,ssl");
            try (CloseableHttpResponse resp = client.execute(httpGet)) {
                String respText = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
                System.out.println(respText);
            }
        }
    }
}
