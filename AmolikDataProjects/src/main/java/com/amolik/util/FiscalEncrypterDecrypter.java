package com.amolik.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.log4j.Logger;

import com.amolik.formfiling.FiscalProcessor;

public class FiscalEncrypterDecrypter {

	private static final Logger logger = Logger.getLogger(FiscalEncrypterDecrypter.class);
	private static KeySpec keySpec;
	private static SecretKey sharedkey;
	private static byte [] ivBytes;
	private static byte[] keyBytes;
	private static String systemId;
	private static String keyString;
	private static String ivString;

	static {
		int keySize = 168; // correct size for twenty bytes
		int ivSize = 8;  // correct size for 8 bytes

		try {
			systemId= AmolikProperties.getProperty("fiscalprocessor.crypto.systemId");
			//"S-1-5-21-570459828-949367136-589045457-1001";
			keyString = AmolikProperties.getProperty("fiscalprocessor.crypto.keyString");
			//"ka2MjgouSqkmIz9GzAdEeorB";
			ivString  = AmolikProperties.getProperty("fiscalprocessor.crypto.ivString");
			//"3K2kW7Wk";

			keyBytes = keyString.getBytes("UTF-8");
			ivBytes = ivString.getBytes("UTF-8");
			keySpec = new DESedeKeySpec(keyBytes);
			sharedkey = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);

		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(logger.isDebugEnabled()) {

			logger.debug("key="+keyBytes.toString());
			logger.debug("iv="+ivBytes.toString());
			logger.debug("ivBytes size="+ivBytes.length);
		}
	}

	public static void main(String [] args) throws Exception {

		String plainText = 
				"21 months";
		//"Üñited Staté of America";

		String encryptedText ="[5RTgkl9ZxZvkZUdQoAwdYOes17h1CO8ayzQfZtnxf4R8R+aDqZDi4zF0UQ95kb2VXw4/uB5MpIRO8Jrq/JzDczJNG3zyHPNkVbLvJSoM2fcJwZtwxWbgdgjJlSJ5iwmuamybnK7MTHwoBj8av5fcpxKpL0ASjnQa7PB3O2Doi4Q=]";
			//	"[v/VoxTgdvFHDLunz/7KN+ARDJd/hDrm6r0UrRyTYMcJlnfXrKbJB7k6TLXuw56Ygks+gn12U1eTEB7mX+0B1CQ/93xG1U3QMWBoNSFLSsOk3lexufEgYXC5C+rhNnRVxjJpLMm1Z5jNWVHnqh9B3WibHFaneq35nQGWGppXI3LM3P1ZvfVylvw==]";

				//"[j/YmsO3lviCV9jrZdB7/BVIIZ+UUXwCs78dThn4O5zXWpeTu9NqfHM3UmJTRmWghCAQSPQ3I6g8It6lgIJ1yxqhTXw5viJIg2e6Kext2Nwg9tDrlU1JtEx9GDFVCCVgnsYIm15u8uvenLjNZcOGC7eHB46TLGJzepMxgzndmmkzYrYmDg4r2/19QEvj6wMHl]";
		//"[/XgEkkmnmyjsPZTM10QNR9tQgPLXzfRhaDwsCInPjIrsizIYm/ERv8hoSN7fBJaSaE0mZfToMmodLujWiUqQlULkxXb5S6FgMPw5mGEvLKfpQH4zVXwoC2VYufmyfrp5l4zzuZyrdwog8gNslJ8YKh0iT2+ufyqDBuXhDqOeBc6lJipa1Txq6w==]";
		//"[HOeiNKHZVEGkFZ8iEFiBDzIBEjghxa86FpR/yMOITYkuTDEf18SfTUt3bkFTlHryUva+Wn63ozExev0GTE99BALXVQqTpe74gwJXNSDpzQZnSLD51rjX589zCLzvzKAsjXSBaOWuFwYUwMrewsSXez58gQBV/6ucgEMbW9vVMfs=]";
		//"[Y9DCFFIGOJqsXyUa6ye8joEfVzFQTUXidnLpA49yB0SYcoI9+mkHxK5Q50LAZN4P75bZmGSTSBeaq/drdizM3b+2YlGqfkZdGFZI96JnNcNJPizEd/NaKJWTQGk8t8Uyet0XRMDo+MdxX0BLPHhej3dBUJz2RMEk0nZSGK99GXg=]";
		String encrypted = encryptFiscalText(plainText);
		String decrypted = DecryptFiscalText(encryptedText);
		
		if(logger.isInfoEnabled()){
			
			logger.info(plainText);
			logger.info("|Encrypted="+encrypted);
			logger.info(encryptedText);
			logger.info("|Decrypted="+decrypted);
			
		}



	}

	public static String encryptFiscalText(String plainText)
			throws GeneralSecurityException {

		// Algo 1. Add plainText + "#!#" + systemId
		// 2. convert databytes to UTF-16LE
		// 3. convert 64base
		// 4. encrypted using 3des.
		// 5. add [ to begining of encrypted and ] to end of crypted
		String encryptedText = "";
		plainText = plainText + "#!#" + systemId;


		try {
			byte[] dataBytes = plainText.getBytes("UTF-16LE");
			encryptedText = Encrypt(dataBytes);

			if(logger.isDebugEnabled())
				logger.debug("plaintext="+plainText);
			logger.debug("plaintextFromUTF-16LE="+new String(dataBytes,"UTF-16LE"));
			logger.debug("plaintextBytesUTF-16LE="+dataBytes.toString());
			logger.debug("Encrypted="+"["+encryptedText+"]");


		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "["+encryptedText+"]";
	}
	public static String Encrypt(byte[] dataBytes) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, sharedkey, new IvParameterSpec(ivBytes));

		return new sun.misc.BASE64Encoder().encode(cipher.doFinal(dataBytes));
	}

	public static byte[] Decrypt(String val) throws GeneralSecurityException, IOException {
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, sharedkey, new IvParameterSpec(ivBytes));

		return cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(val));
	}

	public static String DecryptFiscalText(String encryptedText)
	{
		// Algo 1. remove square brackets
		// 2. decrypted using 3des & decode base 64
		// 3. convert back to UTF-16LE to normal string
		// 4. split the string and separate plain text from system id.

		String decryptedText = "";
		String encrytedWithoutBrackets = encryptedText.replace("[", "").replace("]", "");
		String plainTextAndSysId="";

		try {
			byte[] encryptedUTF16LEByteArray = Decrypt(encrytedWithoutBrackets);

			plainTextAndSysId = new String(encryptedUTF16LEByteArray, "UTF-16LE");
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		if(logger.isDebugEnabled()) {
			
			logger.debug("plainTextAndSysId="+plainTextAndSysId);
		}

		String[] splited = plainTextAndSysId.split("#!#");
		if(splited.length>0){

			decryptedText = splited[0];
		}
		return decryptedText;
	}
}