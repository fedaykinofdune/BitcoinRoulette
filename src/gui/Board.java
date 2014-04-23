package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Board extends JPanel{
	
	private BufferedImage boardImg;
	private RouletteView rouletteView;
	private boolean bettingBlack;

	private static final long serialVersionUID = 3032926387546045087L;
	
	public Board(final RouletteView v){
		
		
		try {
			boardImg = ImageIO.read(new File("assets/board.png"));
			
		} catch (IOException e) {
			
			System.err.println(e);
			return;
		}
		
		bettingBlack = true;
		rouletteView = v;
		
		setLayout(null);
		JButton red = new JButton();
		
		red.setLayout(null);
		red.setBounds(new Rectangle(100,284,68,120));
		red.setOpaque(false);
		red.setContentAreaFilled(false);
		red.setBorderPainted(false);
		
		red.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				bettingBlack = false;
				v.getBetPanel().setBettingOn("Betting on: Red");
			}
		});
		
		JButton black = new JButton();
		
		black.setLayout(null);
		black.setBounds(new Rectangle(100,400,68,115));
		black.setOpaque(false);
		black.setContentAreaFilled(false);
		black.setBorderPainted(false);
		
		black.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				bettingBlack = true;
				v.getBetPanel().setBettingOn("Betting on: Black");
			}
		});
		
		add(red);
		add(black);
		
		setPreferredSize(new Dimension(550, 800));
		setOpaque(false);
	}
	
	
	public boolean getBettingBlack(){
		
		return bettingBlack;
	}
	
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);

		page.drawImage(boardImg, 0, 0, null);
	}

}
