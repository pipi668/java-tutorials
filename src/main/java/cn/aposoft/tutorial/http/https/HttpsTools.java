/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
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

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import sun.security.util.DerValue;
import sun.security.x509.X509CertInfo;

/**
 * @author LiuJian
 * @date 2017年4月3日
 * 
 */
public class HttpsTools {
    /**
     * 创建带自签名授信证书的SSLContext
     * 
     * @return 自签名授信证书的SSLContext
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static SSLContext createSSLContext()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
        // PKIX
        // System.out.println(TrustManagerFactory.getDefaultAlgorithm());

        // 加载自签名授信证书
        final KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(new FileInputStream("f:/key/trust-client.jks"), "changeit".toCharArray());
        SSLContext sslContext = SSLContexts.custom()//
                .loadTrustMaterial(truststore, null) //
                .build();
        return sslContext;
    }

    // 创建用于监听证书的sslcontext
    /**
     * 创建带自签名授信证书的SSLContext 并添加了对证书的监听策略
     * 
     * @return 创建带自签名授信证书的SSLContext
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static SSLContext createCertificateSnifferSSLContext()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
        // PKIX
        // System.out.println(TrustManagerFactory.getDefaultAlgorithm());

        // 加载自签名授信证书
        final KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(new FileInputStream("f:/key/trust-client.jks"), "changeit".toCharArray());
        TrustStrategy strategy = createSnifferStrategy();
        SSLContext sslContext = SSLContexts.custom()//
                .loadTrustMaterial(truststore, strategy) //
                .build();
        return sslContext;
    }

    // 创建用于监听证书的sslcontext
    /**
     * 创建带自签名授信证书的SSLContext 并添加了对证书的监听策略
     * 
     * @return 创建带自签名授信证书的SSLContext
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws UnrecoverableKeyException
     */
    public static SSLContext createPKCS12CertificateSnifferSSLContext() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
            CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
        // PKIX
        // System.out.println(TrustManagerFactory.getDefaultAlgorithm());

        // 加载自签名授信证书
        final KeyStore truststore = KeyStore.getInstance("PKCS12");
        truststore.load(new FileInputStream("f:/key/trust-client.p12"), "changeit".toCharArray());
        visitKeyStore(truststore);

        TrustStrategy strategy = createSnifferStrategy();

