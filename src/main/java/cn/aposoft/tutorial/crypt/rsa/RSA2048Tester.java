/**
 * 
 */
package cn.aposoft.tutorial.crypt.rsa;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAKeyGenParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import cn.aposoft.tutorial.crypt.rsa.RSAUtils.KeyPairEntry;
import sun.security.jca.JCAUtil;

/**
 * @author LiuJian
 *
 */
@SuppressWarnings("restriction")
public class RSA2048Tester {
	private String publicKey;
	private String privateKey;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int keyLength = 2048;
		// createKeyPair();
		new RSA2048Tester().generateKeyPair();
		//
		if (true)
			return;
		RSA2048Tester tester = new RSA2048Tester();
		KeyPairEntry entry = tester.createKeyPair(keyLength);
		long beginPublic = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			tester.testEncryByPublickey(entry, keyLength);
		}
		long endPublic = System.currentTimeMillis();
		System.out.println("public:" + (endPublic - beginPublic));
		long beginPrivate = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			tester.testEncryByPrivatekey(entry, keyLength);
		}
		long endPrivate = System.currentTimeMillis();
		System.out.println("private:" + (endPrivate - beginPrivate));
		try {
			tester.testsign(entry, keyLength);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
	}

	public void testsign(KeyPairEntry entry, int keyLength)
			throws InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException, SignatureException {
		System.out.println("*****************************************************");
		System.out.println("私钥加密——公钥解密");
		String source = "这是一行测试RSA数字签名的无意义文字";
		System.out.println("原文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtils.encryptByPrivateKey(data, entry.getEncodedPrivateKey(), keyLength);
		System.out.println("加密后：\r\n" + new String(encodedData));
		byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData, entry.getEncodedPublicKey(), keyLength);
		String target = new String(decodedData);
		System.out.println("解密后: \r\n" + target);
		System.out.println("私钥签名——公钥验证签名");
		String sign = RSAUtils.sign(encodedData, entry.getEncodedPrivateKey());
		System.out.println("签名:\r\n" + sign);
		boolean status = RSAUtils.verify(encodedData, entry.getEncodedPublicKey(), sign);
		System.out.println("验证结果:" + status);
	}

	private static byte[] createByteArray(int length) {
		byte[] bytes = new byte[length];
		for (int i = 0; i < i; i++) {
			bytes[i] = (byte) ('a' + i % 26);
		}

		return bytes;
	}

	private KeyPairEntry createKeyPair(int keyLength) {
		try {
			KeyPairEntry keyMap = RSAUtils.generateKeyPair(keyLength);
			publicKey = keyMap.getEncodedPublicKey();
			privateKey = keyMap.getEncodedPrivateKey();
			System.out.println("PublicModulus:" + keyMap.getPublicKey().getModulus().toString());
			System.out.println("PublicExponent:" + keyMap.getPublicKey().getPublicExponent().toString());

			System.out.println("public key:");
			System.out.println(keyMap.getPublicKey().toString());
			System.out.println();
			System.out.println("private key:");
			System.out.println(keyMap.getPrivateKey().toString());
			System.out.println();
			if (keyMap.getPrivateKey() instanceof RSAPrivateCrtKey) {
				RSAPrivateCrtKey crtKey = (RSAPrivateCrtKey) keyMap.getPrivateKey();
				System.out.println("PublicExponent" + crtKey.getPublicExponent().toString());
				System.out.println("PrivateExponent:" + crtKey.getPrivateExponent().toString());
				System.out.println("PrivateModulus:" + crtKey.getModulus().toString());
			}

			System.out.println("公钥: " + publicKey.length() + "\n\r" + publicKey);
			System.out.println("私钥：" + privateKey.length() + " \n\r" + privateKey);
			return keyMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	void createKeyPair() {
		KeyPairEntry keyMap = createKeyPair(1024);
		publicKey = keyMap.getEncodedPublicKey();
		privateKey = keyMap.getEncodedPrivateKey();

	}

	final int keySize = 2048;
	private SecureRandom random;
	private BigInteger publicExponent = RSAKeyGenParameterSpec.F4;;

	public void testEncryByPrivatekey(KeyPairEntry keyPair, int keyLength) {
		// rsa 算法的加密block大小为 keyLength / 8 -11(inclusive)
		int maxDecryptedBlockLengh = keyLength / 8;
		int maxEncryptedBlockLength = maxDecryptedBlockLengh - 11;
		for (int blockSize = 1; blockSize <= maxEncryptedBlockLength; blockSize++) {

			byte[] original = createByteArray(blockSize);
			try {
				// System.out.println("original length:" + blockSize);
				byte[] encoded = RSAUtils.encryptByPrivateKey(original, keyPair.getEncodedPrivateKey(), blockSize);
				// byte[] decoded = RSAUtils.decryptByPublicKey(encoded,
				// keyPair.getEncodedPublicKey(), encoded.length);
				// System.out.println("encoded:" + encoded.length);
				// System.out.println(Arrays.equals(original, decoded));
			} catch (InvalidKeyException |
			// NoSuchAlgorithmException |
					InvalidKeySpecException
					// | NoSuchPaddingException
					| IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
				System.out.println("break.");
				break;
			}
		}
	}

	public void testEncryByPublickey(KeyPairEntry keyPair, int keyLength) {
		// rsa 算法的加密block大小为 keyLength / 8 -11(inclusive)
		int maxDecryptedBlockLengh = keyLength / 8;
		int maxEncryptedBlockLength = maxDecryptedBlockLengh - 11;
		for (int blockSize = 1; blockSize <= maxEncryptedBlockLength; blockSize++) {

			byte[] original = createByteArray(blockSize);
			try {
				// System.out.println("original length:" + blockSize);
				byte[] encoded = RSAUtils.encryptByPublicKey(original, keyPair.getEncodedPublicKey(), blockSize);
				// byte[] decoded = RSAUtils.decryptByPrivateKey(encoded,
				// keyPair.getEncodedPrivateKey(), encoded.length);
				// System.out.println("encoded:" + encoded.length);
				// System.out.println(Arrays.equals(original, decoded));
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
					| IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
				System.out.println("break.");
				break;
			}
		}
	}

	public KeyPair generateKeyPair() {

		int i = this.keySize + 1 >> 1;
		int j = this.keySize - i;
		System.out.println("i:" + i + ",j:" + j);
		if (this.random == null) {
			try {
				this.random = SecureRandom.getInstance("SHA1PRNG");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			if (this.random == null)
				this.random = JCAUtil.getSecureRandom();
		}

		BigInteger e = this.publicExponent;
		System.out.println("e:" + e);
		// p q 是两个大质数 并且最终 p>=q
		BigInteger p;
		BigInteger q;
		// p-1
		BigInteger p_1;
		// n=p*q
		BigInteger n;
		// q-1
		BigInteger q_1;
		// (p-1)*(q-1)
		BigInteger p_1mq_1;

		do {
			p = BigInteger.probablePrime(i, this.random);
			do {
				q = BigInteger.probablePrime(j, this.random);
				// swap p and q
				if (p.compareTo(q) < 0) {
					BigInteger t = p;
					p = q;
					q = t;
				}

				n = p.multiply(q);
			} while (n.bitLength() < this.keySize);

			p_1 = p.subtract(BigInteger.ONE);
			// q-1
			q_1 = q.subtract(BigInteger.ONE);
			// (p-1)*(q-1)
			p_1mq_1 = p_1.multiply(q_1);
		}
		// 要求 e 和(p_1)*(q-1) 互质
		// 最大公约数为1 greatest common divisor
		while (!(e.gcd(p_1mq_1).equals(BigInteger.ONE)));
		System.out.println("n:" + n);
		System.out.println("p:" + p);
		System.out.println("q:" + q);
		System.out.println("(p-1)*(q-1):" + p_1mq_1);
		// (d*e)mod((p-1)*(q-1))=1
		// mod inverse means : (d*e) mod ((p-1)*(q-1)) = 1
		BigInteger d = e.modInverse(p_1mq_1);

		System.out.println(" (d * e) mod ((p-1) * (q-1)) = 1 \r\nd:" + d);
		BigInteger pe = d.mod(p_1);
		System.out.println("pe:" + pe);
		BigInteger qe = d.mod(q_1);
		System.out.println("qe:" + qe);

		// : coeff =
		BigInteger coeff = q.modInverse(p);
		System.out.println(" ( q * coeff ) mod p =1 , \r\ncoeff:" + coeff);
//		try {
//			RSAPublicKey localRSAPublicKeyImpl =RSAPublicKeyImpl.newKey(KeyType.RSA, null, n, e);
//			
//			RSAPrivateCrtKey localRSAPrivateCrtKeyImpl = new RSAPrivateCrtKeyImpl//
//			(n, e, d, p, q, pe, qe, coeff);
//
//			return new KeyPair(localRSAPublicKeyImpl, localRSAPrivateCrtKeyImpl);
//		} catch (InvalidKeyException localInvalidKeyException) {
//			throw new RuntimeException(localInvalidKeyException);
//		}
		return null;
	}
}
