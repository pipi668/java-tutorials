/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.ssl.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.HttpClients;

/**
 * @author LiuJian
 * @date 2017年4月7日
 * 
 */
public class DefaultSSLSocketTutorial {

    /**
     * @param args
     */
    public static void main(String[] args) {

        HttpClients.createMinimal();
        SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (SSLSocket sslSocket = (SSLSocket) sslFactory.createSocket("www.gomefinance.com.cn", 443);) {
            // System.out.println("SupportedCipherSuites:" +
            // Arrays.toString(sslSocket.getSupportedCipherSuites()));
            // System.out.println("EnabledCipherSuites:" +
            // Arrays.toString(sslSocket.getEnabledCipherSuites()));
            // System.out.println("SupportedProtocols:" +
            // Arrays.toString(sslSocket.getSupportedProtocols()));
            sslSocket.setEnabledProtocols(new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" });
            System.out.println("EnabledProtocols:" + Arrays.toString(sslSocket.getEnabledProtocols()));
            // 可以设置更高级的Protocol
            System.out.println("UseClientMode:" + sslSocket.getUseClientMode());
            // useful in server mode
            System.out.println("WantClientAuth:" + sslSocket.getWantClientAuth());

            // useful in server mode
            System.out.println("NeedClientAuth:" + sslSocket.getNeedClientAuth());
            sslSocket.startHandshake();
            System.out.println();

            SSLSession session = sslSocket.getSession();
            System.out.println("CipherSuite:" + session.getCipherSuite());
            System.out.println("Creation Time:" + session.getCreationTime());
            System.out.println("LastAccessedTime:" + session.getLastAccessedTime());
            System.out.println("Protocol:" + session.getProtocol());

            System.out.println("LocalPrincipal:" + session.getLocalPrincipal());
            System.out.println("LocalCertificates:" + session.getLocalCertificates());
            System.out.println("" + session.getPeerHost() + ":" + session.getPeerPort());
            System.out.println("PeerPrincipal:" + session.getPeerPrincipal());

            System.in.read();
            // System.out.println("PeerCertificateChain:\r\n" +
            // Arrays.toString(session.getPeerCertificateChain()));
            workWith(sslSocket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void workWith(SSLSocket sslSocket) {
        try {
            System.out.println(REQUEST_STRING);
            BufferedReader reader = IOUtils.buffer(new InputStreamReader(sslSocket.getInputStream(), StandardCharsets.UTF_8));
            OutputStream output = sslSocket.getOutputStream();
            Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            writer.write(REQUEST_STRING);
            writer.flush();
            writer.close();
            System.out.println("output finished.");

            int length = 0;
            int i = 0;
            while (length < 1000 * 20 && i < 1000) {
                System.out.println("reading...");
                if (reader.ready()) {
                    System.out.println("ready...");
                    String line = reader.readLine();
                    System.out.println(line);
                    if (line != null) {
                        length += line.length();
                    }
                }
                try {
                    Thread.sleep(1000 * 5);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    final static String CRLF = "\r\n";
    final static String REQUEST_STRING = requestString();

    private static String requestString() {
        StringBuilder builder = new StringBuilder();

        builder.append("GET / HTTP/1.1").append(CRLF);

        builder.append("Host: www.gomefinance.com.cn").append(CRLF);

        // builder.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64;
        // rv:52.0) Gecko/20100101 Firefox/52.0").append(CRLF);

        // builder.append("Accept:
        // text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").append(CRLF);

        // builder.append("Accept-Language: en-US,en;q=0.5").append(CRLF);

        // builder.append("Accept-Encoding: gzip, deflate, br").append(CRLF);

        // builder.append(
        // "Cookie:
        // Hm_lvt_d2b8fab2102e67a1f1d764426d5a7a90=1476094823,1477201928;
        // ag_fid=NMNSYUSAtFaTRbXF;
        // _jzqa=1.332683160808858800.1470632489.1476094823.1477201928.7;
        // ag_fid=NMNSYUSAtFaTRbXF;
        // _qzja=1.1883280400.1470632488561.1476094822786.1477201927630.1477202691837.1477202695387.0.0.0.15.7;
        // Hm_lvt_39c55f18cef6a922d74d142f566eaf63=1491500453; d=oa;
        // u=MwAyADcAMABmADkAZgA5AC0AOAA3ADEAMwAtADQAYgA3ADEALQA5ADcAMgA1AC0AOAAwADkAZAA5AGMAMABlADcAZgBmAGYAfAAyADAAMQA3ADAANAAwADYAMQAzADEAOQAzADQAfABkAGIAYwA5AGYAYQAzADUALQA0AGUAMwBlAC0ANABiADEAYQAtADkAZgA1ADcALQAwADkANgA0ADIAYQAyADQAOAA4ADAANAB8ADUANQAyADMAOAA3ADgAMwA3AGIAMAAzADIANwBiADgANQBhAGEAYwBmAGYAYgA3AGIAOABhADQAMQA4ADYAMAA=;
        // Hm_lpvt_39c55f18cef6a922d74d142f566eaf63=1491502414;
        // __ag_cm_=g1t1d1b2; BIGipServergomemyc.com=3492908810.20480.0000")
        // .append(CRLF);
        // builder.append("Connection: keep-alive").append(CRLF);
        //
        // builder.append("Upgrade-Insecure-Requests: 1").append(CRLF);

        // builder.append("Pragma: no-cache").append(CRLF);

        // builder.append("Cache-Control: no-cache").append(CRLF);
        builder.append(CRLF);
        return builder.toString();
    }
}
