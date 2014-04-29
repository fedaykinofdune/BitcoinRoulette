package core;

public class Account {

	private double balance; 
	private String address;
	
	public Account(){
		
		address = "UNINIT";
		balance = 0;
		
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		
		this.balance = balance;
	}
	
	public String getAddr(){
		
		return address;
	}
	
	public String prettyPrintBalance(){
		
		return String.format("%1$,.4f", balance);
	}

	public void setAddress(String newFundAddr) {
		
		address = newFundAddr;
		
	}

}
