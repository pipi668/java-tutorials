/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

/**
 * @author LiuJian
 * @date 2017年4月4日
 * 
 */
public class AposoftCnDefault {
    public static void main(String[] args)
            throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                for (X509Certificate cert : chain) {
                    HttpsTools.visitCertificate(cert);
                }
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
        try (CloseableHttpClient client = HttpClients.custom()//
                .setSSLContext(sslContext) //
                .build()) {
            HttpGet httpGet = new HttpGet("https://www.aposoft.cn/");
            // HttpGet httpGet = new HttpGet("https://www.startssl.com/");
            try (CloseableHttpResponse resp = client.execute(httpGet)) {
                String respText = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
                System.out.println();
            }
        }
    }
}
