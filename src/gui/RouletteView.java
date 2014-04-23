package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import core.Bet;
import core.RouletteCore;

public class RouletteView extends JPanel{

	private Wheel wheel;
	private Board board;
	private BetPanel betPanel;
	private RouletteCore core;
	private Bet currBet;
	

	private static final long serialVersionUID = 1L;

	public RouletteView(RouletteCore core) {


		
		this.core = core;
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;

		board = new Board(this);
		wheel = new Wheel(this);
		betPanel = new BetPanel(core, this);

		constraints.gridheight = 2;
		add(board, constraints);
		constraints.gridheight = 1;
		constraints.gridx = 1;
		
		add(wheel,constraints);
		constraints.gridy = 1;
		add(betPanel,constraints);
		
		

	}
	
	
	public void spinWheel(Bet bet){
		
		currBet = bet;
		
		core.getAccount().setBalance(core.getAccount().getBalance() - bet.getWager());
		updateBalance();
		
		int randDeg = (int)((Math.random()*1000)+ 550);
		wheel.spin(randDeg);
				
	}
	
	public void spinWheelResult(int result){
		
		boolean black = board.getBettingBlack();
		boolean valueInBlack = wheel.getBlacks().contains(result);
		
		System.out.println(valueInBlack ? "black" : "red");
		
		/* won bet */
		if(!(Boolean.logicalXor(black, valueInBlack))){
			
			JOptionPane.showMessageDialog(Main.frame, "Landed on: " + result+"\nYou won "+currBet.getWinnings()+ " BTC!");
			core.getAccount().setBalance(core.getAccount().getBalance()+currBet.getWinnings());
			updateBalance();
		}
		else{
			JOptionPane.showMessageDialog(Main.frame, "Landed on: " + result+"\nYou lost!");
		}
		
		
	}
	
	public BetPanel getBetPanel(){
		
		return betPanel;
	}
	

	public void updateBalance(){
		
		betPanel.updateBalance();
	}

	/**
	 * Paints ground with bgImg
	 */
	public void paintComponent(Graphics page) {
		super.paintComponent(page);

		page.drawImage(Main.BGIMG, 0, 0, null);
	}

	

}
