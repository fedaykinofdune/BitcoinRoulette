package core;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class RouletteCore {
	
	private BitcoinUtil bitcoinUtil;
	private Account account;
	private Dealer dealer;

	public RouletteCore(){
		
		try {
			Auth.initAuth();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			bitcoinUtil = new BitcoinUtil();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		setAccount(new Account());
		setDealer(new Dealer(this));
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Dealer getDealer() {
		return dealer;
	}

	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
	
	public void setPlayerNewFundAddr(){
		
		account.setAddress(bitcoinUtil.getNewFundAddr());
	}
	
	
	public BitcoinUtil getBitcoinUtil(){
		
		return bitcoinUtil;
	}
}
