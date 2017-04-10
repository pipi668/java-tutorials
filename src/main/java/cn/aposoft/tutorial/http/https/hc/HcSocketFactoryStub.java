package cn.aposoft.tutorial.http.https.hc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HcSocketFactoryStub {
    private final static String CRLF = "\r\n";
    private final static Log log = LogFactory.getLog(HcSocketFactoryStub.class);
    private final static String REQUEST_HEADER = createRequestHeader();

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
        try (Socket socket = DefaultSSLSocketFactory.connectSocket("www.gomefinance.com.cn", 443);) {
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
                    // response(input);
                    response(reader);
                }
            }
        } catch (IOException e) {
            System.out.println();
            e.printStackTrace();
        } finally {
            System.out.println("system is shuting down.");
        }
    }

    private static void response(InputStream input) {
        System.out.println("read response...");
        System.out.println(input.getClass().getName()); // sun.security.ssl.AppInputStream
        long last = 0;
        byte[] buf = new byte[1024];
        int len;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while (true) {
            try {
                long curr = System.currentTimeMillis();
                if (last != 0 && (curr - last > 1000)) {
                    print(output);
                    output.reset();
                    break;
                }

                last = curr;

                len = input.read(buf);
                if (len != -1) {
                    output.write(buf, 0, len);
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            } catch (IOException e) {
                if (e instanceof java.net.SocketTimeoutException) {
                    print(output);
                } else {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    private static void print(ByteArrayOutputStream output) {
        byte[] bytes = output.toByteArray();
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

    private static void response(Reader reader) throws IOException {
        System.out.println("waiting response...");
        char[] buf = new char[1];
        try {
            while (reader.read(buf) != -1) {
                System.out.print(new String(buf));
            }
        } catch (IOException e) {
            if (e instanceof java.net.SocketTimeoutException) {
                throw e;
            } else {
                e.printStackTrace();
            }
        }
    }

}
