/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.net.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

/**
 * <pre>
 Ethernet Protocol
 * 
 * </pre>
 * 
 * 
 * <pre>
 * IP Protocol

    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |Version|  IHL  |Type of Service|          Total Length         |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |         Identification        |Flags|      Fragment Offset    |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Time to Live |    Protocol   |         Header Checksum       |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                       Source Address                          |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Destination Address                        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Options                    |    Padding    |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

                    Example Internet Datagram Header

                               Figure 4.

  Note that each tick mark represents one bit position.
 * </pre>
 * 
 * <pre>
 *  TCP Protocol
    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |          Source Port          |       Destination Port        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                        Sequence Number                        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Acknowledgment Number                      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Data |           |U|A|P|R|S|F|                               |
   | Offset| Reserved  |R|C|S|S|Y|I|            Window             |
   |       |           |G|K|H|T|N|N|                               |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |           Checksum            |         Urgent Pointer        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Options                    |    Padding    |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                             data                              |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

                            TCP Header Format
 * 
 * </pre>
 * 
 * @author LiuJian
 * @date 2017年4月20日
 * 
 */
public class EthernetIpTcpParser {
    final static int MAC_ADDRESS_LENGTH = 6;
    final static int ETHERNET_PROTOCOL_LENGTH = 2;

    /**
     * @param args
     * @throws IOException
     * @throws DecoderException
     */
    public static void main(String[] args) throws IOException, DecoderException {
        System.out.println("0");
        parseFirstSyn();
        System.out.println("1");
        parseFirstAck();
        System.out.println("2");
        parseSecondAck();
        System.out.println("3");
        parseFirstFin();
        System.out.println("4");
        parseFirstFin2();
        System.out.println("5");
        parseFirstFin3();
    }

    /**
     * TCP 3次握手 第1次
     * 
     * @throws IOException
     * @throws DecoderException
     */
    public static void parseFirstSyn() throws IOException, DecoderException {
        try (InputStream input = ClassLoader.getSystemResourceAsStream("cn/aposoft/tutorial/net/tcp/tcp_handshake_0.raw");) {
            byte[] buffer = new byte[1024];
            int length = IOUtils.read(input, buffer);
            System.out.println("TCP HANDSHAKE STEP 1 -- TOTAL FRAME SIZE:\t" + length);
            System.out.println();
            int offset = 0;
            String encoded = Hex.encodeHexString(Arrays.copyOfRange(buffer, 0, length));
            offset = printEthernetHeader(encoded, offset);

            System.out.println();
            offset = printIpHeader(encoded, offset);
            System.out.println();
            offset = printTcpHeader(encoded, offset);

        }
    }

    /**
     * TCP 3次握手 第2次
     * 
     * @throws IOException
     * @throws DecoderException
     */
    public static void parseFirstAck() throws IOException, DecoderException {
        try (InputStream input = ClassLoader.getSystemResourceAsStream("cn/aposoft/tutorial/net/tcp/tcp_handshake_1.raw");) {
            byte[] buffer = new byte[1024];
            int length = IOUtils.read(input, buffer);
            System.out.println("TCP HANDSHAKE STEP 1 -- TOTAL FRAME SIZE:\t" + length);
            System.out.println();
            int offset = 0;
            String encoded = Hex.encodeHexString(Arrays.copyOfRange(buffer, 0, length));
            offset = printEthernetHeader(encoded, offset);

            System.out.println();
            offset = printIpHeader(encoded, offset);
            System.out.println();
            offset = printTcpHeader(encoded, offset);

        }
    }

    /**
     * TCP 3次握手 第3次
     * 
     * @throws IOException
     * @throws DecoderException
     */
    public static void parseSecondAck() throws IOException, DecoderException {
        try (InputStream input = ClassLoader.getSystemResourceAsStream("cn/aposoft/tutorial/net/tcp/tcp_handshake_2.raw");) {
            byte[] buffer = new byte[1024];
            int length = IOUtils.read(input, buffer);
            System.out.println("TCP HANDSHAKE STEP 1 -- TOTAL FRAME SIZE:\t" + length);
            System.out.println();
            int offset = 0;
            String encoded = Hex.encodeHexString(Arrays.copyOfRange(buffer, 0, length));
            offset = printEthernetHeader(encoded, offset);

            System.out.println();
            offset = printIpHeader(encoded, offset);
            System.out.println();
            offset = printTcpHeader(encoded, offset);

        }
    }

