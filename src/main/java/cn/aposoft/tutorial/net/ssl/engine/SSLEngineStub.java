/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.ssl.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author LiuJian
 * @date 2017年4月11日
 * 
 */
public class SSLEngineStub {

    /**
     * @param args
     * @throws KeyStoreException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
            IOException, UnrecoverableKeyException, KeyManagementException {
        final String hostname = "www.gomefinance.com.cn";
        final int port = 443;
        // Create and initialize the SSLContext with key material
        char[] passphrase = "changeit".toCharArray();

        // First initialize the key and trust material
        KeyStore ksKeys = KeyStore.getInstance("PKCS12");
        ksKeys.load(new FileInputStream("f:/key/privateKey.p12"), passphrase);
        KeyStore ksTrust = KeyStore.getInstance("JKS");
        ksTrust.load(new FileInputStream("jssecacerts"), passphrase);

        // KeyManagers decide which key material to use
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ksKeys, passphrase);

        // TrustManagers decide whether to allow connections
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ksTrust);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        // Create the engine
        SSLEngine engine = sslContext.createSSLEngine(hostname, port);

        // Use as client
        engine.setUseClientMode(true);

        // Create a nonblocking socket channel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(hostname, port));

        // Complete connection
        while (!socketChannel.finishConnect()) {
            Thread.yield();
        }

        // Create byte buffers to use for holding application and encoded data
        SSLSession session = engine.getSession();
        ByteBuffer myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        ByteBuffer peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());

        // Do initial handshake
        doHandshake(socketChannel, engine, myNetData, peerNetData);

        myAppData.put("hello".getBytes());
        myAppData.flip();

        while (myAppData.hasRemaining()) {
            // Generate SSL/TLS encoded data (handshake or application data)
            SSLEngineResult res = engine.wrap(myAppData, myNetData);

            // Process status of call
            if (res.getStatus() == SSLEngineResult.Status.OK) {
                myAppData.compact();

                // Send SSL/TLS encoded data to peer
                while (myNetData.hasRemaining()) {
                    int num = socketChannel.write(myNetData);
                    if (num == 0) {
                        // no bytes written; try again later
                    }
                }
            }

            // Handle other status: BUFFER_OVERFLOW, CLOSED

            // Read SSL/TLS encoded data from peer
            int num = socketChannel.read(peerNetData);
            if (num == -1) {
                // The channel has reached end-of-stream
            } else if (num == 0) {
                // No bytes read; try again ...
            } else {
                // Process incoming data
                peerNetData.flip();
                res = engine.unwrap(peerNetData, peerAppData);

                if (res.getStatus() == SSLEngineResult.Status.OK) {
                    peerNetData.compact();

                    if (peerAppData.hasRemaining()) {
                        // Use peerAppData
                    }
                }
                // Handle other status: BUFFER_OVERFLOW, BUFFER_UNDERFLOW,
                // CLOSED

            }

            // Indicate that application is done with engine
            engine.closeOutbound();

            while (!engine.isOutboundDone()) {
                ByteBuffer empty = ByteBuffer.allocate(myNetData.capacity());
                // Get close message
                SSLEngineResult res1 = engine.wrap(empty , myNetData);

                // Check res statuses

                // Send close message to peer
                while (myNetData.hasRemaining()) {
                    int num1 = socketChannel.write(myNetData);
                    if (num1 == 0) {
                        // no bytes written; try again later
                    }
                    myNetData.compact();
                }
            }

            // Close transport
            socketChannel.close();
        }
    }

    private static void doHandshake(SocketChannel socketChannel, SSLEngine engine, ByteBuffer myNetData, ByteBuffer peerNetData) throws IOException {
        // Create byte buffers to use for holding application data
        int appBufferSize = engine.getSession().getApplicationBufferSize();
        ByteBuffer myAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);

        // Begin handshake
        engine.beginHandshake();
        SSLEngineResult.HandshakeStatus hs = engine.getHandshakeStatus();

        // Process handshaking message
        while (hs != SSLEngineResult.HandshakeStatus.FINISHED && hs != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            switch (hs) {
            default:
                break;
            case NEED_UNWRAP:
                // Receive handshaking data from peer
                if (socketChannel.read(peerNetData) < 0) {
                    // The channel has reached end-of-stream
                }

                // Process incoming handshaking data
                peerNetData.flip();
                SSLEngineResult res = engine.unwrap(peerNetData, peerAppData);
                peerNetData.compact();
                hs = res.getHandshakeStatus();

                // Check status
                switch (res.getStatus()) {
                case OK:
                    // Handle OK status
                    break;

                // Handle other status: BUFFER_UNDERFLOW, BUFFER_OVERFLOW,
                // CLOSED
                default:
                    break;
                }
                break;

            case NEED_WRAP:
                // Empty the local network packet buffer.
                myNetData.clear();

                // Generate handshaking data
                res = engine.wrap(myAppData, myNetData);
                hs = res.getHandshakeStatus();

                // Check status
                switch (res.getStatus()) {
                case OK:
                    myNetData.flip();

                    // Send the handshaking data to peer
                    while (myNetData.hasRemaining()) {
                        socketChannel.write(myNetData);
                    }
                    break;

                // Handle other status: BUFFER_OVERFLOW, BUFFER_UNDERFLOW,
                // CLOSED
                default:
                    break;
                }
                break;

            case NEED_TASK:
                // Handle blocking tasks
                break;

            // Handle other status: // FINISHED or NOT_HANDSHAKING

            }
        }

        // Processes after handshaking

    }

}
