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
import java.security.Provider;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;

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
        X509Certificate[] chain = getX509CertificateChain(alias, ks);
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

    private static X509Certificate[] getX509CertificateChain(String alias, KeyStore ks) throws KeyStoreException {
        List<X509Certificate> certList = new ArrayList<>();

        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
        certList.add(cert);

        // 遍历根证书

        Principal principal = cert.getIssuerDN();

        while (principal != null) {
            X509Certificate x509 = getCertificate(principal, ks);
            certList.add(x509);
            if (x509.getIssuerDN().equals(x509.getSubjectDN())) {
                System.out.println("meets root.");
                break;
            }
            principal = x509.getIssuerDN();
        }
        Collections.reverse(certList);
        return certList.toArray(new X509Certificate[0]);
    }

    private static void visitKeyStore(KeyStore ks) throws KeyStoreException, CertificateEncodingException, CertificateParsingException {
        System.out.println("--getClass:\t" + ks.getClass());
        // keystore type: jks, pkcs12, pkcs8 等
        System.out.println("--getType:\t" + ks.getType());
        // size:
        System.out.println("--size:\t" + ks.size());
        Provider provider = ks.getProvider();
        // visitProvider(provider);

        Enumeration<String> aliases = ks.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (alias.contains("myfen.gomemyf.com-1")) {
                System.out.println("----Alias:\t" + alias + ",\tType:\t" + (ks.isCertificateEntry(alias) ? "Certificate" : "KEY"));
                if (ks.isCertificateEntry(alias)) {
                    Certificate cert = ks.getCertificate(alias);
                    visitCertificate(cert);
                    if (cert instanceof X509Certificate) {
                        getIssuerCertificate((X509Certificate) cert, ks);
                    }
                }
            }
        }
    }

    private static void getIssuerCertificate(X509Certificate cert, KeyStore ks)
            throws KeyStoreException, CertificateEncodingException, CertificateParsingException {
        String alias = ks.getCertificateAlias(cert);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("alias:" + alias);

        Principal principal = cert.getIssuerDN();

        int i = 0;
        while (principal != null && i++ < 5) {
            X509Certificate x509 = getCertificate(principal, ks);
            System.out.println();
            System.out.println("i=" + i);
            System.out.println();
            visitCertificate(x509);
            if (x509.getIssuerDN().equals(x509.getSubjectDN())) {
                break;
            }
            principal = x509.getIssuerDN();
        }
    }

    private static X509Certificate getCertificate(Principal principal, KeyStore ks) throws KeyStoreException {
        Enumeration<String> aliases = ks.aliases();

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (ks.isCertificateEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                if (cert instanceof X509Certificate) {
                    X509Certificate x509 = (X509Certificate) cert;
                    if (x509.getSubjectDN().equals(principal)) {
                        return x509;
                    }
                }
            }
        }
        return null;
    }

    public static void visitCertificate(Certificate certificate) throws CertificateEncodingException, CertificateParsingException {
        System.out.println("--------Type:\t" + certificate.getType());
        System.out.println("-------Class:\t" + certificate.getClass());
        System.out.println("--------Public Key:\t" + certificate.getPublicKey());
        System.out.println("--------Encoded:\t" + certificate.getEncoded().length);
        if (certificate instanceof X509Certificate) {
            visitX509Certificate((X509Certificate) certificate);
        }
    }

    private static void visitX509Certificate(X509Certificate certificate) throws CertificateEncodingException, CertificateParsingException {
        System.out.println("--------Version:\t" + certificate.getVersion());
        System.out.println("--------SerialNumber:\t" + certificate.getSerialNumber());

        System.out.println("--------IssuerUniqueID:\t" + certificate.getIssuerUniqueID());
        System.out.println("--------IssuerDN:\t" + certificate.getIssuerDN());
        System.out.println("--------IssuerX500:\t" + certificate.getIssuerX500Principal());

        System.out.println("--------SubjectUniqueID:\t" + certificate.getSubjectUniqueID());
        System.out.println("--------SubjectDN:\t" + certificate.getSubjectDN());
        System.out.println("--------SubjectX500:\t" + certificate.getSubjectX500Principal());

        System.out.println("--------Dates between:\t" + certificate.getNotBefore() + " and " + certificate.getNotAfter());

        System.out.println("--------Signature:\t" + Base64.encodeBase64String(certificate.getSignature()));

        System.out.println("--------Signature Algorithm Name:\t" + certificate.getSigAlgName());

        System.out.println("--------Signature Algorithm OID:\t" + certificate.getSigAlgOID());

        System.out.println("--------BasicConstraints:\t" + certificate.getBasicConstraints());

        //
        if (certificate.getCriticalExtensionOIDs() != null) {
            System.out.println("--------CriticalExtensionOIDs:\t" + Arrays.toString(certificate.getCriticalExtensionOIDs().toArray()));
        }
        //
        if (certificate.getExtendedKeyUsage() != null) {
            System.out.println("--------ExtendedKeyUsage:\t" + Arrays.toString(certificate.getExtendedKeyUsage().toArray()));
        }
        //
        if (certificate.getIssuerAlternativeNames() != null) {
            System.out.println("--------IssuerAlternativeNames:\t" + Arrays.toString(certificate.getIssuerAlternativeNames().toArray()));
        }
        //
        if (certificate.getSubjectAlternativeNames() != null) {
            System.out.println("--------SubjectAlternativeNames:\t" + Arrays.toString(certificate.getSubjectAlternativeNames().toArray()));
        }

    }

    private static void visitProvider(Provider provider) throws KeyStoreException {
        System.out.println("--Provider:\t" + provider.getName());
        System.out.println("----Info:\t" + provider.getInfo());
        System.out.println("----Version:\t" + provider.getVersion());
        System.out.println("----Class:\t" + provider.getClass());
        System.out.println("----EntrySet:\t" + provider.entrySet().size());
        // for (Entry<Object, Object> entry : provider.entrySet()) {
        // // visitProviderEntry(entry);
        // }

    }

    private static void visitProviderEntry(Entry<Object, Object> entry) {
        System.out.println("----Entry:" + entry.getKey());
        System.out.println("----Entry-V:" + entry.getValue());
        System.out.println();
    }
}