    /**
     * TCP 3次握手 第3次
     * 
     * @throws IOException
     * @throws DecoderException
     */
    public static void parseFirstFin() throws IOException, DecoderException {
        try (InputStream input = ClassLoader.getSystemResourceAsStream("cn/aposoft/tutorial/net/tcp/tcp_handshake_3.raw");) {
            byte[] buffer = new byte[1024];
            int length = IOUtils.read(input, buffer);
            System.out.println("TCP HANDSHAKE STEP 1 -- TOTAL FRAME SIZE:\t" + length);
            System.out.println();
            int offset = 0;
            String encoded = Hex.encodeHexString(Arrays.copyOfRange(buffer, 0, length));
            offset = printEthernetHeader(encoded, offset);

            System.out.println();
            offset = printIpHeader(encoded, offset);
            System.out.println();
            offset = printTcpHeader(encoded, offset);

        }
    }

    /**
     * TCP 3次握手 第3次
     * 
     * @throws IOException
     * @throws DecoderException
     */
    public static void parseFirstFin2() throws IOException, DecoderException {
        try (InputStream input = ClassLoader.getSystemResourceAsStream("cn/aposoft/tutorial/net/tcp/tcp_handshake_4.raw");) {
            byte[] buffer = new byte[1024];
            int length = IOUtils.read(input, buffer);
            System.out.println("TCP HANDSHAKE STEP 1 -- TOTAL FRAME SIZE:\t" + length);
            System.out.println();
            int offset = 0;
            String encoded = Hex.encodeHexString(Arrays.copyOfRange(buffer, 0, length));
            offset = printEthernetHeader(encoded, offset);

            System.out.println();
            offset = printIpHeader(encoded, offset);
            System.out.println();
            offset = printTcpHeader(encoded, offset);

        }
    }

    /**
     * TCP 3次握手 第3次
     * 
     * @throws IOException
     * @throws DecoderException
     */
    public static void parseFirstFin3() throws IOException, DecoderException {
        try (InputStream input = ClassLoader.getSystemResourceAsStream("cn/aposoft/tutorial/net/tcp/tcp_handshake_5.raw");) {
            byte[] buffer = new byte[1024];
            int length = IOUtils.read(input, buffer);
            System.out.println("TCP HANDSHAKE STEP 1 -- TOTAL FRAME SIZE:\t" + length);
            System.out.println();
            int offset = 0;
            String encoded = Hex.encodeHexString(Arrays.copyOfRange(buffer, 0, length));
            offset = printEthernetHeader(encoded, offset);

            System.out.println();
            offset = printIpHeader(encoded, offset);
            System.out.println();
            offset = printTcpHeader(encoded, offset);

        }
    }

    /**
     * 
     * @param encoded
     * @param initOffset
     * @return
     * @throws DecoderException
     */
    private static int printTcpHeader(String encoded, final int initOffset) throws DecoderException {
        int offset = initOffset;
        System.out.println("TCP HEADER:");
        offset = printPort("Source Port", encoded, offset);
        offset = printPort("Destination Port", encoded, offset);
        offset = printSequenceNumber("Sequence Number", encoded, offset);
        offset = printSequenceNumber("Acknowledgment Number", encoded, offset);
        offset = printTcpDataOffset(encoded, offset);
        offset = printReserved(encoded, offset);
        offset = printControlBits(encoded, offset);
        offset = printWindow(encoded, offset);
        offset = printChecksum(encoded, offset);
        offset = printUrgentPointer(encoded, offset);
        offset = printOptions(encoded, offset);
        return 0;
    }

    private static int printOptions(String encoded, final int initOffset) throws DecoderException {
        System.out.println("Options:\t\t" + encoded.substring(initOffset));
        return encoded.length() - 1;
    }

    private static int printUrgentPointer(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Urgent Pointer:\t\t");
        System.out.println(getShort(Hex.decodeHex(encoded.substring(initOffset, initOffset + 4).toCharArray())));
        return initOffset + 4;
    }

