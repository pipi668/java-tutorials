/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.ssl.clientauth;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author LiuJian
 * @date 2017年4月10日
 * 
 */
public class SimpleSslClient {
    public static void main(String[] args) throws Exception {
        // System.setProperty("javax.net.debug", "all,ssl,handshake");
        System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        System.setProperty("javax.net.ssl.keyStore", "f:/key/privateKey.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "f:/key/trust-client.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        // Secure Renegotiation in all modes
        
        // sun.security.ssl.allowUnsafeRenegotiation Introduced in Phase 1, this
        // controls whether legacy (unsafe) renegotiations are permitted.
        // sun.security.ssl.allowLegacyHelloMessages Introduced in Phase 2, this
        // allows the peer to handshake without requiring the proper RFC 5746
        // messages.
        // Strict Mode
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "false");
        System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "false");

        final String host = "aposoft.cn";
        final int port = 9101;

        try (SSLSocket sslsocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, port);) {

            sslsocket.setEnabledProtocols(new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" });
            SSLParameters paramSSLParameters = sslsocket.getSSLParameters();
            paramSSLParameters.setEndpointIdentificationAlgorithm("HTTPS");

            try (OutputStream outputStream = sslsocket.getOutputStream(); //
                    InputStream inputStream = sslsocket.getInputStream();) {
                try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                        Reader bufferedreader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);) {
                    String text = "沉睡的雄狮\r\n";
                    System.out.println("write:" + text);
                    bufferedWriter.write(text);
                    bufferedWriter.flush();
                    System.out.println("waiting for server resp...");
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
}
