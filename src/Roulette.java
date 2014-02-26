import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;


public class Roulette {
	
	private BitcoinUtil bitcoinUtil;
	
	public Roulette(){
		
		try {
			bitcoinUtil =  BitcoinUtil.startBitcoin();
		} catch (MalformedURLException | FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Couldnt initialize Roulette");
		}
		
	}

	public void startGame() {
		
		System.out.println("Starting the Game");
		
		getInitialBet();
		
		
	}
	
	private void getInitialBet(){
		
		String betAddr = "";
		
		try {
			betAddr = bitcoinUtil.getNewAddress("roulette");
		} catch (BitcoinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Send coin to this addr: "+ betAddr);
		
		int conf = 0;
		
		do{
			conf = bitcoinUtil.getNumConf(betAddr); //betADDR
			System.out.println("num confirmations: "+conf);
			
			/* Wait 20 seconds */
			try {
				Thread.sleep(20* 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}while(conf < 3);
		
		
	}

}
