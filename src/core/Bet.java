package core;

public class Bet {
	
	private double wager;
	private boolean black;
	
	public Bet(Double wager, boolean color){
		
		this.wager = wager;
		this.black = color;
	}
	
	
	public double getWager(){
		
		return wager;
	}
	
	public boolean getBlack(){
		
		return black;
	}


	public double getWinnings() {
		return wager*2;
	}


}
