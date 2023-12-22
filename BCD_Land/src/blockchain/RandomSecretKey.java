package blockchain;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

public class RandomSecretKey {
	private static final String ALGORITHM = "AES";

	public static Key create()
	{
		short keySize = 256;
		try {
			KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
			kg.init(keySize, new SecureRandom());
			return kg.generateKey();
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
