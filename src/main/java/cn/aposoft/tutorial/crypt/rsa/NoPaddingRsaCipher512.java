/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.crypt.rsa;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import cn.aposoft.tutorial.crypt.rsa.RSAUtils.KeyPairEntry;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author LiuJian
 * @date 2017年4月22日
 * 
 */
public class NoPaddingRsaCipher512 {

    /**
     * @param args
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static void main(String[] args)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final int keyLength = 512;
        KeyPairEntry keyPair = RSAUtils.generateKeyPair(keyLength);
        Security.addProvider(new BouncyCastleProvider());
        Provider provider = Security.getProvider("BC");
        System.out.println(provider);
        Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding", provider);

        byte[] plainText = "this is a plainText".getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublicKey());
        cipher.update(plainText);
        byte[] encryptedText = cipher.doFinal();
        Cipher cipherDecrypt = Cipher.getInstance("RSA/NONE/NoPadding", provider);
        cipherDecrypt.init(Cipher.DECRYPT_MODE, keyPair.getPrivateKey());
        cipherDecrypt.update(encryptedText);
        byte[] decryptText = cipherDecrypt.doFinal();
        System.out.println("decryptText:" + new String(decryptText, StandardCharsets.UTF_8));
    }

}
