package blockchain;

import java.security.Key;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

public class PredefinedCharsSecretKey {
	private static final String ALGORITHM = "AES";
	private static final String SECRET_CHARS = "asddsaqweewqpbnmzxccz098123765543";
	
	public static Key create()
	{
		int keySize = 16;
		return new SecretKeySpec (Arrays.copyOf(SECRET_CHARS.getBytes(), keySize), ALGORITHM);
		
	}
}
