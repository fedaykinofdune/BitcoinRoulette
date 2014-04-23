package core;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin.ReceviedAddress;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

public class BitcoinUtil extends BitcoinJSONRPCClient {

	
	private boolean cancelled;
	
	/**
	 * @throws MalformedURLException
	 */
	public BitcoinUtil() throws MalformedURLException {

		super("http://localhost:8332");
		cancelled = false;
	}



	public Transaction waitForTx(String betAddr) {

		List<Transaction> txList = null;
		
		while (true) {
			
			System.out.println("Waiting for BTC at " + betAddr);
			
			if(cancelled){
				System.out.println("TX cancelled");
				cancelled = false;
				return null;
			}
			
			try {
				txList = listTransactions("roulette");
			} catch (BitcoinException e) {
				e.printStackTrace();
				return null;
			}
			
			for(Transaction tx : txList){{
				
				if(tx.address().equals(betAddr))
					return tx;
			}
			}

			/* Wait 10 seconds */
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void cancelWaitTx(){
		cancelled = true;
	}
	
	public String getNewFundAddr() {

		/* Generate new address for user to send bet to */
		String betAddr = "FAIL";

		try {
			betAddr = getNewAddress("roulette");
		} catch (BitcoinException e) {
			e.printStackTrace();
			System.out.println("Cant gen new Addr");
		}
		
		return betAddr;
	}

}