    private static int printChecksum(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Checksum:\t\t");
        System.out.println(getShort(Hex.decodeHex(encoded.substring(initOffset, initOffset + 4).toCharArray())));
        return initOffset + 4;
    }

    /**
     * TCP Window
     * 
     * @param encoded
     * @param offset
     * @return
     * @throws DecoderException
     */
    private static int printWindow(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Window:\t\t\t");
        System.out.println(getShort(Hex.decodeHex(encoded.substring(initOffset, initOffset + 4).toCharArray())));
        return initOffset + 4;
    }

    /**
     * TCP ControlBits
     * 
     * @param encoded
     * @param offset
     * @return
     * @throws DecoderException
     */
    private static int printControlBits(String encoded, final int initOffset) throws DecoderException {
        byte[] bytes = Hex.decodeHex(encoded.substring(initOffset, initOffset + 2).toCharArray());
        System.out.println("Control Bits:\t\t");
        boolean urg = (bytes[0] & 0b0010_0000) > 0;
        boolean ack = (bytes[0] & 0b0001_0000) > 0;
        boolean psh = (bytes[0] & 0b0000_1000) > 0;
        boolean rst = (bytes[0] & 0b0000_0100) > 0;
        boolean syn = (bytes[0] & 0b0000_0010) > 0;
        boolean fin = (bytes[0] & 0b0000_0001) > 0;
        System.out.println("URG:　" + urg + ",ACK:" + ack + ",PSH:" + psh + ",RST:" + rst + ",SYN:" + syn + ",FIN:" + fin);

        return initOffset + 2;
    }

    private static int printReserved(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Reserved:\t\t");
        byte[] bytes = Hex.decodeHex(encoded.substring(initOffset, initOffset + 4).toCharArray());
        String reserved = Integer.toBinaryString((((bytes[0] & 0x0F) << 2) | (bytes[1] & 0b11000000) >> 6));
        System.out.printf("0b%s", reserved);
        System.out.println();
        return initOffset + 2;
    }

    private static int printTcpDataOffset(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Data Offset:\t\t");
        byte[] bytes = Hex.decodeHex(encoded.substring(initOffset, initOffset + 2).toCharArray());

        System.out.print(((bytes[0] & 0xF0) >> 4) + " (* 4 bytes)");
        System.out.println();
        return initOffset;
    }

    /**
     * 
     * @param desc
     * @param encoded
     * @param initOffset
     *            初始偏移量
     * @return
     * @throws DecoderException
     */
    private static int printSequenceNumber(String desc, String encoded, final int initOffset) throws DecoderException {
        System.out.print(desc);
        System.out.print(":");
        int loop = 3 - (desc.length() + 1) / 8;
        for (int i = 0; i < loop; i++) {
            System.out.print("\t");
        }
        System.out.println("" + getInt(Hex.decodeHex(encoded.substring(initOffset, initOffset + 8).toCharArray())));
        return initOffset + 8;
    }

    /**
     * 
     * @param desc
     *            打印端口信息
     * @param encoded
     *            原始编码文件
     * @param initOffset
     *            当前偏移位置
     * @return 操作后偏移量
     * @throws DecoderException
     */
    private static int printPort(String desc, String encoded, final int initOffset) throws DecoderException {
        System.out.print(desc);
        System.out.print(":\t");
        int loop = 2 - (desc.length() + 1) / 8;
        for (int i = 0; i < loop; i++) {
            System.out.print("\t");
        }
        System.out.println("" + getUnsignedShort(Hex.decodeHex(encoded.substring(initOffset, initOffset + 4).toCharArray())));
        return initOffset + 4;
    }

    /**
     * 打印IP的头部信息
     * 
     * @param encoded
     * @return
     * @throws DecoderException
     */
    private static int printIpHeader(String encoded, final int initOffset) throws DecoderException {
        int offset = initOffset;
        System.out.println("IP HEADER:");
        offset = printVersion(encoded, offset);
        offset = printIpHeaderLength(encoded, offset);
        offset = printTos(encoded, offset);
        offset = printTotalLength(encoded, offset);
        offset = printIdentification(encoded, offset);
        offset = printControlFlags(encoded, offset);
        offset = printFragmentOffset(encoded, offset);
        offset = printTimeToLive(encoded, offset);
        offset = printProtocolOverIp(encoded, offset);
        offset = printHeaderChecksum(encoded, offset);
        offset = printIpAddress("Source Address", encoded, offset);
        offset = printIpAddress("Destination Address", encoded, offset);
        return offset;
    }

