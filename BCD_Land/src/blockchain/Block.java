package blockchain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Block {
	//create blocks here
	/* relationship implementation */ 
	public Header header;
	public TransactionCollection tranxLst;

	public Block(String previousHash, String root) {
		/* composition relationship */
		this.header = new Header();
		header.setTimestamp( new Timestamp(System.currentTimeMillis()).getTime() );
		header.setPreviousHash(previousHash);
		String info = String.join("+", 
				Integer.toString(header.getIndex()), 
				header.getPreviousHash(),
				root,
				Long.toString(header.getTimestamp()) 
				);
		header.currentHash = Hasher.sha256ns(info);
	}
	
		
	public TransactionCollection getTransactions() 
	{ 
		return this.tranxLst; 
	}
	
	/* aggregation relationship */ 
	public void setTransactions( TransactionCollection tranxLst ) 
	{ 
		this.tranxLst = tranxLst;
	}
	
	public Header getHeader() 
	{ 
		return this.header;
	}
	
	@Override
	public String toString() { 
		return "Block [header=" + header + ", tranxLst=" + tranxLst + "]";
	}
	
	/* define inner class for the Header */
	public class Header implements Serializable{
		public int index;
		public String currentHash, previousHash;
		public long timestamp;
		
		@Override
		public String toString() {
			return "Header [index=" + index + ", currentHash=" + currentHash + 
					", previousHash=" + previousHash + ", timestamp=" + timestamp + "]";
		}
		
		//getset methods
		public int getIndex() {
			return index;
		}
		
		public void setIndex(int index) {
			this.index = index;
		}
		
		public String getCurrentHash() { 
			return currentHash;
		}
		
		public void setCurrentHash(String currentHash) { 
			this.currentHash = currentHash;
		}
		
		public String getPreviousHash() {
			return previousHash;
		}
		
		public void setPreviousHash(String previousHash) {
			this.previousHash = previousHash;
		}
		
		public long getTimestamp() { 
			return timestamp;
		}
		
		public void setTimestamp (long timestamp) {
			this.timestamp = timestamp;
		}
		
	}
}