package blockchain;


import java.security.*;

public class DigitalSignature {
    private static final String ALGORITHM = "SHA256WithRSA";
    private static DigitalSignature instance;
    private Signature signature;
    private KeyPair keyPair;
    
    public DigitalSignature() {
        // Generate key pair only once when the instance is created
        keyPair = generateKeyPair();
    }

    // Singleton pattern: Get the instance of DigitalSignature
    public static DigitalSignature getInstance() {
        if (instance == null) {
            instance = new DigitalSignature();
        }
        return instance;
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
