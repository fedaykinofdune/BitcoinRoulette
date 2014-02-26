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
	public static BitcoinUtil startBitcoin() throws MalformedURLException,FileNotFoundException {

		Auth.initAuth();
		return new BitcoinUtil();
	}

	
	public int getNumConf(String addr) {

		List<ReceviedAddress> allTxRec = null;

		try {
			allTxRec = listReceivedByAddress(0, false);
		} catch (BitcoinException e) {
			e.printStackTrace();
			return -1;
		}

		for (ReceviedAddress r : allTxRec) {

			if (r.address().equals(addr)) {
				return r.confirmations();
			}

		}

		return -1;

	}

}
