package gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import core.Roulette;

public class WelcomeScreen extends JPanel {

	/* Serializable */
	private static final long serialVersionUID = -8824293977897628397L;

	public WelcomeScreen() {
		
		setLayout(new GridBagLayout());
		
		
		JButton title = new JButton(){
			
			/* Serializable */
			private static final long serialVersionUID = 1797733766172367480L;

			public void paintComponent(Graphics page) {
				super.paintComponent(page);
				page.drawImage(Main.TITLEIMG, 0, 0, null);
			}
		};
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridy = 0;
		constraints.ipadx = 670;
		constraints.ipady = 344;
		constraints.insets = new Insets(50, 0, 50, 0);
		
		add(title,constraints);
		
		
		constraints.gridy = 1;
		constraints.ipadx = 50;
		constraints.ipady = 30;
		
		JButton playBtn = new JButton("Play");
		
		playBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

					new Roulette().startGame();
			}
		});
		
		
		add(playBtn, constraints);
	}

	/**
	 * Paints ground with bgImg
	 */
	public void paintComponent(Graphics page) {
		super.paintComponent(page);

		page.drawImage(Main.BGIMG, 0, 0, null);
	}

}
