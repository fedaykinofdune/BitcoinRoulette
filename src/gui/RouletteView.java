package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

public class RouletteView extends JPanel {

	private Wheel wheel;
	private Board board;

	private static final long serialVersionUID = 1L;

	public RouletteView() {

		setLayout(new BorderLayout());

		board = new Board();
		wheel = new Wheel();

		add(board, BorderLayout.WEST);
		add(wheel, BorderLayout.EAST);

		wheel.spin(20);

	}


	/**
	 * Paints ground with bgImg
	 */
	public void paintComponent(Graphics page) {
		super.paintComponent(page);

		page.drawImage(Main.BGIMG, 0, 0, null);
	}

}
