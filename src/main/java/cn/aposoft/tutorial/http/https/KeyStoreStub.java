/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author LiuJian
 * @date 2017年3月29日
 * 
 */
public class KeyStoreStub {

    /**
     * @param args
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     */
    public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeyException,
            NoSuchProviderException, SignatureException {
        visitKeyStore("jssecacerts", "changeit", "JKS");
        // visitKeyStore("client.p12", "123456", "PKCS12");
        // visitKeyStore("cacerts-jdk1.8", "changeit", "JKS");
        // visitKeyStore("cacerts-jdk1.7", "changeit", "JKS");

    }

    private static void visitKeyStore(String keyFileName, String password, String keyStoreType) throws IOException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, InvalidKeyException, NoSuchProviderException, SignatureException {
        File file = new File(keyFileName);
        char[] passphrase = password.toCharArray();
        System.out.println("Loading KeyStore " + file + "...");

        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(keyStoreType);
        ks.load(in, passphrase);
        in.close();
        // visitKeyStore(ks);

        verifyCertificate("myfen.gomemyf.com-1", ks); //
    }

    // 验证证书有效性
    private static void verifyCertificate(String alias, KeyStore ks) throws KeyStoreException, InvalidKeyException, CertificateException,
            NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
        X509Certificate[] chain = HttpsTools.getX509CertificateChain(alias, ks);
        System.out.println(Arrays.toString(chain));

        // verify
        System.out.println("Start verify...");
        if (chain.length > 0) {
            // verify Root
            System.out.println("Begin verify Root:" + chain[0].getSubjectDN());
            chain[0].verify(chain[0].getPublicKey());
            System.out.println("Begin verify  descendants:");
            for (int i = 1; i < chain.length; i++) {
                System.out.println("Chain[" + i + "]:" + chain[i].getSubjectDN());
                chain[i].verify(chain[i - 1].getPublicKey());
            }
            System.out.println("verify procedure is completed!");
        }
    }

}
