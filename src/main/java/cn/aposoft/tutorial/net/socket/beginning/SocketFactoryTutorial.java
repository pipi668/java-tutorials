package cn.aposoft.tutorial.net.socket.beginning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javax.net.SocketFactory;

import org.apache.commons.io.IOUtils;

public class SocketFactoryTutorial {

    /**
     * 客户端socket创建
     * 
     * @param args
     * @throws IOException
     * @throws UnknownHostException
     */
    public static void main(String[] args) {
        SocketFactory socketFactory = SocketFactory.getDefault();

        String inetAddress = "localhost";
        int port = 8080;
        try (Socket socket = socketFactory.createSocket(inetAddress, port);) {
            System.out.println("client: localAddress:" + socket.getLocalAddress() + ":" + socket.getLocalPort());
            System.out.println("client: remoteAddress:" + socket.getRemoteSocketAddress());
            // 默认 64kb receiveBuffer, sendBuffer
            String sayHello = "Hello, this is Alice.";
            System.out.println("client: receiver buffer size:" + socket.getReceiveBufferSize() + ", send buffer size:" + socket.getSendBufferSize());
            Reader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(sayHello);
            writer.flush();
            socket.shutdownOutput();
            System.out.println("client: " + sayHello);
            final String sayHi = IOUtils.toString(reader);
            System.out.println("Server answered: " + sayHi);
            socket.shutdownInput();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("socket is closed.");
        }

    }
}
