/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.certs;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 
 * @author LiuJian
 * @date 2017年4月4日
 * 
 */
public class X509RfcReader {

    /**
     * 
     * @param args
     * @throws CertificateException
     * @throws SignatureException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * 
     * @see cn.aposoft.tutorial.security.bc.CertificateFactoryStub
     *      使用BouncyCastle的CertificateFactory可以直接读取RFC格式证书
     */
    public static void main(String[] args)
            throws CertificateException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException {
        // 原始文件读取没有问题
        final String fileName = "f:/key/client-rfc.cer";
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileReader inputReader = new FileReader(fileName);

        String base64 = org.apache.commons.io.IOUtils.toString(inputReader);
        base64 = base64.replace("-----BEGIN CERTIFICATE-----", "");
        base64 = base64.replace("-----END CERTIFICATE-----", "");
        System.out.println(base64);
        byte[] rawBytes = Base64.decodeBase64(base64);

        ByteArrayInputStream input = new ByteArrayInputStream(rawBytes);

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
