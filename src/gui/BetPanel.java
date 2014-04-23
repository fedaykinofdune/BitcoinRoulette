package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import core.Bet;
import core.RouletteCore;

public class BetPanel extends JPanel {

	private static final long serialVersionUID = -738579405552840499L;

	private JLabel balance;
	private RouletteCore core;
	private JTextField betInput;
	private JLabel bettingOn;
	
	public BetPanel(RouletteCore core, final RouletteView view){
		
		this.core = core;
		
		setPreferredSize(new Dimension(530, 200));
		setBackground(new Color(230, 230, 230));
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(20, 20, 20, 20);
		
		balance = new JLabel("Balance: "+core.getAccount().getBalance()+ " BTC");
		balance.setFont(new Font("Sans Serif", Font.BOLD,22));
		add(balance,constraints);
		
		bettingOn = new JLabel("Betting on: ");
		bettingOn.setFont(new Font("Sans Serif", Font.BOLD,20));
		constraints.gridy = 1;
		add(bettingOn,constraints);
		
		constraints.gridy = 0;
		
		betInput = new JTextField("Enter Wager");
		betInput.setFont(new Font("Sans Serif", Font.BOLD,25));
		constraints.gridx = 1;
		constraints.weightx=1.;
		constraints.fill=GridBagConstraints.HORIZONTAL;
		betInput.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				betInput.setText("");
				
			}
		});
		add(betInput, constraints);
		
		JButton spin = new JButton("Spin");
		spin.setFont(new Font("Sans Serif", Font.BOLD,30));
		constraints.gridy = 1;
		add(spin,constraints);
		
		
		
		spin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String wagerStr = betInput.getText();
				Double wager;
				
				try{
					wager = Double.parseDouble(wagerStr);
				}
				catch(NumberFormatException e){
					System.err.println("Not a number");
					return;
				}
				
				view.spinWheel(new Bet(wager,true));
			}
		});
	}

	public void updateBalance() {
		
		balance.setText("Balance: "+core.getAccount().prettyPrintBalance()+ " BTC");
		
	}

	public JLabel getBettingOn() {
		return bettingOn;
	}

	public void setBettingOn(String bet) {
		
		bettingOn.setText(bet);
	}
}
