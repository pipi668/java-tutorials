/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.certs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Enumeration;

/**
 * @author LiuJian
 * @date 2017年4月3日
 * 
 */
public class X509CertLoader {
    public static void main(String[] args) throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException,
            KeyStoreException, UnrecoverableKeyException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        Field keyStoreSpi = KeyStore.class.getDeclaredField("keyStoreSpi");

        if (!keyStoreSpi.isAccessible()) {
            keyStoreSpi.setAccessible(true);
        }

        KeyStore truststoreDst = KeyStore.getInstance("PKCS12");
        System.out.println("pkcs12 store className:\t" + keyStoreSpi.get(truststoreDst).getClass().getName());
        truststoreDst.load(new FileInputStream("f:/key/trust-client.p12"), "changeit".toCharArray());
        KeyStore truststoreSrc = KeyStore.getInstance("JKS");
        System.out.println("jks store className:\t" + keyStoreSpi.get(truststoreSrc).getClass().getName());
        truststoreSrc.load(new FileInputStream("f:/key/trust-client.jks"), "changeit".toCharArray());
        print(truststoreSrc);
        final String alias = "aposoft-cn-cert0";
        Certificate cert = truststoreSrc.getCertificate("aposoft-cn-cert0");

        truststoreDst.setCertificateEntry(alias, cert);
        truststoreDst.store(new FileOutputStream("f:/key/new-trust-client.p12"), "changeit".toCharArray());
    }

    private static void print(KeyStore truststore) throws UnrecoverableKeyException, NoSuchAlgorithmException {
        try {
            Enumeration<String> alias = truststore.aliases();
            System.out.println("entry size:\t" + truststore.size());
            while (alias.hasMoreElements()) {
                String a = alias.nextElement();
                System.out.println("alias:\t" + a);
                if (truststore.isCertificateEntry(a)) {
                    print(truststore.getCertificate(a));
                } else if (truststore.isKeyEntry(a)) {
                    System.out.println("a is a key entry.");
                    Key key = truststore.getKey(a, "changeit".toCharArray());
                    print(key);
                }
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private static void print(Key key) {
        System.out.println(key.getAlgorithm());
        System.out.println(key.getFormat());
        System.out.println("class:" + key.getClass().getName());
        if (key instanceof RSAPrivateCrtKey) {
            print((RSAPrivateCrtKey) key);
        }
    }

    private static void print(RSAPrivateCrtKey key) {
        System.out.println("RSA Private Key:");
        System.out.println(key.getModulus());
        System.out.println(key.getCrtCoefficient());
        System.out.println(key.getPrimeExponentP());
        System.out.println(key.getPrimeExponentQ());
        System.out.println(key.getPrimeP());
        System.out.println(key.getPrimeQ());
        System.out.println(key.getPrivateExponent());
        System.out.println(key.getPublicExponent());
        System.out.println(key.getFormat());
        System.out.println("class:" + key.getClass().getName());
    }

    private static void print(Certificate certificate) {
        System.out.println(certificate.getType());
        System.out.println(certificate.getClass());
        System.out.println(certificate.getPublicKey());
        if (certificate instanceof X509Certificate) {
            print((X509Certificate) certificate);
        }
    }

    private static void print(X509Certificate certificate) {
        System.out.println(certificate.getSigAlgName());
        System.out.println(certificate.getSigAlgOID());
        System.out.println(certificate.getVersion());
        System.out.println(certificate.getIssuerDN());
        System.out.println(certificate.getIssuerUniqueID());
        System.out.println(certificate.getSubjectDN());
        System.out.println(certificate.getSubjectUniqueID());
    }
}
