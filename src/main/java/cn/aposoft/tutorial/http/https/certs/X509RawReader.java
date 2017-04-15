/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.certs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * X509证书读取
 * 针对DER格式编码的读取功能
 * @author LiuJian
 * @date 2017年4月4日
 * 
 */
public class X509RawReader {

    /**
     * @param args
     * @throws CertificateException
     * @throws FileNotFoundException
     * @throws SignatureException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static void main(String[] args) throws CertificateException, FileNotFoundException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, SignatureException {
        // 原始文件读取没有问题
        final String fileName = "f:/key/client-raw.cer";
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream input = new FileInputStream(fileName);
        X509Certificate cert = (X509Certificate) cf.generateCertificate(input);
        System.out.println(cert.getClass().getName());
        System.out.println(cert.getSigAlgName());
        System.out.println(cert.getPublicKey());
        try {
            cert.verify(cert.getPublicKey());
            System.out.println("verified:true");
        } finally {

        }

    }

}
