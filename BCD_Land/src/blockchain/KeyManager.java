//package blockchain;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.security.*;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//
//public class KeyManager {
//    private static final String ALGORITHM = "SHA256WithRSA";
//    public static final String PUBLIC_KEY_PATH = "path/to/public_key.pem";
//    public static final String PRIVATE_KEY_PATH = "path/to/private_key.pem";
//
//    public static PublicKey getPublicKey(String path) throws Exception {
//        byte[] keyBytes = readKeyBytes(path);
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//        return KeyFactory.getInstance(ALGORITHM).generatePublic(spec);
//    }
//
//    public static PrivateKey getPrivateKey(String path) throws Exception {
//        byte[] keyBytes = readKeyBytes(path);
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//        return KeyFactory.getInstance(ALGORITHM).generatePrivate(spec);
//    }
//
//    public static void generateKeyPair(String publicKeyPath, String privateKeyPath) {
//        KeyPair keyPair = generateKeyPair();
//        put(keyPair.getPublic().getEncoded(), publicKeyPath);
//        put(keyPair.getPrivate().getEncoded(), privateKeyPath);
//    }
//
//    private static KeyPair generateKeyPair() {
//        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
//            keyPairGenerator.initialize(2048); // You can choose your key size
//            return keyPairGenerator.generateKeyPair();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static byte[] readKeyBytes(String path) throws Exception {
//        return Files.readAllBytes(Paths.get(path));
//    }
//
//    private static void put(byte[] keyBytes, String path) {
//        File f = new File(path);
//        f.getParentFile().mkdirs();
//        try {
//            Files.write(Paths.get(path), keyBytes, StandardOpenOption.CREATE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
