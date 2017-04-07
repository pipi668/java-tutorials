package cn.aposoft.tutorial.http.https.hc;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.x500.X500Principal;

import org.apache.http.HttpHost;
import org.apache.http.config.SocketConfig;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class HcSocketFactoryStub {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

    }

    private final javax.net.ssl.SSLSocketFactory socketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    private final static String[] EMPTY_STRING_ARRAY = new String[0];

    public Socket createSocket(final HttpContext context) throws IOException {
        return SocketFactory.getDefault().createSocket();
    }

    public Socket prepareSocket(Socket socket, final SocketConfig socketConfig) throws IOException {

        socket.setSoTimeout(socketConfig.getSoTimeout());
        socket.setReuseAddress(socketConfig.isSoReuseAddress());
        socket.setTcpNoDelay(socketConfig.isTcpNoDelay());
        socket.setKeepAlive(socketConfig.isSoKeepAlive());
        if (socketConfig.getRcvBufSize() > 0) {
            socket.setReceiveBufferSize(socketConfig.getRcvBufSize());
        }
        if (socketConfig.getSndBufSize() > 0) {
            socket.setSendBufferSize(socketConfig.getSndBufSize());
        }

        final int linger = socketConfig.getSoLinger();
        if (linger >= 0) {
            socket.setSoLinger(true, linger);
        }
        return socket;
    }

    public Socket connectSocket(final int connectTimeout, final Socket socket, final HttpHost host, final InetSocketAddress remoteAddress,
            final InetSocketAddress localAddress, final HttpContext context) throws IOException {
        Args.notNull(host, "HTTP host");
        Args.notNull(remoteAddress, "Remote address");
        final Socket sock = socket != null ? socket : createSocket(context);
        if (localAddress != null) {
            sock.bind(localAddress);
        }
        try {
            if (connectTimeout > 0 && sock.getSoTimeout() == 0) {
                sock.setSoTimeout(connectTimeout);
            }

            sock.connect(remoteAddress, connectTimeout);
        } catch (final IOException ex) {
            try {
                sock.close();
            } catch (final IOException ignore) {
            }
            throw ex;
        }
        // Setup SSL layering if necessary
        if (sock instanceof SSLSocket) {
            final SSLSocket sslsock = (SSLSocket) sock;
            sslsock.startHandshake();
            verifyHostname(sslsock, host.getHostName());
            return sock;
        } else {
            // create SSLSocket over PlainSocket
            return createLayeredSocket(sock, host.getHostName(), remoteAddress.getPort(), context);
        }
    }

    public Socket createLayeredSocket(final Socket socket, final String target, final int port, final HttpContext context) throws IOException {
        final SSLSocket sslsock = (SSLSocket) this.socketfactory.createSocket(socket, target, port, true);
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

        sslsock.startHandshake();
        verifyHostname(sslsock, target);
        return sslsock;
    }

    private void verifyHostname(final SSLSocket sslsock, final String hostname) throws IOException {
        try {
            SSLSession session = sslsock.getSession();
            if (session == null) {

                final InputStream in = sslsock.getInputStream();
                in.available();

                session = sslsock.getSession();
                if (session == null) {

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

        } catch (final IOException iox) {
            // close the socket before re-throwing the exception
            try {
                sslsock.close();
            } catch (final Exception x) {
                /* ignore */ }
            throw iox;
        }
    }
}
