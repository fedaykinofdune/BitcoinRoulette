package gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.RouletteCore;

public class WelcomeScreen extends JPanel {

	/* Serializable */
	private static final long serialVersionUID = -8824293977897628397L;
	private RouletteView rouletteView;

	public WelcomeScreen(final RouletteCore core) {

		setLayout(new GridBagLayout());
		rouletteView = new RouletteView(core);

		JButton title = new JButton() {

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

		add(title, constraints);

		constraints.gridy = 1;
		constraints.ipadx = 50;
		constraints.ipady = 30;

		JButton playBtn = new JButton("Play");
		playBtn.setFont(new Font("Sans Serif", Font.BOLD,30));

		playBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JTabbedPane tabPane = new JTabbedPane();

				final FundPanel fundPanel = new FundPanel(core, rouletteView);

				tabPane.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent changeEvent) {
						JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent
								.getSource();
						int index = sourceTabbedPane.getSelectedIndex();
						if (index == 1)
							rouletteView.updateBalance();
						else if (index == 0)
							fundPanel.updateBalance();
					}
				});

				tabPane.addTab("Dealer", fundPanel);
				tabPane.addTab("Roulette", rouletteView);
				tabPane.setSelectedIndex(1);

				removeAll();
				setVisible(false);
				Main.frame.add(tabPane);
				Main.frame.pack();
			}
		});

		add(playBtn, constraints);
	}

	public RouletteView getRoueltteView() {

		return rouletteView;
	}

	/**
	 * Paints ground with bgImg
	 */
	public void paintComponent(Graphics page) {
		super.paintComponent(page);

		page.drawImage(Main.BGIMG, 0, 0, null);
	}

}
