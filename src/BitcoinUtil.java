import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin.ReceviedAddress;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

public class BitcoinUtil extends BitcoinJSONRPCClient {

	/**
	 * PRIVATE constructor. Only called from startBitcoin()
	 * 
	 * @throws MalformedURLException
	 */
	private BitcoinUtil() throws MalformedURLException {

		super("http://localhost:8332");

	}

	/**
	 * Creates authentication before instantiating class
	 * 
	 * @return a new BitcoinUtil
	 * @throws MalformedURLException
	 * @throws FileNotFoundException
	 */
	public static BitcoinUtil startBitcoin() throws MalformedURLException,
			FileNotFoundException {

		Auth.initAuth();
		return new BitcoinUtil();
	}


	public Transaction waitForTx(String betAddr) {

		List<Transaction> txList = null;
		
		while (true) {
			
			try {
				txList = listTransactions("roulette");
			} catch (BitcoinException e) {
				e.printStackTrace();
				return null;
			}
			
			for(Transaction tx : txList){
				if(tx.address().equals(betAddr))
					return tx;
			}

			/* Wait 20 seconds */
			try {
				Thread.sleep(20 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
