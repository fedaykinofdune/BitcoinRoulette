package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ConfirmationPanel extends JPanel {

	private static final long serialVersionUID = -6807658703911729105L;
	private JLabel fundAmt;
	private JLabel numConfLabel;
	
	public ConfirmationPanel(){
		
		setPreferredSize(new Dimension(1100, 150));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS) );
		setMaximumSize(new Dimension(999999, 100));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		fundAmt = new JLabel("Waiting for BTC...");
		fundAmt.setBorder(new EmptyBorder(60, 0, 0, 0));
		fundAmt.setFont(new Font("Sans Serif", Font.BOLD,35));
		fundAmt.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(fundAmt);
		
		numConfLabel = new JLabel("");
		numConfLabel.setFont(new Font("Sans Serif", Font.BOLD,27));
		numConfLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(numConfLabel);
		
	}
	
	
	public void updateConf(int conf, double amt){
		
		System.out.println(amt + " BTC transaction has "+conf + " confirmations");
		
		fundAmt.setText("Amount funded: "+amt+ " BTC");
		numConfLabel.setText(conf+ " Confirmations");
	}
	
	public void changeFuntAmt(String amt){
		
		fundAmt.setText("Amount funded: "+amt+ " BTC");
	}

}
