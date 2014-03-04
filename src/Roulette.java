import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.RawTransaction;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;

/**
 * Roulette game logic / graphics
 * @author Brian
 *
 */
public class Roulette {

	private BitcoinUtil bitcoinUtil;
	private Fund fund;
	private final int reqConfirmations = 0;
	private final Color bg = new Color(19,20,19);
	final JFrame frame = new JFrame("Bitcoin Roulette");
	final JPanel pan = new JPanel();

	public Roulette() {

		try {
			bitcoinUtil = BitcoinUtil.startBitcoin();
		} catch (MalformedURLException | FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Couldnt initialize Roulette");
		}

		fund = new Fund(bitcoinUtil);

	}

	public void startGui() {

		/* Initialize GUI */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1024, 768));
		frame.setBackground(bg);

		Container framePane = frame.getContentPane();
		framePane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		framePane.setBackground(bg);
		framePane.setLayout(new BorderLayout());

		JLabel title = new JLabel("Bitcoin Roulette");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Sans Serif", Font.BOLD, 50));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBorder(BorderFactory.createRaisedBevelBorder());
		framePane.add(title, BorderLayout.NORTH);

		framePane.add(pan, BorderLayout.CENTER);
		pan.setBackground(bg);
		pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
		pan.setBorder(new EmptyBorder(60, 10, 10, 10));

		final JButton startBtn = new JButton("Fund Account");
		startBtn.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		startBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);

		/* When fund account is clicked */
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				startBtn.setVisible(false);

				/* Step1: get new addr (in worker thread) */
				new SwingWorker<Fund, Object>() {

					@Override
					protected Fund doInBackground() throws Exception {
						return fund.getFundAddr();
					}

					/* Draw address on JPanel */
					@Override
					protected void done() {

						JTextField addrTitle = null;

						try {

							JPanel fundAddrPanel = new JPanel();
							fundAddrPanel.setLayout(new BoxLayout(fundAddrPanel, BoxLayout.PAGE_AXIS) );
							fundAddrPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
							fundAddrPanel.setMaximumSize(new Dimension(999999, 250));
							pan.add(fundAddrPanel);
							
							JLabel sendBtcTitle = new JLabel("Send BTC to this address:");
							sendBtcTitle.setFont(new Font("Sans Serif", Font.BOLD,20));
							sendBtcTitle.setHorizontalAlignment(JLabel.CENTER);
							sendBtcTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
							fundAddrPanel.add(sendBtcTitle);
							
							addrTitle = new JTextField(get().getAddr());
							addrTitle.setEditable(false);
							addrTitle.setFont(new Font("Sans Serif", Font.BOLD,
									35));
							addrTitle.setHorizontalAlignment(JLabel.CENTER);
							addrTitle.setPreferredSize(new Dimension(50,50));
							fundAddrPanel.add(addrTitle);
							frame.pack();
							
				
							frame.pack();

							/* */
							waitForConfirmations(reqConfirmations);

						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}

				}.execute();
			}

		});

		/* Add GUI elems to frame */
		pan.add(startBtn);
		frame.pack();
		frame.setVisible(true);

	}

	private void waitForConfirmations(final int numToWaitFor) {

		
		
		/* Initialize new thread to wait for confirmations */
		new SwingWorker<Object, Integer>() {
			
			JLabel numConfLabel;
			JButton startRoulette;

			@Override
			protected Object doInBackground() throws Exception {

				/* Waits for user to send BTC */
				Transaction betTx = bitcoinUtil.waitForTx(fund.getAddr());
				
				/* Create confirmations panel*/
				
				JPanel confPanel = new JPanel();
				confPanel.setLayout(new BoxLayout(confPanel, BoxLayout.PAGE_AXIS) );
				confPanel.setMaximumSize(new Dimension(999999, 100));
				confPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
				pan.add(confPanel);
				
				JPanel align = new JPanel();
				align.setLayout(new BoxLayout(align, BoxLayout.PAGE_AXIS));
				align.setMaximumSize(new Dimension(400, 100));
				align.setAlignmentX(Component.CENTER_ALIGNMENT);
				confPanel.add(align);
				
				JLabel fundAmt = new JLabel("Amount funded: "+betTx.amount()+ " BTC");
				fundAmt.setFont(new Font("Sans Serif", Font.BOLD,25));
				align.add(fundAmt);
				
				numConfLabel = new JLabel("0 Confirmations");
				numConfLabel.setFont(new Font("Sans Serif", Font.BOLD,20));
				align.add(numConfLabel);
				
				
				JPanel startRouletteWrapper = new JPanel();
				startRouletteWrapper.setLayout(new BoxLayout(startRouletteWrapper, BoxLayout.PAGE_AXIS));
				startRouletteWrapper.setMaximumSize(new Dimension(999999, 200));
				startRouletteWrapper.setBorder(new EmptyBorder(40, 0, 0, 0));
				startRouletteWrapper.setBackground(bg);
				
				startRoulette = new JButton();
				startRoulette.setFont(new Font("Sans Serif", Font.BOLD,30));
				startRoulette.setText("Waiting for "+reqConfirmations+ " confirmations...");
				startRoulette.setAlignmentX(Component.CENTER_ALIGNMENT);
				startRouletteWrapper.add(startRoulette);
				
				pan.add(startRouletteWrapper);
				
				frame.pack();
				
				RawTransaction betTxRaw = betTx.raw();

				int confirmations = 0;

				/* Wait for 3 confirmations */
				do {
					
					/* Wait 20 seconds */
					try {
						Thread.sleep(20 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}


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

					/* Send confirmations to EDT */
					publish(confirmations);


				} while (confirmations < numToWaitFor);
				
				
				return null;

			}

			/* When we get an update on number of confirmations */
			@Override
			protected void process(List<Integer> conf) {
				
				int max = -1;
				
				
				for(int i: conf)
					if(i > max)
						max = i;
				
					numConfLabel.setText(max + (max==1 ? " Confirmation" : " Confirmations"));

			}

			/**
			 * Funding confirmed, ready to start game
			 */
			@Override
			protected void done() {
				
				startRoulette.setText("Start Roulette");
				
				startRoulette.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						pan.setVisible(false);
						
						JPanel board = new JPanel();
						board.setBackground(bg);
						JLabel rouletteboard = new JLabel("ROULETTE BOARD");
						rouletteboard.setForeground(Color.WHITE);
						board.add(rouletteboard);
						frame.add(board);
						
						frame.pack();
						
						initBoard();
					}
				});
			}

		}.execute();

	}

	private void initBoard() {

		System.out.println("STARTING ROULETTE");
	}

	public void startGame() {

		System.out.println("Starting the Game");

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				startGui();
			}
		});

	}

}
