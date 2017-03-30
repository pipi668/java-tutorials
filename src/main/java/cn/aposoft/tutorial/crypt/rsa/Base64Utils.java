/**
 * 
 */
package cn.aposoft.tutorial.crypt.rsa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 * BASE64编码解码工具包
 * </p>
 * 
 * @author IceWee
 * @date 2012-5-19
 * @version 1.0
 */
public class Base64Utils {
    private final static Charset ENCODE_CHARSET = Charset.forName("UTF-8");

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * <p>
     * BASE64字符串解码为二进制数据
     * </p>
     * 
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) {
        return Base64.decodeBase64(base64.getBytes(ENCODE_CHARSET));
    }

    /**
     * <p>
     * 二进制数据编码为BASE64字符串 ,数据采用 UTF-8进行反解码
     * </p>
     * 
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes), ENCODE_CHARSET);
    }

    /**
     * <p>
     * 将文件编码为BASE64字符串
     * </p>
     * <p>
     * 大文件慎用，可能会导致内存溢出
     * </p>
     * 
     * @param filePath
     *            文件绝对路径
     * @return
     * @throws IOException
     */
    public static String encodeFile(String filePath) throws IOException {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }

    /**
     * <p>
     * BASE64字符串转回文件
     * </p>
     * 
     * @param filePath
     *            文件绝对路径
     * @param base64
     *            编码字符串
     * @throws IOException
     */
    public static void decodeToFile(String filePath, String base64) throws IOException {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }

    /**
     * <p>
     * 文件转换为二进制数组
     * </p>
     * 
     * @param filePath
     *            文件路径
     * @return
     * @throws IOException
     */
    public static byte[] fileToByte(String filePath) throws IOException {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file); ByteArrayOutputStream out = new ByteArrayOutputStream(2048);) {
                byte[] cache = new byte[CACHE_SIZE];
                int nRead = 0;
                while ((nRead = in.read(cache)) != -1) {
                    out.write(cache, 0, nRead);
                    out.flush();
                }
                data = out.toByteArray();
            }
        }
        return data;
    }

    /**
     * <p>
     * 二进制数据写文件
     * </p>
     * 
     * @param bytes
     *            二进制数据
     * @param filePath
     *            文件生成目录
     * @throws IOException
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws IOException {
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        try (InputStream in = new ByteArrayInputStream(bytes); //
                OutputStream out = new FileOutputStream(destFile);) {

            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
        }
    }

}
