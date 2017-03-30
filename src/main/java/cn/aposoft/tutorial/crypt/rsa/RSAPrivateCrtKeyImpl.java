/**
 * 
 */
package cn.aposoft.tutorial.crypt.rsa;

import java.io.IOException;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateCrtKey;

import sun.security.pkcs.PKCS8Key;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.rsa.RSAKeyFactory;

/**
 * @author LiuJian
 */
@SuppressWarnings("restriction")
public final class RSAPrivateCrtKeyImpl extends PKCS8Key implements RSAPrivateCrtKey {
    private static final long serialVersionUID = -1326088454257084918L;
    private BigInteger n;
    private BigInteger e;
    private BigInteger d;
    private BigInteger p;
    private BigInteger q;
    private BigInteger pe;
    private BigInteger qe;
    private BigInteger coeff;
    static final AlgorithmId rsaId = new AlgorithmId(AlgorithmId.RSAEncryption_oid);

    @SuppressWarnings("resource")
    RSAPrivateCrtKeyImpl(BigInteger n, BigInteger e, BigInteger d, BigInteger p, BigInteger q, BigInteger pe, BigInteger qe, BigInteger coeff)
            throws InvalidKeyException {
        this.n = n;
        this.e = e;
        this.d = d;
        this.p = p;
        this.q = q;
        this.pe = pe;
        this.qe = qe;
        this.coeff = coeff;
        RSAKeyFactory.checkKeyLengths(n.bitLength(), e, 512, 2147483647);

        this.algid = rsaId;
        try {
            DerOutputStream localDerOutputStream = new DerOutputStream();
            localDerOutputStream.putInteger(0);
            localDerOutputStream.putInteger(n);
            localDerOutputStream.putInteger(e);
            localDerOutputStream.putInteger(d);
            localDerOutputStream.putInteger(p);
            localDerOutputStream.putInteger(q);
            localDerOutputStream.putInteger(pe);
            localDerOutputStream.putInteger(qe);
            localDerOutputStream.putInteger(coeff);
            DerValue localDerValue = new DerValue((byte) 48, localDerOutputStream.toByteArray());

            this.key = localDerValue.toByteArray();
        } catch (IOException localIOException) {
            throw new InvalidKeyException(localIOException);
        }
    }

    public String getAlgorithm() {
        return "RSA";
    }

    public BigInteger getModulus() {
        return this.n;
    }

    public BigInteger getPublicExponent() {
        return this.e;
    }

    public BigInteger getPrivateExponent() {
        return this.d;
    }

    public BigInteger getPrimeP() {
        return this.p;
    }

    public BigInteger getPrimeQ() {
        return this.q;
    }

    public BigInteger getPrimeExponentP() {
        return this.pe;
    }

    public BigInteger getPrimeExponentQ() {
        return this.qe;
    }

    public BigInteger getCrtCoefficient() {
        return this.coeff;
    }

    protected void parseKeyBits() throws InvalidKeyException {
        try {
            DerInputStream localDerInputStream1 = new DerInputStream(this.key);
            DerValue localDerValue = localDerInputStream1.getDerValue();
            if (localDerValue.tag != 48) {
                throw new IOException("Not a SEQUENCE");
            }
            DerInputStream localDerInputStream2 = localDerValue.data;
            int i = localDerInputStream2.getInteger();
            if (i != 0) {
                throw new IOException("Version must be 0");
            }
            this.n = getBigInteger(localDerInputStream2);
            this.e = getBigInteger(localDerInputStream2);
            this.d = getBigInteger(localDerInputStream2);
            this.p = getBigInteger(localDerInputStream2);
            this.q = getBigInteger(localDerInputStream2);
            this.pe = getBigInteger(localDerInputStream2);
            this.qe = getBigInteger(localDerInputStream2);
            this.coeff = getBigInteger(localDerInputStream2);
            if (localDerValue.data.available() != 0)
                throw new IOException("Extra data available");
        } catch (IOException localIOException) {
            throw new InvalidKeyException("Invalid RSA private key", localIOException);
        }
    }

    static BigInteger getBigInteger(DerInputStream paramDerInputStream) throws IOException {
        BigInteger localBigInteger = paramDerInputStream.getBigInteger();

        if (localBigInteger.signum() < 0) {
            localBigInteger = new BigInteger(1, localBigInteger.toByteArray());
        }
        return localBigInteger;
    }

}
