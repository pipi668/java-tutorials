/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.socket.beginning;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * 
 * @author LiuJian
 * @date 2017年4月20日
 * 
 */
public class SocketConnectionStub {

    /**
     * @param args
     * @throws IOException
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        try (Socket socket = new Socket();) {
            socket.connect(new InetSocketAddress(InetAddress.getByName("115.159.1.65"), 80));
            // try (OutputStream output = socket.getOutputStream();) {
            // output.write(new byte[10]);
            // System.out.println("socket connected.");
            // }
        }
        System.out.println("socket stop.");
    }

}
