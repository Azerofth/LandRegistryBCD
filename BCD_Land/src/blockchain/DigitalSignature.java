package blockchain;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;

public class DigitalSignature {
    private static final String ALGORITHM = "SHA256WithRSA";
    private static final String KEY_PAIR_FILE = "keypair.dat";
    
    private static DigitalSignature instance;
    private Signature signature;
    private KeyPair keyPair;
    
    private DigitalSignature() {
        // Load existing key pair or generate a new one
        keyPair = loadKeyPair(KEY_PAIR_FILE);
        if (keyPair == null) {
            keyPair = generateKeyPair();
            saveKeyPair(KEY_PAIR_FILE, keyPair);
        }
    }

    // Singleton pattern: Get the instance of DigitalSignature
    public static DigitalSignature getInstance() {
        if (instance == null) {
            instance = new DigitalSignature();
        }
        return instance;
    }

    // Load key pair from file
    private KeyPair loadKeyPair(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (KeyPair) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null; // File not found or error loading, return null
        }
    }

    // Save key pair to file
    private void saveKeyPair(String fileName, KeyPair keyPair) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(keyPair);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving key pair.");
        }
    }
    
    // Generate key pair for digital signature
    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating key pair.");
        }
    }

    // Generate digital signature for the transaction
    public byte[] generateDigitalSignature(String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        signature = Signature.getInstance(ALGORITHM);
        signature.initSign(keyPair.getPrivate());
        signature.update(data.getBytes());
        return signature.sign();
    }

    // Verify digital signature during transaction approval
    public boolean verifyDigitalSignature(String data, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initVerify(keyPair.getPublic());
        signature.update(data.getBytes());
        return signature.verify(signatureBytes);
    }

    // Getter for public key
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    // Setter for key pair (for deserialization)
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
