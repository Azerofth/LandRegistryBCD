package blockchain;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class DigitalSignature {
	private static final String ALGORITHM = "SHA256WithRSA";
    private Signature sig;
    private KeyPair keyPair;
 
    public DigitalSignature() {
        try {
            sig = Signature.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
 
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
 
    public byte[] getSignature(String text, PrivateKey key) {
        try {
            sig.initSign(key);
            sig.update(text.getBytes());
            return sig.sign();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    public boolean isTextAndSignatureValid(String text, byte[] signature, PublicKey key) {
        try {
            sig.initVerify(key);
            sig.update(text.getBytes());
            return sig.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }
 
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }
}
