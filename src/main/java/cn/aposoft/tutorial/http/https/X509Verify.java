/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https;

import java.math.BigInteger;
import java.security.Principal;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author LiuJian
 * @date 2017年3月29日
 * 
 */
public class X509Verify {
    
    
    
    
    
    
    
    public static boolean verify(X509Certificate X509certificateRoot, Collection<X509Certificate> collectionX509CertificateChain, X509CRL X509crl,
            String stringTarget) {
        // 获取证书链长度
        int nSize = collectionX509CertificateChain.size();
        // 将证书链转化为数组
        X509Certificate[] arX509certificate = new X509Certificate[nSize];
        collectionX509CertificateChain.toArray(arX509certificate);
        // 声明list，存储证书链中证书主体信息
        ArrayList<BigInteger> list = new ArrayList<BigInteger>();
        // 沿证书链自上而下，验证证书的所有者是下一个证书的颁布者
        Principal principalLast = null;
        for (int i = 0; i < nSize; i++) {// 遍历arX509certificate
            X509Certificate x509Certificate = arX509certificate[i];
            // 获取发布者标识
            Principal principalIssuer = x509Certificate.getIssuerDN();
            // 获取证书的主体标识
            Principal principalSubject = x509Certificate.getSubjectDN();
            // 保存证书的序列号
            list.add(x509Certificate.getSerialNumber());

            if (principalLast != null) {
                // 验证证书的颁布者是上一个证书的所有者
                if (principalIssuer.equals(principalLast)) {
                    try {
                        // 获取上个证书的公钥
                        PublicKey publickey = arX509certificate[i - 1].getPublicKey();
                        // 验证是否已使用与指定公钥相应的私钥签署了此证书
                        arX509certificate[i].verify(publickey);
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            principalLast = principalSubject;

        }
        // 验证根证书是否在撤销列表中
        try {
            if (!X509crl.getIssuerDN().equals(X509certificateRoot.getSubjectDN()))
                return false;
            X509crl.verify(X509certificateRoot.getPublicKey());
        } catch (Exception e) {
            return false;
        }
        // 在当前时间下，验证证书链中每个证书是否存在撤销列表中
        if (X509crl != null) {
            try {
                // 获取CRL中所有的项
                Set<X509CRLEntry> setEntries = new HashSet<>();
                setEntries.addAll(X509crl.getRevokedCertificates());
                if (setEntries != null && !setEntries.isEmpty()) {
                    Iterator<X509CRLEntry> iterator = setEntries.iterator();
                    while (iterator.hasNext()) {
                        X509CRLEntry X509crlentry = (X509CRLEntry) iterator.next();
                        if (list.contains(X509crlentry.getSerialNumber()))
                            return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        // 证明证书链中的第一个证书由用户所信任的CA颁布
        try {
            PublicKey publickey = X509certificateRoot.getPublicKey();
            arX509certificate[0].verify(publickey);
        } catch (Exception e) {
            return false;
        }
        // 证明证书链中的最后一个证书的所有者正是现在通信对象
        Principal principalSubject = arX509certificate[nSize - 1].getSubjectDN();
        if (!stringTarget.equals(principalSubject.getName()))
            return false;
        // 验证证书链里每个证书是否在有效期里
        Date date = new Date();
        for (int i = 0; i < nSize; i++) {
            try {
                arX509certificate[i].checkValidity(date);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static boolean verifySign(X509Certificate X509certificateCA, String sign, String original) {

        try {
            // 获得签名实例
            Signature signature = Signature.getInstance(X509certificateCA.getSigAlgName());
            // 用证书公钥进行初始化
            signature.initVerify(X509certificateCA.getPublicKey());
            // 更新源数据
            signature.update(original.getBytes());
            // 验证数字签名
            return signature.verify(sign.getBytes());
        } catch (Exception e) {
            return false;
        }
    }
}
