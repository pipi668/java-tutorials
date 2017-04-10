/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.ssl.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ServerSocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.apache.commons.io.IOUtils;

/**
 * @author LiuJian
 * @date 2017年4月7日
 * 
 */
public class SSLServerSocketTutorial {
    private static final AtomicLong longSeeds = new AtomicLong(0);
    private static volatile boolean isRunning = true;
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    final static int SERVER_PORT = 18443;

    /**
     * @param args
     */
    public static void main(String[] args) {
        final HandshakeCompletedListener listener = new HandshakeCompletedListener() {

            @Override
            public void handshakeCompleted(HandshakeCompletedEvent paramHandshakeCompletedEvent) {
                System.out.println("handshake successful...");
            }
        };
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();

                try (SSLServerSocket sslServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(SERVER_PORT);) {

                    System.out.println("EnabledCipherSuites:" + Arrays.toString(sslServerSocket.getEnabledCipherSuites()));
                    System.out.println(sslServerSocket.getClass().getName());
                    System.out.println("Seversocket is waiting in board...");

                    while (isRunning) {
                        SSLSocket socket = (SSLSocket) sslServerSocket.accept();
                        System.out.println("Socket accepted...");
                        System.out.println("clientMode:" + socket.getUseClientMode());
                        socket.addHandshakeCompletedListener(listener);
                        executor.execute(new SSLHandShakeWorker(socket));
                        // executor.execute(new Worker(socket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        try {
            while ((System.in.read()) != 'q') {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = false;
        executor.shutdown();

    }

    private static final class SSLHandShakeWorker implements Runnable {
        private SSLSocket socket;
        final String sayHi = "Hello, this is Bob.";

        public SSLHandShakeWorker(SSLSocket socket) {
            socket.setUseClientMode(false);
            this.socket = socket;

        }

        @Override
        public void run() {

        }
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

                Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                Reader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String clientSayHello = IOUtils.toString(reader);
                System.out.println("client:" + clientSayHello);
                socket.shutdownInput();
                writer.write(sayHi);
                writer.flush();
                socket.shutdownOutput();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                int i = 0;

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
