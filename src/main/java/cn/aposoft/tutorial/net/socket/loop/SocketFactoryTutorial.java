package cn.aposoft.tutorial.net.socket.loop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.net.SocketFactory;

import org.apache.commons.io.IOUtils;

/**
 * 当使用量达到16000以上时,windows 资源会被耗尽
 * 
 * @author LiuJian
 * @date 2017年4月7日
 *
 */
public class SocketFactoryTutorial {
    final static String inetAddress = "localhost";
    final static int SERVER_PORT = 8080;
    final static int CLIENT_PORT = 10000;
    final static InetAddress REMOTE_ADDRESS = createAddress(inetAddress);

    final static InetAddress LOCAL_ADDRESS = createAddress(inetAddress);

    final static SocketAddress REMOTE_SOCKET_ADDRESS = new InetSocketAddress(REMOTE_ADDRESS, SERVER_PORT);
    final static SocketAddress LOCAL_SOCKET_ADDRESS = new InetSocketAddress(LOCAL_ADDRESS, CLIENT_PORT);
    final static SocketFactory SOCKET_FACTORY = SocketFactory.getDefault();
    final static String sayHello = "Hello, this is Alice.";

    /**
     * 客户端socket创建
     * 
     * @param args
     * @throws IOException
     * @throws UnknownHostException
     */
    public static void main(String[] args) {
        System.out.println(new Date());
        for (int i = 0; i < 1000 * 100; i++) {
            System.out.println("i:" + i);
            testConnection();
            if (i % 1000 == 0) {
                System.out.println("connected:" + i);
            }

        }
        System.out.println(new Date());
    }

    public static void testConnection() {
        try (Socket socket = SOCKET_FACTORY.createSocket();) {
            // try (Socket socket = new Socket(REMOTE_ADDRESS, SERVER_PORT,
            // LOCAL_ADDRESS, CLIENT_PORT);) {
            socket.setReuseAddress(true);

            socket.connect(REMOTE_SOCKET_ADDRESS);
            Reader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(sayHello);
            writer.flush();
            socket.shutdownOutput();
            final String sayHi = IOUtils.toString(reader);
            socket.shutdownInput();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // System.out.println("socket is closed.");
        }
    }

    private static InetAddress createAddress(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

}
