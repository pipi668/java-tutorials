/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.ssl.clientauth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * 双向认证服务 SSL Socket Demo
 * 
 * @author LiuJian
 * @date 2017年4月10日
 * 
 */
public class SimpleSslServer {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // System.setProperty("javax.net.debug", "all,ssl,handshake");
        System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.keyStore", "F:/key/aposoft.cn.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "F:/key/aposoft.cn.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        
        // Strict Mode
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "false");
        System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "false");
        
        
        final String host = "aposoft.cn";
        final InetAddress inetAddress = InetAddress.getByName(host);
        final int port = 9101;
        try (SSLServerSocket serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(port, 0, inetAddress);) {
            serverSocket.setEnabledProtocols(new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" });
            // 要求客户端身份验证
            serverSocket.setNeedClientAuth(true);
            SSLParameters paramSSLParameters = serverSocket.getSSLParameters();
            // identificationAlgorithm EndpointIdentificationAlgorithm
            // HTTPS , LDAPS
            paramSSLParameters.setEndpointIdentificationAlgorithm("HTTPS");

            while (true) {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                Accepter accepter = new Accepter(socket);
                accepter.service();
            }
        }
    }

    static class Accepter implements Runnable {
        private SSLSocket socket;

        public Accepter(SSLSocket socket) {
            this.socket = socket;
        }

        public void service() {
            Thread thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run() {
            try (InputStream inputStream = socket.getInputStream();
                    InputStreamReader inputstreamreader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    OutputStream outputStream = socket.getOutputStream();
                    OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);) {
                try (BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                        BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);) {
                    String string = null;

                    while ((string = bufferedreader.readLine()) != null) {
                        System.out.println(string);
                        bufferedwriter.write(string);
                        bufferedwriter.write("\r\n");
                        bufferedwriter.flush();
                        System.out.println("resp:" + string);
                    }

                }

            } catch (IOException e) {
                // replace with other code
                e.printStackTrace();
            }
        }
    }
}