    private static int printIpAddress(String desc, String encoded, final int initOffset) throws DecoderException {
        System.out.print(desc + ":\t");
        byte[] bytes = Hex.decodeHex(encoded.substring(initOffset, initOffset + 8).toCharArray());

        for (int i = 0; i < 4; i++) {
            System.out.print(0xFF & bytes[i]);
            if (i != 3) {
                System.out.print(".");
            }
        }
        System.out.println();
        return initOffset + 8;
    }

    private static int printHeaderChecksum(String encoded, final int initOffset) {
        System.out.print("Header Checksum:\t");
        int finalOffset = printByte(encoded, initOffset, 2);
        System.out.println();
        return finalOffset;
    }

    /**
     * 打印协议
     * 
     * @param encoded
     * @param initOffset
     * @return
     */
    private static int printProtocolOverIp(String encoded, final int initOffset) {
        System.out.print("Protocol:\t\t\t");
        int finalOffset = printByte(encoded, initOffset, 1);
        System.out.println();
        return finalOffset;
    }

    /**
     * Time To Live
     * 
     * @param encoded
     *            编码字符串
     * @param offset
     *            偏移量
     * @return 打印后偏移量
     * @throws DecoderException
     */
    private static int printTimeToLive(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Time To Live:\t\t");
        byte[] bytes = Hex.decodeHex(encoded.substring(initOffset, initOffset + 2).toCharArray());
        System.out.println(bytes[0] & 0xFF);

        return initOffset + 2;
    }

    private static int printFragmentOffset(String encoded, final int initOffset) throws DecoderException {
        //
        byte[] bytes = Hex.decodeHex(encoded.substring(initOffset, initOffset + 4).toCharArray());

        short offset = (short) (((bytes[0] & 0x1F) << 8) | bytes[1]);
        System.out.println("offset:\t\t\t" + offset);
        return initOffset + 4;
    }

    /**
     * 
     * @param encoded
     * @param offset
     * @return
     * @throws DecoderException
     */
    private static int printControlFlags(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Control Flags:\t\t");

        byte[] bytes = Hex.decodeHex(encoded.substring(initOffset, initOffset + 2).toCharArray());
        System.out.print(((bytes[0] & 0x80) == 0 ? "0 'reserved'" : "1") + " ");
        System.out.print(((bytes[0] & 0x40) == 0 ? "0 'May Fragment'" : "1 'Don't Fragment'") + " ");
        System.out.print(((bytes[0] & 0x20) == 0 ? "0 'Last Fragment'" : "1 'More Fragments'"));

        System.out.println();

        return initOffset;
    }

    /**
     * <pre>
     * 标识:指IP协议的版本号。目前的主要版本为IPV4，即第4版本号，
     * 也有一些教育网和科研机构在使用IPV6。在进行通信时，
     * 通信双方的IP协议版本号必须一致，否则无法直接通信。
     * </pre>
     * 
     * @param encoded
     *            原始编码
     * @param offset
     *            偏移量
     * @return 更新后偏移量
     * @throws DecoderException
     */
    private static int printIdentification(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Identification:\t\t");
        int finalOffset = printByte(encoded, initOffset, 2);
        System.out.println(" (" + getShort(Hex.decodeHex(Arrays.copyOfRange(encoded.toCharArray(), initOffset, finalOffset))) + ")");
        return finalOffset;
    }

    private static int printTotalLength(String encoded, final int initOffset) throws DecoderException {
        System.out.print("Total Length:\t\t");
        int finalOffset = printByte(encoded, initOffset, 2);
        System.out.println(" (" + getShort(Hex.decodeHex(Arrays.copyOfRange(encoded.toCharArray(), initOffset, finalOffset))) + " bytes)");
        return finalOffset;
    }

    private static short getShort(byte[] decodeHex) {
        short s = (short) 0x0;
        s = (short) ((s | decodeHex[0]) << 8);
        s |= decodeHex[1];
        return s;
    }

