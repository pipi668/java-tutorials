/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.security.bc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;

/**
 * @author LiuJian
 * @date 2017年4月15日
 * 
 */
public class CertificateFactoryStub {

    /**
     * 读取RFC格式的cert证书内容
     * 
     * @param args
     * @throws CertificateException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws CertificateException, FileNotFoundException {
        CertificateFactory cf = new CertificateFactory();
        InputStream in = new FileInputStream("f:/key/client-rfc.cer");
        Certificate cert = cf.engineGenerateCertificate(in);
        System.out.println(cert);
    }

}
