package blockchain;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Hasher {
	
	public static byte[] generate() {
		SecureRandom sr = new SecureRandom();
		byte[] b = new byte[64];
		sr.nextBytes(b);
		return b;
	}
	
	//with salt
	public static String sha256(String input, byte[] salt) {
		return hash(input,salt,"SHA-256");
	}
	
	//without salt
	public static String sha256ns(String input)
	{
		return hashns( input, "SHA-256" );
	}
	
	//with salting
	public static String hash(String input, byte[] salt, String algorithm)
	{
	String hashCode = "";
	try {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(input.getBytes() );
		md.update(salt);
		byte[] hashBytes = md.digest();
		hashCode = Base64.getEncoder().encodeToString(hashBytes);
	} catch (Exception e) {
	e.printStackTrace();
	}
	return hashCode;
	}
	
	//without salting
	private static String hashns(String input, String algorithm)
	{
	String hashCode = "";
	try {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update( input.getBytes() );
		//digesting...
		byte[] hashBytes = md.digest();
		//convert the byte[] to String
		//1)
		hashCode = Base64.getEncoder().encodeToString(hashBytes);
		//2) hex format output - recommended!
		//hashCode = Hex.encodeHexString(hashBytes);
	} catch (Exception e) {
	e.printStackTrace();
	}
	return hashCode;
	}
	
}
