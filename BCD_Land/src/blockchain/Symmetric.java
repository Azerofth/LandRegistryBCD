package blockchain;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;

public class Symmetric {
	Cipher cipher;
	Symmetric(String algorithm) throws Exception{
		cipher = Cipher.getInstance(algorithm);	
	}
	public Symmetric()throws Exception{
		this("AES");
	}
}
