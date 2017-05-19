/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.socket.beginning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javax.net.SocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;

import org.apache.commons.io.IOUtils;

/**
 * @author LiuJian
 * @date 2017年5月10日
 * 
 */
public class SocketHttp {
    final HandshakeCompletedListener listener = new HandshakeCompletedListener() {

        @Override
        public void handshakeCompleted(HandshakeCompletedEvent paramHandshakeCompletedEvent) {
            System.out.println("handshake successful...");
        }
    };

    /**
     * @param args
     */
    public static void main(String[] args) {

        System.setProperty("javax.net.debug", "all");
        try (Socket socket = SocketFactory.getDefault().createSocket("www.aposoft.cn", 80);) {
            workWith(socket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("结束.");
    }

    private static void workWith(Socket socket) {
        try {
            System.out.println(REQUEST_STRING);
            BufferedReader reader = IOUtils.buffer(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            OutputStream output = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            writer.write(REQUEST_STRING);
            writer.flush();
            // writer.close();
            System.out.println("output finished.");

            int i = 0;
            System.out.println("reading...");
            while (i < 1000) {
                if (reader.ready()) {
                    String line = reader.readLine();
                    if (line != null) {
                        System.out.println(line);
                        continue;
                    } else {
                        break;
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

        builder.append("Host: www.aposoft.cn").append(CRLF);

        builder.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64;rv:53.0) Gecko/20100101 Firefox/52.0").append(CRLF);

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
