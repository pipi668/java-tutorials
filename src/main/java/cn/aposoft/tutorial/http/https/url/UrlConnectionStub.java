/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.url;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

/**
 * 因JDK本身并不信任java证书,因此使用了自定义的 jssecacerts 证书库,并导入了aposoft.cn的证书
 * 
 * @author LiuJian
 * @date 2017年4月5日
 * 
 */
public class UrlConnectionStub {

    /**
     * @param args
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws MalformedURLException {
        Properties systemProps = System.getProperties();
        
        System.setProperty("javax.net.debug", "all");
        systemProps.put("javax.net.ssl.trustStore", "jssecacerts");
        systemProps.put("javax.net.ssl.trustStorePassword", "changeit");
        URL url = new URL("https://www.aposoft.cn/");
        // SSLSocketFactory sf = (SSLSocketFactory)
        // SSLSocketFactory.getDefault();
        // HttpsURLConnection.setDefaultSSLSocketFactory(sf);
        try {
            URLConnection conn = url.openConnection();
            if (conn instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
                System.out.println("do input:" + httpsConn.getDoInput());
                System.out.println("do output:" + httpsConn.getDoOutput());
                httpsConn.connect();

                // OutputStream output = httpsConn.getOutputStream();
                InputStream input = httpsConn.getInputStream();
                System.out.println(IOUtils.toString(input, StandardCharsets.UTF_8));
                httpsConn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
