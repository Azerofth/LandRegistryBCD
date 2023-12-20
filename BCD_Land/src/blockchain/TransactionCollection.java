package blockchain;

import java.util.ArrayList;
import java.util.List;

public class TransactionCollection {
private final int SIZE = 10;
	
	public String merkleRoot;
	
	public List<String> tranxLst;
	
	public void complete() {
		MerkleTree mt = MerkleTree.getInstance(tranxLst);
		mt.build();
		this.merkleRoot = mt.getRoot();
		
	}
	
	public TransactionCollection() { 
		tranxLst = new ArrayList<>(SIZE); 
		}
	
	public void add(String tranx) { 
		tranxLst.add(tranx); 
		}
	
	public String getMerkleRoot() { 
		return this.merkleRoot; 
		}
	
	public List<String> getTransactionList(){
		return this.tranxLst;
	}
}
