package core;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;

public class Fund {

	BitcoinUtil bitcoinUtil;
	String fundAddr;

	public Fund(BitcoinUtil util) {
		bitcoinUtil = util;
		fundAddr = "SAfasd45fsdafs34534adasdf";
	}

	public String getAddr() {
		return fundAddr;
	}

	public Fund getFundAddr() {

		/* Generate new address for user to send bet to */
		String betAddr = "FAIL";

		try {
			betAddr = bitcoinUtil.getNewAddress("roulette");
		} catch (BitcoinException e) {
			e.printStackTrace();
			System.out.println("Cant gen new Addr");
		}

		fundAddr = betAddr;
		
		return this;
	}

}
