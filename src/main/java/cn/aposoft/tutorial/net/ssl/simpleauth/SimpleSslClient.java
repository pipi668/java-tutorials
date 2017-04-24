/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.ssl.simpleauth;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;

/**
 * // sun.security.ssl.SSLSocketFactoryImpl
 * 
 * <p>
 * server_name extension 的添加取决于rowHostname字段的赋值,需要显式调用
 * {@code String host=***;}的方式对sslSocket赋值,才能实现此扩展
 * </p>
 * 
 * <pre>
 *  // add server_name extension
    if (enableSNIExtension) {
    // We cannot use the hostname resolved from name services.  For
    // virtual hosting, multiple hostnames may be bound to the same IP
    // address, so the hostname resolved from name services is not
    // reliable.
    String hostname = getRawHostnameSE();
    
    // we only allow FQDN
    if (hostname != null && hostname.indexOf('.') > 0 &&
            !IPAddressUtil.isIPv4LiteralAddress(hostname) &&
            !IPAddressUtil.isIPv6LiteralAddress(hostname)) {
        clientHelloMessage.addServerNameIndicationExtension(hostname);
    }
 * </pre>
 * 
 * @author LiuJian
 * @date 2017年4月10日
 * 
 */
public class SimpleSslClient {
    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.debug", "ssl,handshake");/* all, */

        System.setProperty("javax.net.ssl.trustStore", "f:/key/trust-client.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        //
        // Strict: If clients do not send the proper RFC 5746 messages, initial
        // connections will immediately be terminated by the server
        // (SSLHandshakeException/handshake_failure).
        //
        // Interoperable: Initial connections from legacy clients allowed
        // (missing RFC 5746 messages), but renegotiations will not be allowed
        // by the server. [2][3]
        //
        // Insecure: Connections and renegotiations with legacy clients are
        // allowed, but are vulnerable to the original MITM attack.

        // Strict Mode
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "false");
        System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "false");

        final String host = "aposoft.cn";
        final InetAddress inetAddress = InetAddress.getByName(host);
        final int port = 9101;

        final SSLContext sslContext = SSLContext.getDefault();
        try (
                // 以下两种方式均可以实现 server_name extension的加载
                // 方法1:
                // SSLSocket sslsocket = (SSLSocket)
                // sslContext.getSocketFactory().createSocket(SocketFactory.getDefault().createSocket(inetAddress,
                // port),
                // inetAddress.getHostName(), 9101, false);
                // 方法2:
                SSLSocket sslsocket = (SSLSocket) sslContext.getSocketFactory().createSocket(inetAddress.getHostName(), port);) {
            sslsocket.setEnabledProtocols(new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" });
            SSLParameters paramSSLParameters = sslsocket.getSSLParameters();
            // identificationAlgorithm EndpointIdentificationAlgorithm
            // HTTPS , LDAPS
            paramSSLParameters.setEndpointIdentificationAlgorithm("HTTPS");

            sslsocket.startHandshake();
            //
            System.out.println("****Secure Renegotiation****");
            sslsocket.startHandshake();

            try (OutputStream outputStream = sslsocket.getOutputStream(); //
                    InputStream inputStream = sslsocket.getInputStream();) {
                try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                        Reader bufferedreader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);) {
                    String text = "沉睡的雄狮\r\n";
                    System.out.println("write:" + text);
                    bufferedWriter.write(text);
                    bufferedWriter.flush();

                    char[] buf = new char[1];
                    outloop: // 读取的循环
                    while (true) {
                        try {
                            int len = bufferedreader.read(buf);
                            if (len > 0) {
                                if (!('\r' == buf[0] || '\n' == buf[0])) {
                                    System.out.print(buf[0]);
                                } else if ('\r' == buf[0]) {
                                    System.out.print("[\\r]");
                                } else {
                                    System.out.println("[\\n]");
                                    break outloop;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

        }
        System.out.println("client is going to leave...");
        TimeUnit.SECONDS.sleep(2);

    }

    private static void diaplay(SSLParameters params) {
        System.out.println(Arrays.toString(params.getProtocols()));
        System.out.println(params.getNeedClientAuth());
        System.out.println(params.getWantClientAuth());
        System.out.println(params.getAlgorithmConstraints());
        System.out.println(params.getEndpointIdentificationAlgorithm());
        System.out.println(Arrays.toString(params.getCipherSuites()));
    }

}
