package cn.aposoft.tutorial.security;

import java.util.Properties;

public class SslKeyStoreRegistry {

    public static void main(String[] args) {
        // java -Djavax.net.ssl.trustStore=yourTruststore.jks
        // -Djavax.net.ssl.trustStorePassword=123456 YourApp
        Properties systemProps = System.getProperties();
        systemProps.put("javax.net.ssl.trustStore", "jssecacerts");
        systemProps.put("javax.net.ssl.trustStorePassword", "changeit");
        System.setProperties(systemProps);
        // 统一的KeyStore
        // System.setProperty("javax.net.ssl.keyStore", "sslkeystore");
        // System.setProperty("javax.net.ssl.keyStorePassword", "123456");
    }
}
