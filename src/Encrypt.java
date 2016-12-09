import java.io.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;


public class Encrypt {
	
	PublicKey publicKey;
	PrivateKey privateKey;
	byte[] data;
	
	public void createKey() {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		
		kpg.initialize(1024);
		KeyPair kp = kpg.genKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void encrypt(String in, String out){
		
		try {
		FileInputStream file = new FileInputStream(in);
        byte[] text = new byte[file.available()];
        file.read(text);
        file.close();
		
		
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] ciphertext = cipher.doFinal(text);
		FileOutputStream cipherfile = new FileOutputStream(out);
		cipherfile.write(ciphertext);
		cipherfile.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void decrypt(String in, String out){
		try {
			FileInputStream cipherfile = new FileInputStream(in);
	        byte[] ciphertext = new byte[cipherfile.available()];
	        cipherfile.read(ciphertext);
	        cipherfile.close();
			
			
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			byte[] text = cipher.doFinal(ciphertext);
			FileOutputStream file = new FileOutputStream(out);
			file.write(text);
			file.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
}