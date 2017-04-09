/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.hc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiuJian
 * @date 2017年4月9日
 * 
 */
public class SocketSniffer extends Socket {
    static final Logger logger = LoggerFactory.getLogger(SocketSniffer.class);

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        logger.debug("connected to " + endpoint);
        super.connect(endpoint);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        logger.debug("connected to " + endpoint + "," + timeout);
        super.connect(endpoint, timeout);
    }

    @Override
    public void bind(SocketAddress bindpoint) throws IOException {
        logger.debug("bind to " + bindpoint);
        super.bind(bindpoint);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new SnifferInputStream(super.getInputStream());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new SnifferOutputStream(super.getOutputStream());
    }

    static class SnifferOutputStream extends OutputStream {
        private OutputStream output;

        public SnifferOutputStream(OutputStream output) {
            this.output = output;
        }

        byte[] inbuf = new byte[1024];
        int insize = 0;
        long inTime = 0;

        byte[] outbuf = new byte[1024];
        int outsize = 0;
        long outTime = 0;

        private void flushIn() {
            long curr = System.currentTimeMillis();
            if (inTime != 0 && curr - inTime > 1 || insize == 1024) {
                System.out.println("Before print In");
                for (int i = 0; i < insize; i++) {
                    System.out.print((char) inbuf[i]);
                }
                insize = 0;
                System.out.println("After print In");
            }
            inTime = curr;
        }

        private void flushOut() {
            long curr = System.currentTimeMillis();
            if (outTime != 0 && curr - outTime > 1 || outsize == 1024) {
                System.out.println("Before print In");
                for (int i = 0; i < outsize; i++) {
                    System.out.print((char) outbuf[i]);
                }
                outsize = 0;
                System.out.println("After print In");
            }
            outTime = curr;
        }

        public void flushbuf(int b) {
            flushIn();
            flushOut();

            outbuf[insize++] = (byte) (b & 0xFF);
            if (outsize == 1024) {
                outsize = 0;
            }
        }

        @Override
        public void write(int b) throws IOException {
            // flushbuf(b);
            output.write(b);
        }

    }

    static class SnifferInputStream extends InputStream {
        private InputStream input;

        public SnifferInputStream(InputStream input) {
            this.input = input;
        }

        byte[] inbuf = new byte[1024];
        int insize = 0;
        long inTime = 0;

        byte[] outbuf = new byte[1024];
        int outsize = 0;
        long outTime = 0;

        private void flushIn(long curr) {
            if (inTime != 0 && curr - inTime > 1 || insize == 1024) {
                System.out.println("Before print In");
                for (int i = 0; i < insize; i++) {
                    System.out.print((char) inbuf[i]);
                }
                insize = 0;
                System.out.println("After print In");
            }
            inTime = curr;
        }

        private void flushOut(long curr) {
            if (outTime != 0 && curr - outTime > 1 || outsize == 1024) {
                System.out.println("Before print In");
                for (int i = 0; i < outsize; i++) {
                    System.out.print((char) outbuf[i]);
                }
                outsize = 0;
                System.out.println("After print In");
            }
            outTime = curr;
        }

        public void flushbuf(int i) {
            long curr = System.currentTimeMillis();
            flushIn(curr);
            flushOut(curr);
            inbuf[insize++] = (byte) (i & 0xFF);
            if (insize == 1024) {
                insize = 0;

            }
        }

        @Override
        public int read() throws IOException {

            int i = input.read();
            // flushbuf(i);

            return i;
        }
    }
}
