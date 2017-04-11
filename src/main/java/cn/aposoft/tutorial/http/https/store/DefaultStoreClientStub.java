package cn.aposoft.tutorial.http.https.store;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DefaultStoreClientStub {

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
        Properties systemProps = System.getProperties();
        systemProps.put("javax.net.ssl.trustStore", "jssecacerts");
        systemProps.put("javax.net.ssl.trustStorePassword", "changeit");

        try (CloseableHttpClient client = HttpClients.custom().build();) {
            final String aposoft_url = "https://www.aposoft.cn:8443/";
            HttpGet get = new HttpGet(aposoft_url);
            // 执行远程请求,并输出结果
            try (CloseableHttpResponse resp = client.execute(get);) {
                System.out.println(EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8));
            }
        }

    }
}
