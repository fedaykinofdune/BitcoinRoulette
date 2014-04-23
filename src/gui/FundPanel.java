package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;

import core.Account;
import core.Dealer;
import core.RouletteCore;


/**
 * Roulette game logic / graphics
 * @author Brian
 *
 */
public class FundPanel extends JPanel{

	private static final long serialVersionUID = -7576299607980296915L;
	private RouletteCore core;
	private Image casinoChips;
	private ConfirmationPanel confirmationPanel;
	private RouletteView rouletteView;
	private JLabel balance;

	
	public FundPanel(RouletteCore core, RouletteView rouletteView) {
		
		this.rouletteView = rouletteView;
		this.core = core;
		
		try {
			casinoChips = ImageIO.read(new File("assets/chips.png"));
		} catch (IOException e) {
			
			System.err.println(e);
			return;
		}
		
		init();

	}
	
	public void init(){
		
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		/* Casino chips */
		JLabel chipsImgLabel = new JLabel(new ImageIcon(casinoChips));
		chipsImgLabel.setMaximumSize(new Dimension(1100, 300));
		
		chipsImgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(chipsImgLabel);
		
		JLabel title = new JLabel("Fund account with BTC");
		title.setBorder(new EmptyBorder(20, 0, 0, 0));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setForeground(Color.BLACK);
		title.setFont(new Font("Sans Serif", Font.BOLD, 50));
		add(title);
		
		balance = new JLabel("Balance: "+core.getAccount().prettyPrintBalance()+" BTC");
		balance.setAlignmentX(Component.CENTER_ALIGNMENT);
		balance.setForeground(Color.WHITE);
		balance.setFont(new Font("Sans Serif", Font.BOLD, 50));
		add(balance);
		
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setMaximumSize(new Dimension(1100, 100));
		buttonsPanel.setOpaque(false);
		add(buttonsPanel);
		
		/* Fund acct button */
		final JButton fundBtn = new JButton("Fund Account");
		fundBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		fundBtn.setFont(new Font("Sans Serif", Font.PLAIN, 30));
		buttonsPanel.add(fundBtn);
		
		
		
		final JButton cashOutBtn = new JButton("Cash out");
		cashOutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		cashOutBtn.setFont(new Font("Sans Serif", Font.PLAIN, 30));
		buttonsPanel.add(cashOutBtn);
		
		/* Fund acct listener */
		fundBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				fundBtn.setVisible(false);
				cashOutBtn.setVisible(false);

				/* Get new addr (in worker thread) */
				new SwingWorker<Object, Object>() {

					@Override
					protected String doInBackground() throws Exception {
						core.setPlayerNewFundAddr();
						return "NOT USED";
					}

					/* Draw address on JPanel */
					@Override
					protected void done() {


						JPanel fundAddrPanel = new JPanel();
						fundAddrPanel.setLayout(new BorderLayout());
						fundAddrPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
						add(fundAddrPanel);
						
						
						JPanel top = new JPanel();
						top.setMaximumSize(new Dimension(1100, 70));
						top.setLayout(new BorderLayout());
						JLabel sendBtcTitle = new JLabel("Send BTC to this address:");
						sendBtcTitle.setFont(new Font("Sans Serif", Font.BOLD,20));
						top.add(sendBtcTitle,BorderLayout.WEST);
						
						JButton close = new JButton("Cancel");
						close.setFont(new Font("Sans Serif", Font.BOLD,20));
						close.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								removeAll();
								updateUI();
								init();
								Main.frame.pack();
								core.getBitcoinUtil().cancelWaitTx();
								return;
								
							}
						});
						top.add(close,BorderLayout.EAST);
						
						fundAddrPanel.add(top, BorderLayout.NORTH);
						
						JTextField addrTitle = new JTextField(core.getAccount().getAddr());
						addrTitle.setHorizontalAlignment(JTextField.CENTER);
						addrTitle.setEditable(false);
						addrTitle.setMaximumSize(new Dimension(900, 200));
						addrTitle.setAlignmentX(SwingConstants.CENTER);
						addrTitle.setFont(new Font("Sans Serif", Font.BOLD,35));
						addrTitle.setPreferredSize(new Dimension(50,50));
						fundAddrPanel.add(addrTitle, BorderLayout.CENTER);
						
						confirmationPanel = new ConfirmationPanel();
						add(confirmationPanel);
						
						Main.frame.pack();
						
