package blockchain;

public class Genesis {
	
	public void genesisCreation() {
		Block genesis = new Block("0","0");
		System.out.println(genesis);
		String tranx1 = "Test";
		TransactionCollection tranxLst = new TransactionCollection(); 
		tranxLst.add(tranx1);
		Block b1 = new Block (genesis.getHeader().getCurrentHash(), tranxLst.getMerkleRoot());
		b1.setTransactions (tranxLst);
		
	}
	
	
}
