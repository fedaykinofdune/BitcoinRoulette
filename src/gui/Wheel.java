package gui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Wheel extends JPanel {

	private static final long serialVersionUID = -8286973648077470628L;
	private BufferedImage wheelImg;
	private Timer timer;
	private double locationX;
	private double locationY;
	private AffineTransform tx;
	private HashMap<Integer, Integer> degToValue;

	public Wheel() {
		

		try {
			wheelImg = ImageIO.read(new File("assets/wheel.png"));

		} catch (IOException e) {

			System.err.println(e);
			return;
		}
		
		locationX = wheelImg.getWidth() / 2;
		locationY = wheelImg.getHeight() / 2;
		
		degToValue = new HashMap<Integer,Integer>();
		
		int[] wheelValues = new int[]{32,0,26,3,35,12,28,7,29,18,22,9,31,14,
										20,1,33,16,24,5,10,23,8,30,11,36,13,
										27,6,34,17,25,2,21,4,19,15};
		
		for(int i=0; i < 360; i++){
			
			double singleSliceDegree = 360.0/37.0;
			
			int val = (int)((double)i/singleSliceDegree);
			
			degToValue.put(i,wheelValues[val]);
			
		}
		
		setPreferredSize(new Dimension(550, 800));
		setOpaque(false);

	}
	
	
	public int spin(final int totalDeg){
		
		final Robot r;
		try {
			 r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			return -1;
		}

		int ret = -77;
		//////////////
		
		
		/* Timer */
		timer = new Timer(30, new ActionListener() {

			
			int deg = 1;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				r.keyPress(62);
				
				if(deg >= totalDeg)
					timer.stop();

				
				/* Inertial speed */
				int incSpeed = 10-((int)((deg/((double)totalDeg))*10));
				deg = (deg + incSpeed);
				
				deg = deg+1;

				double rotationRequired = Math.toRadians(deg);
				
				tx = AffineTransform.getRotateInstance(
						rotationRequired, locationX, locationY);
				
				repaint();
				
			}
		});
		
		timer.start();
		
		return ret;

	}
	
	

	public void paintComponent(Graphics page) {
		
		super.paintComponent(page);

		((Graphics2D)page).drawImage(wheelImg, tx, null);

	}

}
