/**
 *   Copyright  :  www.aposoft.cn
 */
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.apache.commons.io.IOUtils;

/**
 * @author LiuJian
 * @date 2017年4月6日
 * 
 */
public class ServerSocketFactoryTutorial {
    final static String inetAddress = "localhost";
    final static int port = 8080;
    final static InetAddress REMOTE_ADDRESS = createAddress(inetAddress);
    final static SocketAddress LISTEN_SOCKET_ADDRESS = new InetSocketAddress(REMOTE_ADDRESS, port);
    final static SocketFactory SOCKET_FACTORY = SocketFactory.getDefault();
    final static String sayHello = "Hello, this is Alice.";

    private static volatile boolean isRunning = true;
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final AtomicLong longSeeds = new AtomicLong(0);

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

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
                try {
                    // return a server socket bound to the port 8080
                    final ServerSocket serverSocket = serverSocketFactory.createServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(LISTEN_SOCKET_ADDRESS);
                    System.out.println("Seversocket is waiting in board...");
                    while (isRunning) {
                        // System.out.println("Seversocket is waiting
                        // inbound...");
                        Socket socket = serverSocket.accept();
                        // System.out.println("New Socket is accepted.");
                        executor.execute(new Worker(socket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        int recv = -1;
        try {
            while ((recv = System.in.read()) != 'q') {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = false;
        executor.shutdown();
    }

    private static final class Worker implements Runnable {
        private Socket socket;
        final String sayHi = "Hello, this is Bob.";

        public Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            long curr = longSeeds.incrementAndGet();
            try {

                // System.out.println("localAddress:" + socket.getLocalAddress()
                // + ":" + socket.getLocalPort());
                // System.out.println("remoteAddress:" +
                // socket.getRemoteSocketAddress());
                // 默认 64kb receiveBuffer, sendBuffer
                // System.out.println("receiver buffer size:" +
                // socket.getReceiveBufferSize() + ", send buffer size:" +
                // socket.getSendBufferSize());
                // if (socket.getKeepAlive()) {
                // System.out.println("server " + curr + ": connection is
                // persistent.");
                // }
                Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                Reader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String clientSayHello = IOUtils.toString(reader);
                socket.shutdownInput();
                // System.out.println("Server received :" + clientSayHello);
                writer.write(sayHi);
                writer.flush();
                socket.shutdownOutput();
                // System.out.println("Server answered: " + sayHi);
            } catch (SocketException e) {
                e.printStackTrace();
                // System.out.println("remote close the socket.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                int i = 0;
                // while (!socket.isClosed() && i < 1000 * 10) {
                // i++;
                // try {
                // Thread.sleep(1);
                // } catch (InterruptedException e) {
                // e.printStackTrace();
                // }
                // }
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("server " + curr + ": after" + i + "millisecond(s), socket is closed.");
            }
        }
    }
}
