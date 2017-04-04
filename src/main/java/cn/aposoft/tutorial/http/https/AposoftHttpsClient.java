/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import cn.aposoft.tutorial.http.https.verifier.DefaultHostnameVerifier;

/**
 * @author LiuJian
 * @date 2017年4月3日
 * 
 */
public class AposoftHttpsClient {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, CertificateException, FileNotFoundException,
            IOException, KeyStoreException, UnrecoverableKeyException {
        final String aposoft_url = "https://www.aposoft.cn/";
        // SSLContext sslContext = SSLContext.getDefault();

        SSLContext sslContext = null;
        try {
            final KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(new FileInputStream("f:/key/trust-client.jks"), "changeit".toCharArray());
            System.out.println("trustStore Name:\t" + truststore.getClass().getName());
            // 拦截读取证书使用
            // TrustStrategy trustAposoft = createTrustStrategy(truststore);
            sslContext = SSLContexts.custom()//
                    // .loadTrustMaterial(truststore, trustAposoft)//
                    .loadTrustMaterial(truststore, null) //
                    .build();
        } catch (KeyManagementException e1) {
            e1.printStackTrace();
            throw e1;
        }
        // cn.aposoft.tutorial.http.https.verifier
        HostnameVerifier hostNameVerifier = new DefaultHostnameVerifier();
        SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext, hostNameVerifier);
        // SSLConnectionSocketFactory sslFactory = new
        // SSLConnectionSocketFactory(sslContext);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()//
                .register("https", sslFactory) //
                .build();
        HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(registry);
        try (CloseableHttpClient client = HttpClients.createMinimal(connManager);) {
            HttpGet get = new HttpGet(aposoft_url);
            try {
                try (CloseableHttpResponse resp = client.execute(get);) {
                    System.out.println(EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // 用于拦截读取trustCert证书使用,平时不用
    private static TrustStrategy createTrustStrategy(final KeyStore truststore) {
        TrustStrategy trustAposoft = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                if (chain != null && chain.length > 0) {
                    int i = 0;
                    System.out.println("authType:" + authType + ",chain.length:" + chain.length);
                    for (X509Certificate cert : chain) {
                        // print(cert);
                        try {
                            truststore.setCertificateEntry("aposoft-cn-cert" + (i++), cert);
                        } catch (KeyStoreException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // try {
                // print(truststore);
                // } catch (UnrecoverableKeyException |
                // NoSuchAlgorithmException e1) {
                // e1.printStackTrace();
                // }
                try {
                    truststore.store(new FileOutputStream("f:/key/trust-client-1.jks"), "changeit".toCharArray());
                } catch (KeyStoreException | NoSuchAlgorithmException | IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

        };
        return trustAposoft;
    }

    private static void print(KeyStore truststore) throws UnrecoverableKeyException, NoSuchAlgorithmException {
        try {
            Enumeration<String> alias = truststore.aliases();
            System.out.println("entry size:\t" + truststore.size());
            while (alias.hasMoreElements()) {

                String a = alias.nextElement();
                System.out.println("alias:\t" + a);
                if (truststore.isCertificateEntry(a)) {
                    print(truststore.getCertificate(a));
                } else if (truststore.isKeyEntry(a)) {
                    System.out.println("a is a key entry.");
                    Key key = truststore.getKey(a, "changeit".toCharArray());
                    print(key);
                }
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private static void print(Key key) {
        System.out.println(key.getAlgorithm());
        System.out.println(key.getFormat());
        System.out.println("class:" + key.getClass().getName());
        if (key instanceof RSAPrivateCrtKey) {
            print((RSAPrivateCrtKey) key);
        }
    }

    private static void print(RSAPrivateCrtKey key) {
        System.out.println("RSA Private Key:");
        System.out.println(key.getModulus());
        System.out.println(key.getCrtCoefficient());
        System.out.println(key.getPrimeExponentP());
        System.out.println(key.getPrimeExponentQ());
        System.out.println(key.getPrimeP());
        System.out.println(key.getPrimeQ());
        System.out.println(key.getPrivateExponent());
        System.out.println(key.getPublicExponent());
        System.out.println(key.getFormat());
        System.out.println("class:" + key.getClass().getName());
    }

    private static void print(Certificate certificate) {
        System.out.println(certificate.getType());
        System.out.println(certificate.getClass());
        System.out.println(certificate.getPublicKey());
        if (certificate instanceof X509Certificate) {
            print((X509Certificate) certificate);
        }
    }

    private static void print(X509Certificate certificate) {
        System.out.println(certificate.getSigAlgName());
        System.out.println(certificate.getSigAlgOID());
        System.out.println(certificate.getVersion());
        System.out.println(certificate.getIssuerDN());
        System.out.println(certificate.getIssuerUniqueID());
        System.out.println(certificate.getSubjectDN());
        System.out.println(certificate.getSubjectUniqueID());
    }
}
