package core;

import gui.FundPanel;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.SwingWorker;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.RawTransaction;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;

public class Dealer {

	
	private final String dealerFundAddr = "17rR8CFtMooeCLeBSeGpxpHXo6ETSLEkY9";
	public Transaction betTx;
	private int betConfirmations;
	private final int reqConfirmations = 0;
	private RouletteCore core;

	public Dealer(RouletteCore core) {
		
		this.core = core;
		betConfirmations = 0;
		
	}

	public String getDealerAddr() {
		return dealerFundAddr;
	}
	

	
	
	public Transaction getBetTx(){
		
		return betTx;
	}
	

	public int getBetConfirmations() {
		return betConfirmations;
	}
	
	public int getReqConf() {

		return reqConfirmations;
	}

	private Transaction waitForTxToAppear(String betAddr){
		
		return core.getBitcoinUtil().waitForTx(betAddr);
	}
	
	public void cashOut(double amt, String address) throws BitcoinException{
		
		core.getBitcoinUtil().sendToAddress(address, amt, "Enjoy your winnings!");
		
	}


	public void waitForConfirmations(final FundPanel fundPan){
		
		/* Initialize new thread to wait for confirmations */
		new SwingWorker<Object, Integer>() {
			

			@Override
			protected Object doInBackground() throws Exception {

				/* Waits for user to send BTC */
				betTx = waitForTxToAppear(core.getAccount().getAddr());
				
				RawTransaction betTxRaw = betTx.raw();

				do {
					
					/* Wait 10 seconds */
					try {
						Thread.sleep(10 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}


					/* Update Tx */
					try {
						betTxRaw = core.getBitcoinUtil().getRawTransaction(betTx.txId());

					} catch (BitcoinException e1) {
						e1.printStackTrace();
					}

					/* Update confirmation (errs if conf=0) */
					try {
						betConfirmations = betTxRaw.confirmations();
						fundPan.updateConfirmationPanel(betConfirmations);
					} catch (NullPointerException e) {
						fundPan.updateConfirmationPanel(0);
						/* Happens if confirmations=0 */
					}



				} while (betConfirmations < reqConfirmations);
				
				
				return null;

			}

			/* When we get an update on number of confirmations */
			@Override
			protected void process(List<Integer> conf) {
				
				int max = -1;
				
				
				for(int i: conf)
					if(i > max)
						max = i;
				

			}

		}.execute();
		
		
	}
	
	
}