        SSLContext sslContext = SSLContexts.custom()//
                .loadTrustMaterial(truststore, strategy) //
                .build();
        return sslContext;
    }

    public static SSLContext createDualSSLContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
            IOException, KeyManagementException, UnrecoverableKeyException {
        // 加载自签名授信证书
        final KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(new FileInputStream("f:/key/trust-client.jks"), "changeit".toCharArray());

        final KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream("f:/key/privateKey.p12"), "changeit".toCharArray());
        HttpsTools.visitKeyStore(keystore);

        SSLContext sslContext = SSLContexts.custom()//
                .loadTrustMaterial(truststore, null) //
                .loadKeyMaterial(keystore, "changeit".toCharArray())//
                .build();
        return sslContext;
    }

    public static TrustStrategy createSnifferStrategy() {
        TrustStrategy strategy = new TrustStrategy() {

            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                System.out.println("authType:" + authType);

                if (chain != null && chain.length > 0) {
                    for (X509Certificate cert : chain) {
                        boolean isVerified;
                        try {
                            isVerified = verify(cert);
                        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                            e.printStackTrace();
                            isVerified = false;
                        }
                        if (isVerified)
                            return true;
                    }
                }

                return false;
            }

        };
        return strategy;
    }

    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    // 自签名证书验证
    /**
     * 验证证书真实性(自签名证书)
     * 
     * @param cert
     *            证书内容
     * @return 证书真实性验证结果
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws NoSuchProviderException
     * @throws CertificateException
     */
    public static boolean verify(X509Certificate cert)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, CertificateException {
        // 公钥
        PublicKey pkey = cert.getPublicKey();

        try {
            cert.verify(pkey);
            System.out.println("cert is self verified.");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.out.println("cert is not self verified.");
        } catch (SignatureException e) {
            e.printStackTrace();
            System.out.println("Signature does not match.");
        }

        byte[] encoded = null;

        try {
            sun.security.util.DerValue dv = new sun.security.util.DerValue(cert.getEncoded());
            DerValue[] arrayOfDerValue = new DerValue[3];

            arrayOfDerValue[0] = dv.data.getDerValue();
            arrayOfDerValue[1] = dv.data.getDerValue();
            arrayOfDerValue[2] = dv.data.getDerValue();

            X509CertInfo info = new X509CertInfo(arrayOfDerValue[0]);
            encoded = info.getEncodedInfo();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 签名 算法
        final String signatureAlgName = cert.getSigAlgName();
        // 签名 字节组
        byte[] signature = cert.getSignature();

        // 执行签名验证
        Signature signatureAlgorithm = Signature.getInstance(signatureAlgName);
        signatureAlgorithm.initVerify(pkey);
        signatureAlgorithm.update(encoded);
        boolean isVerified = signatureAlgorithm.verify(signature);
        System.out.println("isVerified:" + isVerified);
        return isVerified;
    }

    // CA签名证书验证
    /**
     * 验证证书真实性
     * 
     * @param cert
     *            待验证签名证书
     * @param pkey
     *            证书签名公钥
     * @return 证书真实性验证结果
     * @throws NoSuchAlgorithmException
     * @throws CertificateEncodingException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verify(X509Certificate cert, PublicKey pkey)
            throws NoSuchAlgorithmException, CertificateEncodingException, InvalidKeyException, SignatureException {

        byte[] encoded = cert.getEncoded();
        // 签名
        byte[] signature = cert.getSignature();
        Signature signatureAlgorithm = Signature.getInstance(SIGNATURE_ALGORITHM);
        signatureAlgorithm.initVerify(pkey);
        signatureAlgorithm.update(encoded);
        return signatureAlgorithm.verify(signature);
    }

    public static void visitKeyStore(KeyStore ks)
            throws KeyStoreException, CertificateEncodingException, CertificateParsingException, UnrecoverableKeyException, NoSuchAlgorithmException {
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

            System.out.println("----Begin Alias:\t" + alias + ",\tType:\t" + (ks.isCertificateEntry(alias) ? "Certificate" : "KEY"));
            if (ks.isCertificateEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                visitCertificate(cert);
                if (cert instanceof X509Certificate) {
                    System.out.println("----Parents:");
                    getIssuerCertificate((X509Certificate) cert, ks);
                }
            } else if (ks.isKeyEntry(alias)) {
                Key key = ks.getKey(alias, "changeit".toCharArray());
                System.out.println("Class:" + key.getClass().getName());
                System.out.println("Algorithm:" + key.getAlgorithm());
                System.out.println("Format:" + key.getFormat());
                System.out.println("Total Content:" + key.toString());
            }
            System.out.println("----End Alias:\t" + alias + ",\tType:\t" + (ks.isCertificateEntry(alias) ? "Certificate" : "KEY"));
            System.out.println();
            System.out.println();
            System.out.println();

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
            visitCertificate((Certificate) x509);
            if (x509.getIssuerDN().equals(x509.getSubjectDN())) {
                break;
            }
            principal = x509.getIssuerDN();
        }
    }

    public static X509Certificate getCertificate(Principal principal, KeyStore ks) throws KeyStoreException {
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

    public static X509Certificate[] getX509CertificateChain(String alias, KeyStore ks) throws KeyStoreException {
        List<X509Certificate> certList = new ArrayList<>();

        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
        certList.add(cert);

        // 遍历根证书

        Principal principal = cert.getIssuerDN();

        while (principal != null) {
            X509Certificate x509 = HttpsTools.getCertificate(principal, ks);
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

    public static void visitCertificate(Certificate certificate) throws CertificateEncodingException, CertificateParsingException {
        System.out.println("--------Type:\t" + certificate.getType());
        System.out.println("-------Class:\t" + certificate.getClass());
        System.out.println("--------Public Key:\t" + certificate.getPublicKey());
        System.out.println("--------Encoded:\t" + certificate.getEncoded().length);
        if (certificate instanceof X509Certificate) {
            visitX509Certificate((X509Certificate) certificate);
        }
    }

    public static void visitX509Certificate(X509Certificate certificate) throws CertificateEncodingException, CertificateParsingException {
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
