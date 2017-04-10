/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.hc;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.x500.X500Principal;

/**
 * @author LiuJian
 * @date 2017年4月9日
 * 
 */
public class DefaultSSLSocketFactory {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static Socket connectSocket(final String remoteHost, final int remotePort) throws UnknownHostException, IOException {
        final SSLSocket sslsock = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket();
        final String[] allProtocols = sslsock.getSupportedProtocols();
        final List<String> enabledProtocols = new ArrayList<String>(allProtocols.length);
        for (final String protocol : allProtocols) {
            if (!protocol.startsWith("SSL")) {
                enabledProtocols.add(protocol);
            }
        }
        sslsock.setSoTimeout(500);
        SocketAddress endpoint = new InetSocketAddress(remoteHost, remotePort);
        sslsock.connect(endpoint);
        sslsock.startHandshake();
        verifyHostname(sslsock, remoteHost);
        return sslsock;
    }

    public static Socket connectSocket(final String remoteHost, final int remotePort, final InetSocketAddress localAddress) throws IOException {
        final Socket sock = createSocket();
        if (localAddress != null) {
            sock.bind(localAddress);
            System.out.println("sock bind.");
        }

        try {
            sock.connect(new InetSocketAddress(remoteHost, remotePort));
            System.out.println("Socket is connected:" + sock.isConnected());
            return createLayeredSocket(sock, remoteHost, remotePort);
        } catch (final IOException ex) {
            try {
                sock.close();
            } catch (final IOException ignore) {
            }
            throw ex;
        }

    }

    public static Socket createSocket() throws IOException {
        // Socket socket = SocketFactory.getDefault().createSocket();
        Socket socket = new SocketSniffer();

        socket.setSoTimeout(500);

        return socket;
    }

    public static Socket createLayeredSocket(final Socket socket, final String remoteHost, final int remotePort) throws IOException {
        final SSLSocket sslsock = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(socket, remoteHost, remotePort, true);
        final String[] allProtocols = sslsock.getSupportedProtocols();

        final List<String> enabledProtocols = new ArrayList<String>(allProtocols.length);
        for (final String protocol : allProtocols) {
            if (!protocol.startsWith("SSL")) {
                enabledProtocols.add(protocol);
            }
        }

        if (!enabledProtocols.isEmpty()) {
            sslsock.setEnabledProtocols(enabledProtocols.toArray(EMPTY_STRING_ARRAY));
        }
        // HandshakeCompletedListener listener = new
        // HandshakeCompletedListener() {
        // @Override
        // public void handshakeCompleted(HandshakeCompletedEvent event) {
        // sslsock.removeHandshakeCompletedListener(this);
        // }
        // };
        // sslsock.addHandshakeCompletedListener(listener);

        sslsock.startHandshake();
        verifyHostname(sslsock, remoteHost);
        return sslsock;
    }

    private static void verifyHostname(SSLSocket sslsock, String remoteHost) {
        try {
            SSLSession session = sslsock.getSession();
            if (session == null) {
                // In our experience this only happens under IBM 1.4.x when
                // spurious (unrelated) certificates show up in the server'
                // chain. Hopefully this will unearth the real problem:
                final InputStream in = sslsock.getInputStream();
                in.available();
                // If ssl.getInputStream().available() didn't cause an
                // exception, maybe at least now the session is available?
                session = sslsock.getSession();
                if (session == null) {
                    // If it's still null, probably a startHandshake() will
                    // unearth the real problem.
                    sslsock.startHandshake();
                    session = sslsock.getSession();
                }
            }
            if (session == null) {
                throw new SSLHandshakeException("SSL session not available");
            }

            try {
                final Certificate[] certs = session.getPeerCertificates();
                final X509Certificate x509 = (X509Certificate) certs[0];
                final X500Principal peer = x509.getSubjectX500Principal();

                final Collection<List<?>> altNames1 = x509.getSubjectAlternativeNames();
                if (altNames1 != null) {
                    final List<String> altNames = new ArrayList<String>();
                    for (final List<?> aC : altNames1) {
                        if (!aC.isEmpty()) {
                            altNames.add((String) aC.get(1));
                        }
                    }
                }

                final X500Principal issuer = x509.getIssuerX500Principal();

                final Collection<List<?>> altNames2 = x509.getIssuerAlternativeNames();
                if (altNames2 != null) {
                    final List<String> altNames = new ArrayList<String>();
                    for (final List<?> aC : altNames2) {
                        if (!aC.isEmpty()) {
                            altNames.add((String) aC.get(1));
                        }
                    }
                }
            } catch (final Exception ignore) {
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

}
