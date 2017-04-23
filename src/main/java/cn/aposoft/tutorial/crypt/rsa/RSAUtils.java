/**
 * 
 */
package cn.aposoft.tutorial.crypt.rsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 * 
 * RSA 公私钥关系
 * 
 * <pre>
 * p,q为大素数 且 p>=q
 * Modulus = n = p*q
 *  e 任意数值，在Java Rsa算法中为实际为 3 或 65537
 *  e 要求与 (p-1)*(q-1) 互质（即最大公约数为1）
 *  
 *  定义  z = (p-1)*(q-1);
 *  
 *  (d * e) mod z=1 等价于 (d * e) -1 = z * n (n 为>=1的正整数)
 *  d =  (z * n + 1)  / e  右边表达式必须返回整数
 * </pre>
 * 
 * 
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public class RSAUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * RSA defaults to 1024 (颁发证书时例外,为2048)
     */
    public static final int KEY_LENGTH = 1024;
    /**
     * 签名算法 RSA defaults to sha256 in java keytool
     */
    // public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    /**
     * RSA最大加密明文大小, DEFAULT TO PKCS#1 PADDING
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final KeyFactory keyFactory = createKeyFactory();

    static KeyPairGenerator createKeyPairGenerator() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            System.out.println(generator.getClass().getName());
            try {
                Field field = generator.getClass().getDeclaredField("spi");
                if (!field.isAccessible())
                    field.setAccessible(true);
                Object obj;
                try {
                    obj = field.get(generator);
                    System.out.println(obj.getClass().getName());
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (NoSuchFieldException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return generator;
        } catch (NoSuchAlgorithmException e) {
            // this will not happen
            return null;
        }
    }

    static Signature createSignature() {
        try {
            return Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // this will not happen
            return null;
        }
    }

    static Cipher createCipher() {
        try {
            return Cipher.getInstance(keyFactory.getAlgorithm());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // this will not happen
            return null;
        }
    }

    static KeyFactory createKeyFactory() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory;
        } catch (NoSuchAlgorithmException e) {
            // this will not happen
            return null;
        }

    }

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     * 
     * @return
     * @throws Exception
     */
    public static KeyPairEntry generateKeyPair() {
        KeyPairGenerator keyPairGen = createKeyPairGenerator();
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        KeyPairEntry entry = new KeyPairEntry();
        entry.setPublicKey(publicKey);
        entry.setPrivateKey(privateKey);
        return entry;
    }

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     * 
     * @param length
     *            制定密钥的长度
     * @return 公私密钥对
     * @throws Exception
     */
    public static KeyPairEntry generateKeyPair(int length) {
        KeyPairGenerator keyPairGen = createKeyPairGenerator();
        keyPairGen.initialize(length);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        KeyPairEntry entry = new KeyPairEntry();
        entry.setPublicKey(publicKey);
        entry.setPrivateKey(privateKey);
        return entry;
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     * 
     * @param data
     *            已加密数据
     * @param privateKey
     *            私钥(BASE64编码)
     * 
     * @return
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String sign(byte[] data, String privateKey) throws InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = createSignature();
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encode(signature.sign());
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     * 
     * @param data
     *            已加密数据
     * @param publicKey
     *            公钥(BASE64编码)
     * @param sign
     *            数字签名
     * 
     * @return
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws Exception
     * 
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = createSignature();
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     * 
     * @param encryptedData
     *            已加密数据
     * @param privateKey
     *            私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        return decryptByPrivateKey(encryptedData, privateKey, MAX_DECRYPT_BLOCK);
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     * 
     * @param encryptedData
     *            已加密数据
     * @param privateKey
     *            私钥(BASE64编码)
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String privateKey) throws InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return decryptByPublicKey(encryptedData, privateKey, MAX_DECRYPT_BLOCK);
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     * 
     * @param encryptedData
     *            已加密数据
     * @param privateKey
     *            私钥(BASE64编码)
     * @return
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey, final int maxDecryptBlock)
            throws InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        Cipher cipher = createCipher();
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxDecryptBlock) {
                    cache = cipher.doFinal(encryptedData, offSet, maxDecryptBlock);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxDecryptBlock;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        } catch (IOException e) {
            // this won't happen
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     * 
     * @param encryptedData
     *            已加密数据
     * @param publicKey
     *            公钥(BASE64编码)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey, final int maxDecryptBlock) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = createCipher();
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxDecryptBlock) {
                    cache = cipher.doFinal(encryptedData, offSet, maxDecryptBlock);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxDecryptBlock;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        } catch (IOException e) {
            // won't happen
            throw new RuntimeException("ByteArrayOutputStream close exception, this will never happen.");
        }

    }

    /**
     * <p>
     * 公钥加密
     * </p>
     * 
     * @param data
     *            源数据
     * @param publicKey
     *            公钥(BASE64编码)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws IOException
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return encryptByPublicKey(data, publicKey, MAX_ENCRYPT_BLOCK);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     * 
     * @param data
     *            源数据
     * @param publicKey
     *            公钥(BASE64编码)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws IOException
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey, final int maxEncryptBlock) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = createCipher();
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxEncryptBlock) {
                    cache = cipher.doFinal(data, offSet, maxEncryptBlock);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxEncryptBlock;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } catch (IOException e) {
            // this won't happen
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     * 
     * @param data
     *            源数据
     * @param privateKey
     *            私钥(BASE64编码)
     * @return
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        return encryptByPrivateKey(data, privateKey, MAX_DECRYPT_BLOCK);
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     * 
     * @param data
     *            源数据
     * @param privateKey
     *            私钥(BASE64编码)
     * @return
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey, final int maxEncryptBlock)
            throws InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = createCipher();
        cipher.init(Cipher.ENCRYPT_MODE, privateK);

        int inputLen = data.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxEncryptBlock) {
                    cache = cipher.doFinal(data, offSet, maxEncryptBlock);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxEncryptBlock;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } catch (IOException e) {
            // this won't happen
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公私密钥对
     * 
     * @author LiuJian
     *
     */
    public static class KeyPairEntry {
        void setPublicKey(RSAPublicKey publicKey) {
            this.publicKey = publicKey;

        }

        void setPrivateKey(RSAPrivateKey privateKey) {
            this.privateKey = privateKey;
        }

        private RSAPublicKey publicKey;
        private RSAPrivateKey privateKey;

        public RSAPublicKey getPublicKey() {
            return this.publicKey;
        }

        public RSAPrivateKey getPrivateKey() {
            return this.privateKey;
        }

        /**
         * <p>
         * 获取公钥
         * </p>
         * 
         * @param keyMap
         *            密钥对
         * @return
         * @throws Exception
         */
        public String getEncodedPublicKey() {
            return Base64Utils.encode(this.publicKey.getEncoded());
        }

        /**
         * <p>
         * 获取私钥
         * </p>
         * 
         * @param keyMap
         *            密钥对
         * @return
         * @throws Exception
         */
        public String getEncodedPrivateKey() {
            return Base64Utils.encode(this.privateKey.getEncoded());
        }
    }

    /**
     * 将私钥转化为xml格式
     * 
     * @param encodedPrivkey=privateKey.getEncoded()
     * 
     * @author wenfengzhuo
     */
    public static String privatekeyinfoToXMLRSAPriKey(byte[] encodedPrivkey) {
        try {
            StringBuffer buff = new StringBuffer(1024);

            PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(encodedPrivkey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(pvkKeySpec);

            buff.append("<RSAKeyValue>");
            buff.append("<Modulus>" + Base64Utils.encode(removeMSZero(pvkKey.getModulus().toByteArray())) + "</Modulus>");
            buff.append("<Exponent>" + Base64Utils.encode(removeMSZero(pvkKey.getPublicExponent().toByteArray())) + "</Exponent>");
            buff.append("<P>" + Base64Utils.encode(removeMSZero(pvkKey.getPrimeP().toByteArray())) + "</P>");
            buff.append("<Q>" + Base64Utils.encode(removeMSZero(pvkKey.getPrimeQ().toByteArray())) + "</Q>");
            buff.append("<DP>" + Base64Utils.encode(removeMSZero(pvkKey.getPrimeExponentP().toByteArray())) + "</DP>");
            buff.append("<DQ>" + Base64Utils.encode(removeMSZero(pvkKey.getPrimeExponentQ().toByteArray())) + "</DQ>");
            buff.append("<InverseQ>" + Base64Utils.encode(removeMSZero(pvkKey.getCrtCoefficient().toByteArray())) + "</InverseQ>");
            buff.append("<D>" + Base64Utils.encode(removeMSZero(pvkKey.getPrivateExponent().toByteArray())) + "</D>");
            buff.append("</RSAKeyValue>");
            return buff.toString();
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    /**
     * 将公钥转化为xml格式
     * 
     * @param encodedPrivkey=privateKey.getEncoded()
     * 
     * @author wenfengzhuo
     */
    public static String privatekeyinfoToXMLRSAPubKey(byte[] encodedPrivkey) {
        try {
            StringBuffer buff = new StringBuffer(1024);

            PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(encodedPrivkey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(pvkKeySpec);
            buff.append("<RSAKeyValue>");
            buff.append("<Modulus>" + Base64Utils.encode(removeMSZero(pvkKey.getModulus().toByteArray())) + "</Modulus>");
            buff.append("<Exponent>" + Base64Utils.encode(removeMSZero(pvkKey.getPublicExponent().toByteArray())) + "</Exponent>");
            buff.append("</RSAKeyValue>");
            return buff.toString();
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    /**
     * 实现java和C# key的byte[]值互通
     * 
     * @author wenfengzhuo
     */
    private static byte[] removeMSZero(byte[] data) {
        byte[] data1;
        int len = data.length;
        if (data[0] == 0) {
            data1 = new byte[data.length - 1];
            System.arraycopy(data, 1, data1, 0, len - 1);
        } else
            data1 = data;
        return data1;
    }

    /**
     * 获取publicKey
     * 
     * @author wenfengzhuo
     */
    public static PublicKey getPublicKey(String module, String exponentString) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] modulusBytes = Base64Utils.decode(module);
        byte[] exponentBytes = Base64Utils.decode(exponentString);
        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);
        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
        PublicKey pubKey = keyFactory.generatePublic(rsaPubKey);
        return pubKey;
    }

    /**
     * 获取privateKey
     * 
     * @author wenfengzhuo
     */
    public static PrivateKey getPrivateKey(String delement, String module) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] exponentBytes = Base64Utils.decode(delement);
        byte[] modBytes = Base64Utils.decode(module);
        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger privateExponent = new BigInteger(1, exponentBytes);
        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, privateExponent);
        PrivateKey privKey = keyFactory.generatePrivate(privSpec);
        return privKey;
    }
}
