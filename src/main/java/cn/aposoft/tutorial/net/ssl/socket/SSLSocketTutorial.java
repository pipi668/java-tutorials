/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.ssl.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author LiuJian
 * @date 2017年4月7日
 * 
 */
public class SSLSocketTutorial {
    final static String inetAddress = "localhost";
    final static int SERVER_PORT = 18443;
    final static InetAddress REMOTE_ADDRESS = createAddress(inetAddress);
    final static SocketAddress REMOTE_SOCKET_ADDRESS = new InetSocketAddress(REMOTE_ADDRESS, SERVER_PORT);

    private static InetAddress createAddress(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("javax.net.debug", "all,ssl,handshake");
        SocketFactory factory = SSLSocketFactory.getDefault();
        if (factory instanceof SSLSocketFactory) {
            SSLSocketFactory sslFactory = (SSLSocketFactory) factory;
            try (SSLSocket socket = (SSLSocket) sslFactory.createSocket();) {
                System.out.println("EnabledCipherSuites:" + Arrays.toString(socket.getEnabledCipherSuites()));
                socket.setEnabledProtocols(new String[] { "TLSv1.2" });
                socket.setUseClientMode(true);
                socket.connect(REMOTE_SOCKET_ADDRESS);
                socket.startHandshake();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
