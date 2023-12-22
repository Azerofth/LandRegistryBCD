package blockchain;

import java.io.*;
import java.security.Key;
import java.util.Base64;
import java.util.LinkedList;
import javax.crypto.Cipher;

import com.google.gson.GsonBuilder;

public class Blockchain {
	private static LinkedList<Block>db = new LinkedList<>();
	private static Cipher cipher;
	private static Blockchain _instance;
	public static Blockchain getInstance(String chainFile) {
		if (_instance == null)
			_instance = new Blockchain(chainFile);
		return _instance;
	}
	public String chainFile;
	
	public static String encrypt(String data, Key key) throws Exception
	{
		String cipherText = null;
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherBytes = cipher.doFinal(data.getBytes());
		cipherText = Base64.getEncoder().encodeToString(cipherBytes);
		return cipherText;
	}
	
	public Blockchain(String chainFile) {
		super();
		this.chainFile = chainFile;
		try {
            // Create a Cipher object with the AES algorithm
            cipher = Cipher.getInstance("AES");
            System.out.println("> Blockchain object is created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void genesis() {
		Block genesis = new Block ("0", "Root");
		db.add(genesis);
		persist();
	}
	
	public void nextBLock(Block newBlock)
	{
		db = get();
		db.add(newBlock);
		persist();
	}
	
	public LinkedList<Block> get(){
		try(FileInputStream fin = new FileInputStream(this.chainFile);
				ObjectInputStream in = new ObjectInputStream(fin);
				){
			return (LinkedList<Block>)in.readObject();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void persist()
	{
		try(FileOutputStream fout = new FileOutputStream(this.chainFile);
				ObjectOutputStream out = new ObjectOutputStream(fout);){
			out.writeObject(db);
			System.out.println(">> Master file is updated!");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void persistTextFile() {
        try (FileWriter writer = new FileWriter(this.chainFile + ".txt")) {
            String chain = new GsonBuilder().setPrettyPrinting().create().toJson(db);
            writer.write(chain);
            System.out.println(">> Text file is updated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public void distribute()
	{
		String chain = new GsonBuilder().setPrettyPrinting().create().toJson(db);
//		System.out.println(chain);
	}
	private static String masterFolder = "master";
	private static String fileName=masterFolder+"/chain.bin";
	
	public static void createBlockchain(String data) {
	Key secretKey = PredefinedCharsSecretKey.create();
	Blockchain bc = Blockchain.getInstance(fileName);
    String encryptedChain = null;
	try {
		encryptedChain = encrypt(data, secretKey);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	if (!new File(masterFolder).exists()) {
		System.err.println("> creating Blockchain binary!");
		new File(masterFolder).mkdir();
		bc.genesis();
		}
	else {
		String line1=encryptedChain;
		TransactionCollection tranxLst = new TransactionCollection();
		tranxLst.add(line1);
		String previousHash = bc.get().getLast().getHeader().getCurrentHash();
		Block b1 = new Block(previousHash, "Root");
		b1.setTransactions(tranxLst);
		bc.nextBLock(b1);
//		System.out.println(b1);
		bc.distribute();
		bc.persistTextFile();
	}
}
}
