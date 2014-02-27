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

public class Roulette {

	private BitcoinUtil bitcoinUtil;
	private Fund fund;
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

		Container framePane = frame.getContentPane();
		framePane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		framePane.setBackground(Color.WHITE);
		framePane.setLayout(new BorderLayout());

		JLabel title = new JLabel("Bitcoin Roulette");
		title.setFont(new Font("Sans Serif", Font.BOLD, 50));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBorder(BorderFactory.createRaisedBevelBorder());
		framePane.add(title, BorderLayout.PAGE_START);

		pan.setBackground(Color.WHITE);
		framePane.add(pan, BorderLayout.CENTER);
		pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
		pan.setBorder(new EmptyBorder(100, 10, 10, 10));

		JButton startBtn = new JButton("Fund Account");
		startBtn.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

		/* When fund account is clicked */
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

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

							addrTitle = new JTextField(get().getAddr());
							addrTitle.setEditable(false);
							addrTitle.setFont(new Font("Sans Serif", Font.BOLD,
									30));
							addrTitle.setHorizontalAlignment(JLabel.CENTER);
							addrTitle.setBorder(BorderFactory
									.createRaisedBevelBorder());
							pan.add(addrTitle, BorderLayout.CENTER);
							frame.pack();

							/* */
							waitForConfirmations(3);

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

			@Override
			protected Object doInBackground() throws Exception {

				Transaction betTx = bitcoinUtil.waitForTx(fund.getAddr());
				
				numConfLabel = new JLabel("0 Confirmations");
				pan.add(numConfLabel,BorderLayout.CENTER);
				frame.pack();
				
				System.out.println("User sent a transaction worth "+betTx.amount());
				
				pan.add(new JLabel("AMT: "+betTx.amount()));
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
				
				System.out.println("Proccess called");
				for(int i: conf)
				{
					if(i > max)
						max = i;
					System.out.print(i+" ");
				}
				System.out.println("");
				
				numConfLabel.setText(max+" Confirmations");

			}

			@Override
			protected void done() {
				
				initBoard();
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
