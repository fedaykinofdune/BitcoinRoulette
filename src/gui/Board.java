package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Board extends JPanel{
	
	private BufferedImage boardImg;

	private static final long serialVersionUID = 3032926387546045087L;
	
	public Board(){
		
		try {
			boardImg = ImageIO.read(new File("assets/board.png"));
			
		} catch (IOException e) {
			
			System.err.println(e);
			return;
		}
		
		setPreferredSize(new Dimension(550, 800));
		setOpaque(false);
	}
	
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);

		page.drawImage(boardImg, 0, 0, null);
	}

}
