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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
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

        System.setProperty("javax.net.debug", "ssl");
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
                .loadTrustMaterial(new File("jssecacerts"), "changeit".toCharArray(), trustStrategy) //
                .build();
        String[] allProtocols = null;
        String[] supportedCipherSuites = null;
        SSLSocketFactory sslFactory = (SSLSocketFactory) sslContext.getSocketFactory();
        List<String> enabledProtocols = new ArrayList<>();
        try (SSLSocket sslSocket = (SSLSocket) sslFactory.createSocket();) {
            allProtocols = sslSocket.getSupportedProtocols();
            supportedCipherSuites = sslFactory.getSupportedCipherSuites();

            for (final String protocol : allProtocols) {
                if (!protocol.startsWith("SSL")) {
                    enabledProtocols.add(protocol);
                }
            }
            System.out.println("ENABLED+PROTOCOLS:" + enabledProtocols);
        }
        // cn.aposoft.tutorial.http.https.verifier
        HostnameVerifier hostNameVerifier = new DefaultHostnameVerifier();
        SSLConnectionSocketFactory sslSocketFactory;
        if (allProtocols == null || supportedCipherSuites == null) {
            sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostNameVerifier);
        } else {
            sslSocketFactory = new SSLConnectionSocketFactory(sslContext, enabledProtocols.toArray(new String[enabledProtocols.size()]),
                    supportedCipherSuites, hostNameVerifier);
        }
        try (CloseableHttpClient client = HttpClients.custom()//
                .setSSLContext(sslContext) //
                .setSSLSocketFactory(sslSocketFactory)

                .build()) {
            HttpGet httpGet = new HttpGet("https://www.aposoft.cn:8443/");
            // HttpGet httpGet = new HttpGet("https://www.startssl.com/");
            try (CloseableHttpResponse resp = client.execute(httpGet)) {
                String respText = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
                System.out.println(respText);
            }
        }
    }
}
