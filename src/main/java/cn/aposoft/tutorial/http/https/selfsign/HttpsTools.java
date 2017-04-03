/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.selfsign;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

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
     */
    public static SSLContext createPKCS12CertificateSnifferSSLContext()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
        // PKIX
        // System.out.println(TrustManagerFactory.getDefaultAlgorithm());

        // 加载自签名授信证书
        final KeyStore truststore = KeyStore.getInstance("PKCS12");
        truststore.load(new FileInputStream("f:/key/trust-client.p12"), "changeit".toCharArray());
        TrustStrategy strategy = createSnifferStrategy();
        SSLContext sslContext = SSLContexts.custom()//
                .loadTrustMaterial(truststore, strategy) //
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
}
