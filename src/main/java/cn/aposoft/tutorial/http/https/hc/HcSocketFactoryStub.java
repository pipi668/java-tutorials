package cn.aposoft.tutorial.http.https.hc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.x500.X500Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HcSocketFactoryStub {
    private final static String CRLF = "\r\n";
    private final static String REQUEST_HEADER = createRequestHeader();
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final static Log log = LogFactory.getLog(HcSocketFactoryStub.class);

    public static String createRequestHeader() {
        StringBuilder builder = new StringBuilder();
        builder.append("GET / HTTP/1.1").append(CRLF);
        builder.append("Host: www.gomefinance.com.cn").append(CRLF);
        builder.append("Connection: Keep-Alive").append(CRLF);
        builder.append("User-Agent: Apache-HttpClient/4.5.3 (Java/1.7.0_80)").append(CRLF)//
                .append(CRLF);
        return builder.toString();
    }

    /**
     * www.gomefinance.com.cn 10.141.4.154 fen.gomemyf.com [118.26.23.108]
     * 
     * @param args
     */
    public static void main(String[] args) {
        try (Socket socket = connectSocket("www.gomefinance.com.cn", 443, new InetSocketAddress(0));) {
            // System.in.read();
            System.out.println("socket created...");
            try (OutputStream output = socket.getOutputStream(); InputStream input = socket.getInputStream();) {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
                        Reader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));) {
                    System.out.println(output.getClass().getName());

                    System.out.println("write " + REQUEST_HEADER);
                    writer.write(REQUEST_HEADER);
                    writer.flush();
                    System.out.println("write finished.");
                    response(input);
                    // response(reader);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void response(InputStream input) {
        System.out.println("read response...");
        System.out.println(input.getClass().getName()); // sun.security.ssl.AppInputStream

        int i;
        while (true) {
            try {
                if (input.available() > 0) {
                    i = input.read();
                    if (i != -1) {
                        System.out.print('[' + i + ']');
                    } else {
                        break;
                    }
                } else {
                    Thread.yield();
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private static void response(Reader reader) {
        System.out.println("waiting response...");
        int c = -1;
        try {
            while ((c = reader.read()) != -1) {
                System.out.print(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Socket connectSocket(final String remoteHost, final int remotePort, final InetSocketAddress localAddress) throws IOException {
        final Socket sock = createSocket();
        if (localAddress != null) {
            sock.bind(localAddress);
        }
        System.out.println("sock bind.");
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
        HandshakeCompletedListener listener = new HandshakeCompletedListener() {

            @Override
            public void handshakeCompleted(HandshakeCompletedEvent event) {
                System.out.println("handshake successful.");
                try {
                    System.out.println(event.getPeerCertificates());
                    System.out.println(event.getPeerPrincipal());
                    System.out.println(event.getCipherSuite());

                } catch (SSLPeerUnverifiedException e) {
                    e.printStackTrace();
                }
                sslsock.removeHandshakeCompletedListener(this);
                System.out.println("listener is removed.");
            }

        };
        sslsock.addHandshakeCompletedListener(listener);
        System.out.println("before hand shake.");
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

            if (log.isDebugEnabled()) {
                log.debug("Secure session established");
                log.debug(" negotiated protocol: " + session.getProtocol());
                log.debug(" negotiated cipher suite: " + session.getCipherSuite());

                try {

                    final Certificate[] certs = session.getPeerCertificates();
                    final X509Certificate x509 = (X509Certificate) certs[0];
                    final X500Principal peer = x509.getSubjectX500Principal();

                    log.debug(" peer principal: " + peer.toString());
                    final Collection<List<?>> altNames1 = x509.getSubjectAlternativeNames();
                    if (altNames1 != null) {
                        final List<String> altNames = new ArrayList<String>();
                        for (final List<?> aC : altNames1) {
                            if (!aC.isEmpty()) {
                                altNames.add((String) aC.get(1));
                            }
                        }
                        log.debug(" peer alternative names: " + altNames);
                    }

                    final X500Principal issuer = x509.getIssuerX500Principal();
                    log.debug(" issuer principal: " + issuer.toString());
                    final Collection<List<?>> altNames2 = x509.getIssuerAlternativeNames();
                    if (altNames2 != null) {
                        final List<String> altNames = new ArrayList<String>();
                        for (final List<?> aC : altNames2) {
                            if (!aC.isEmpty()) {
                                altNames.add((String) aC.get(1));
                            }
                        }
                        log.debug(" issuer alternative names: " + altNames);
                    }
                } catch (final Exception ignore) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }

}