						/* */
						core.getDealer().waitForConfirmations(FundPanel.this);
					}

				}.execute();
			}

		});
		
		/*  Cash out listener */
		cashOutBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				cashOutBtn.setVisible(false);
				fundBtn.setVisible(false);
				
				JPanel cashOutPanel = new JPanel();
				cashOutPanel.setOpaque(false);
				cashOutPanel.setLayout(new BorderLayout());
				cashOutPanel.setBorder(new EmptyBorder(20, 20, 95, 20));
				add(cashOutPanel);
				
				
				JPanel top = new JPanel();
				top.setOpaque(false);
				top.setMaximumSize(new Dimension(1100, 70));
				top.setLayout(new BorderLayout());
				
				JButton close = new JButton("Cancel");
				close.setFont(new Font("Sans Serif", Font.BOLD,20));
				close.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						removeAll();
						updateUI();
						init();
						Main.frame.pack();
						
						return;
						
					}
				});
				
				top.add(close,BorderLayout.EAST);
				
				cashOutPanel.add(top, BorderLayout.NORTH);
				
				JPanel cashOutOptPanel = new JPanel();
				cashOutOptPanel.setOpaque(false);
				cashOutOptPanel.setLayout(new BoxLayout(cashOutOptPanel, BoxLayout.PAGE_AXIS) );
				
				/* Enter amount */
				final JTextField enterAmt = new JTextField("Enter amount in BTC");
				enterAmt.setFont(new Font("Sans Serif", Font.BOLD,25));
				
				enterAmt.addFocusListener(new FocusListener() {
					
					@Override
					public void focusLost(FocusEvent arg0) {
					}
					
					@Override
					public void focusGained(FocusEvent arg0) {
						enterAmt.setText("");
					}
				});
				
				cashOutOptPanel.add(enterAmt);
				
				JPanel space = new JPanel();
				space.setOpaque(false);
				space.setPreferredSize(new Dimension(30, 30));
				cashOutOptPanel.add(space);
				
				
				/* Enter Address */
				final JTextField enterAddr = new JTextField("Enter address");
				enterAddr.setFont(new Font("Sans Serif", Font.BOLD,25));

				enterAddr.addFocusListener(new FocusListener() {
					
					@Override
					public void focusLost(FocusEvent arg0) {
					}
					
					@Override
					public void focusGained(FocusEvent arg0) {
						enterAddr.setText("");
					}
				});
				
				cashOutOptPanel.add(enterAddr);
				cashOutOptPanel.add(space);
				
				JButton cashOutBtn = new JButton("Cash Out");
				cashOutBtn.setFont(new Font("Sans Serif", Font.BOLD,25));
				
				cashOutOptPanel.add(cashOutBtn);
				
				cashOutBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						new SwingWorker<Boolean, Object>() {

							@Override
							protected Boolean doInBackground() throws Exception {

								try {
									double amt = Double.parseDouble(enterAmt.getText());
									core.getDealer().cashOut(amt, enterAddr.getText());
								} catch (Exception e) {
									
									JOptionPane.showMessageDialog(Main.frame, "Enter valid address and amount");
									e.printStackTrace();
									return false;
								}
								
								return true;
							}
							
							@Override
							protected void done() {
								
								boolean succ;
								
								try {
									succ = get();
								} catch (InterruptedException
										| ExecutionException e) {
									succ = false;
								}
								
								if(succ){

									JOptionPane.showMessageDialog(Main.frame, "Successfully cashed out "
															+enterAmt.getText() + " to "+enterAddr.getText());
									
									removeAll();
									updateUI();
									init();
									Main.frame.pack();
								}
							}
							
						}.execute();
					}
				});
				
				cashOutPanel.add(cashOutOptPanel,BorderLayout.CENTER);
				
				Main.frame.pack();
				
			}
		});
		

		
		
	}
	

	public void updateConfirmationPanel(int numConf) {
		
		/* Done confirming.  Update Account */
		if(numConf >= core.getDealer().getReqConf()){
			
			Account acct = core.getAccount();
			acct.setBalance(acct.getBalance() + core.getDealer().getBetTx().amount());
			rouletteView.updateBalance();
			
			removeAll();
			updateUI();
			init();
			Main.frame.pack();
		}
		else{
			confirmationPanel.updateConf(numConf,core.getDealer().getBetTx().amount());
		}

	}
	
	
	public void updateBalance(){
		balance.setText("Balance: "+core.getAccount().prettyPrintBalance()+" BTC");
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		g.drawImage(Main.BGIMG, 0, 0, null);
			
	}




}
