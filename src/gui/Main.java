package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


public class Main {
	
	public static JFrame frame;
	public static Image BGIMG;
	public static Image TITLEIMG;
	
	private void init(){
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1100,800));
		frame.setResizable(false);

		try {
		BGIMG = ImageIO.read(new File("assets/Felt.jpg"));
		TITLEIMG = ImageIO.read(new File("assets/title.png")); 
		} catch (IOException e) {
			
			System.err.println(e);
			return;
		}

	}
	

	public static void main(String[]args) {
		
		 new Main().init();
		 frame.add(new WelcomeScreen());
		 frame.pack();
	}
	
}
