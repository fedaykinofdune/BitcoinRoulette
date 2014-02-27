import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.RawTransaction;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;

public class Roulette {

	private BitcoinUtil bitcoinUtil;

	public Roulette() {

		try {
			bitcoinUtil = BitcoinUtil.startBitcoin();
		} catch (MalformedURLException | FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Couldnt initialize Roulette");
		}

	}

	public void startGame() {

		System.out.println("Starting the Game");

		getInitialBet();

	}

	private void getInitialBet() {

		
		/* Generate new address for user to send bet to */
		String betAddr = "";

		try {
			betAddr = bitcoinUtil.getNewAddress("roulette");
		} catch (BitcoinException e) {
			e.printStackTrace();
			System.out.println("Cant gen new Addr");
			return;
		}

		System.out.println("Send coin to this addr: " + betAddr + "\n");

		/* Wait until user submits a tx */
		Transaction betTx = bitcoinUtil.waitForTx(betAddr);
		RawTransaction betTxRaw = betTx.raw();

		System.out.println("User submitted: " + betTx.amount() + " BTC");
		System.out.println("TxId: " + betTxRaw.txId() + "\n");

		int confirmations = 0;

		/* Wait for 3 confirmations */
		do {

			
			/* Update Tx */
			try {
				betTxRaw = bitcoinUtil.getRawTransaction(betTx.txId());

			} catch (BitcoinException e1) {
				e1.printStackTrace();
			}

			
			/* Update confirmation (errs if conf=0) */
			try {
				confirmations = betTxRaw.confirmations();
			} catch (NullPointerException e) {
				/* Happens if confirmations=0 */
			}

			System.out.println(confirmations + " Confirmations");

			/* Wait 20 seconds */
			try {
				Thread.sleep(20 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (confirmations < 3);

	}

}