    private static int getUnsignedShort(byte[] decodeHex) {
        int s = 0x0;
        s = s | (decodeHex[0] & 0xFF);
        s = (s << 8);
        s |= decodeHex[1] & 0xFF;
        return s;
    }

    private static int getInt(byte[] decodeHex) {
        int s = 0x0;
        s = s | (decodeHex[0] & 0xFF);
        s = (s << 8);
        s |= decodeHex[1] & 0xFF;
        s = (s << 8);
        s |= decodeHex[2] & 0xFF;
        s = (s << 8);
        s |= decodeHex[3] & 0xFF;
        return s;
    }

    /**
     * 打印TOS 段
     * 
     * @param encoded
     * @param initOffset
     * @return
     */
    private static int printTos(String encoded, final int initOffset) {
        System.out.print("TOS:\t\t\t\t");
        int finalOffset = printByte(encoded, initOffset, 1);
        System.out.println();
        return finalOffset;
    }

    /**
     * 打印Internet Protocol 头部长度
     * 
     * @param encoded
     * @param initOffset
     * @return
     */
    private static int printIpHeaderLength(String encoded, int initOffset) {
        System.out.print("Length:\t\t\t");
        int finalOffset = printHex(encoded, initOffset);
        System.out.println();
        return finalOffset;
    }

    /**
     * 打印 Internet Protocol 的版本
     * 
     * @param encoded
     *            编码字符串
     * @param offset
     *            偏移量
     * @return 打印后偏移量
     */
    private static int printVersion(final String encoded, final int initOffset) {
        System.out.print("Version:\t\t\t");
        int finalOffset = printHex(encoded, initOffset);
        System.out.println();
        return finalOffset;
    }

    private static int printHex(final String encoded, final int initOffset) {
        System.out.print(encoded.substring(initOffset, initOffset + 1));
        return initOffset + 1;
    }

    /**
     * 打印Ethernet 头部
     * 
     * <pre>
     * DESTERNATION SOURCE  PROTOCOL
     * 
     * </pre>
     * 
     * @param encoded
     */
    private static int printEthernetHeader(String encoded, final int initOffset) {
        int offset = initOffset;
        // 打印 Desternation
        System.out.println("ETHERNET HEADER:");
        offset = printEthernetItem("Desternation", encoded, offset);
        offset = printEthernetItem("Source", encoded, offset);
        offset = printEthernetProtocol("Protocol", encoded, offset);
        System.out.println();
        return offset;
    }

    /**
     * 打印以太网头部信息
     * 
     * @param desc
     *            输出说明
     * @param encoded
     *            十六进制编码字符串
     * @param offset
     *            offset of chars, so it double of the real bytes
     * @return 事后偏移量
     */
    private static int printEthernetItem(final String desc, String encoded, final int offset) {
        System.out.print(desc);
        System.out.print(":\t\t");
        int tLen = 3 - (desc.length()) / 8;
        for (int i = 0; i < tLen - 2; i++) {
            System.out.print("\t");
        }
        int newOffset = printEthernetMac(encoded, offset);
        System.out.println();
        return newOffset;
    }

    /**
     * 
     * @param encoded
     * @param offset
     * @return 打印完成后,offset的偏移增量后结果
     */
    private static int printEthernetMac(final String encoded, final int offset) {
        return printByte(encoded, offset, MAC_ADDRESS_LENGTH);

    }

    private static int printEthernetProtocol(final String desc, String encoded, final int offset) {
        System.out.print("Protocol");
        System.out.print(":\t\t");
        int tLen = 4 - (desc.length()) / 8;
        for (int i = 0; i < tLen - 2; i++) {
            System.out.print("\t");
        }

        int newOffset = printByte(encoded, offset, ETHERNET_PROTOCOL_LENGTH);
        if ("0800".equals(encoded.substring(newOffset - 4, newOffset))) {
            System.out.print(" (Internet Protocol)");
        }
        return newOffset;

    }

    /**
     * 
     * @param encoded
     * @param offset
     * @param i
     * @return
     */
    private static int printByte(String encoded, int offset, final int byteLen) {
        int i = offset;
        for (; i < offset + (byteLen * 2); i += 2) {
            System.out.print(encoded.substring(i, i + 2));
            if (i < offset + (byteLen * 2) - 2)
                System.out.print(" ");
        }
        return i;
    }
}
