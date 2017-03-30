/**
 * 
 */
package cn.aposoft.tutorial.crypt.rsa;

import java.nio.charset.Charset;
import java.security.interfaces.RSAPrivateCrtKey;

import cn.aposoft.tutorial.crypt.rsa.RSAUtils.KeyPairEntry;

public class RSATester {

    static String publicKey;
    static String privateKey;

    static {

    }

    public static void main(String[] args) throws Exception {
        createKeyPair();
        test();
        testSign();
    }

    static void createKeyPair() {
        try {
            KeyPairEntry keyMap = RSAUtils.generateKeyPair();
            publicKey = keyMap.getEncodedPublicKey();
            privateKey = keyMap.getEncodedPrivateKey();
            System.out.println("PublicModulus:" + keyMap.getPublicKey().getModulus().toString());
            System.out.println("PublicExponent:" + keyMap.getPublicKey().getPublicExponent().toString());

            System.out.println("public key:");
            System.out.println(keyMap.getPublicKey().toString());
            System.out.println();
            System.out.println("private key:");
            System.out.println(keyMap.getPrivateKey().toString());
            System.out.println();
            if (keyMap.getPrivateKey() instanceof RSAPrivateCrtKey) {
                RSAPrivateCrtKey crtKey = (RSAPrivateCrtKey) keyMap.getPrivateKey();
                System.out.println("PublicExponent" + crtKey.getPublicExponent().toString());
                System.out.println("PrivateExponent:" + crtKey.getPrivateExponent().toString());
                System.out.println("PrivateModulus:" + crtKey.getModulus().toString());
            }

            System.out.println("公钥: " + publicKey.length() + "\n\r" + publicKey);
            System.out.println("私钥：" + privateKey.length() + " \n\r" + privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void test() throws Exception {
        Charset charset = Charset.forName("UTF-8");
        System.out.println("公钥加密——私钥解密");
        String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
        System.out.println("\r加密前文字：\r\n" + source);
        byte[] data = source.getBytes(charset);
        byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
        System.out.println("加密后文字：\r\n" + new String(encodedData));
        byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData, privateKey);
        String target = new String(decodedData, charset);
        System.out.println("解密后文字: \r\n" + target);
    }

    static void testSign() throws Exception {
        System.out.println("*****************************************************");
        System.out.println("私钥加密——公钥解密");
        String source = "这是一行测试RSA数字签名的无意义文字";
        System.out.println("原文字：\r\n" + source);
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
        System.out.println("加密后：\r\n" + new String(encodedData));
        byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData, publicKey);
        String target = new String(decodedData);
        System.out.println("解密后: \r\n" + target);
        System.out.println("私钥签名——公钥验证签名");
        String sign = RSAUtils.sign(encodedData, privateKey);
        System.out.println("签名:\r" + sign);
        boolean status = RSAUtils.verify(encodedData, publicKey, sign);
        System.out.println("验证结果:\r" + status);
    }

}