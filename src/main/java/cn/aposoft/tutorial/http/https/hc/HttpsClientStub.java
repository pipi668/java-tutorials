/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.hc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import cn.aposoft.tutorial.http.https.verifier.DefaultHostnameVerifier;

/**
 * @author LiuJian
 * @date 2017年3月28日
 * 
 */
public class HttpsClientStub {

    /**
     * 
     * 
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, ClassNotFoundException, NoSuchFieldException,
            NoSuchMethodException, SecurityException {
        // String securityUrl =
        // "https://myfen.gomemyf.com/fen-api/goods/?sku=100001911";
        String securityUrlGomeFinance = "https://www.gomefinance.com.cn/";
        final String aposoft_url = "https://www.aposoft.cn/";
        // readContent1(securityUrl);

        // 1 读取公钥证书到本地
        // readContent(securityUrl);
        // readContent(aposoft_url);
        readContent(securityUrlGomeFinance);

        // defaultManagers();
    }

    public static void defaultKeyManagers() throws ClassNotFoundException, NoSuchFieldException {
        // sun.security.ssl.SunX509KeyManagerImpl impl;
        try {
            @SuppressWarnings("restriction")
            Method method = sun.security.ssl.SSLContextImpl.DefaultSSLContext.class.getDeclaredMethod("getDefaultKeyManager");

            if (!method.isAccessible()) {
                method.setAccessible(true);
                try {
                    Object obj = method.invoke(null);
                    if (obj instanceof javax.net.ssl.KeyManager[]) {
                        javax.net.ssl.KeyManager[] keyManagers = (javax.net.ssl.KeyManager[]) obj;
                        for (javax.net.ssl.KeyManager km : keyManagers) {

                            Field field = km.getClass().getDeclaredField("credentialsMap");

                            // 可访问性
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                                Object credentialsMap = field.get(km);
                                if (credentialsMap instanceof Map) {
                                    @SuppressWarnings("unchecked")
                                    Map<String, Object> map = (Map<String, Object>) credentialsMap;
                                }

                            }
                        }
                    }

                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    //
    public static void defaultTrustManagers() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, SecurityException {
        @SuppressWarnings("restriction")
        Method method = sun.security.ssl.SSLContextImpl.DefaultSSLContext.class.getDeclaredMethod("getDefaultTrustManager");

        if (!method.isAccessible()) {
            method.setAccessible(true);
            try {
                Object obj = method.invoke(null);
                if (obj instanceof javax.net.ssl.TrustManager[]) {
                    javax.net.ssl.TrustManager[] keyManagers = (javax.net.ssl.TrustManager[]) obj;
                    for (javax.net.ssl.TrustManager km : keyManagers) {

                        Field field = km.getClass().getDeclaredField("trustedCerts");

                        // 可访问性
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                            Object certificateSet = field.get(km);
                            if (certificateSet instanceof Set) {
                                @SuppressWarnings("unchecked")
                                Set<X509Certificate> set = (Set<X509Certificate>) certificateSet;

                            }

                        }
                    }
                }

            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    //
    public static void defaultManagers() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, SecurityException {
        defaultKeyManagers();
        defaultTrustManagers();
    }

    // 如果加载拦截, https直接报证书异常
    @SuppressWarnings("unused")
    private static void readContent1(String securityUrl) throws NoSuchAlgorithmException {
        // HttpHost proxy = new HttpHost("localhost", 8888);
        // DefaultProxyRoutePlanner routePlanner = new
        // DefaultProxyRoutePlanner(proxy);

        try (CloseableHttpClient client = HttpClients.custom()
                // .setRoutePlanner(routePlanner)
                .build();) {
            HttpUriRequest request = new HttpGet(securityUrl);
            try (CloseableHttpResponse resp = client.execute(request);) {
                String respText = EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加拦截,直接报证书异常
    private static void readContent(String securityUrl) throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContext.getDefault();

        try {
            Field field = SSLContext.class.getDeclaredField("contextSpi");
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                    Object o = field.get(sslContext);
                    if (o instanceof SSLContextSpi) {
                        SSLContextSpi spi = (SSLContextSpi) o;
                    }

                }
            } catch (IllegalArgumentException | IllegalAccessException e) {

                e.printStackTrace();
            }

        } catch (NoSuchFieldException | SecurityException e1) {

            e1.printStackTrace();
        }

        HostnameVerifier hostNameVerifier = new DefaultHostnameVerifier();

        SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext, hostNameVerifier);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()) //
                .register("https", sslFactory) //
                .build();
        // 设置
        // HttpHost proxy = new HttpHost("localhost", 8888);

        // DefaultProxyRoutePlanner routePlanner = new
        // DefaultProxyRoutePlanner(proxy);
        HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(registry);

        try (CloseableHttpClient client = HttpClients.custom() // .setRoutePlanner(routePlanner)
                .setConnectionManager(connManager).build();) {
            HttpUriRequest request = new HttpGet(securityUrl);
            try (CloseableHttpResponse resp = client.execute(request);) {
                String respText = EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8"));
            }

            Thread.sleep(1000 * 3600);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
