package com.roboticseattle.spar.body;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.roboticseattle.common.FaceExpression;
import com.roboticseattle.spar.mind.FaceNerve;
import com.roboticseattle.spar.mind.Nerve;

public class Face extends Part {
	
	private static int FRAME_WIDTH = 800;
	private static int FRAME_HEIGHT = 480;
	
	private JFrame frame;
    private JLabel label;
    private JPanel leftMargin;
    
    private ClassLoader loader;
    private FaceExpression newState  = FaceExpression.THINKING;
    private FaceExpression nextState  = null;
    private boolean isFastForward = false;
	
	public Face(int aSleepBetween) {
		super(aSleepBetween);
	}

	public Face() {
		super();
	}

	public synchronized void setNewState(FaceExpression aNewState) {
	    if(aNewState != null && newState != aNewState && aNewState != nextState) {
          isFastForward = true;
          nextState = aNewState;
	    }    
	}
	
	public static void main(String[] args) throws Exception {
		Face face = new Face();
		face.start(null);
		
		Thread.sleep(15000);
		face.setNewState(FaceExpression.LOOKING_RIGHT);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.HIGH_FIVE);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.LOOKING_LEFT);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.BORED);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.SPEAKING);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.PLAYING_MUSIC);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.THINKING);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.EXCITED);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.SAD);
		Thread.sleep(15000);
		face.setNewState(FaceExpression.IDLE);
		Thread.sleep(15000);
		face.shutdown();
		
	}

	@Override
	public void beforeLoop() {
		loader = Thread.currentThread().getContextClassLoader();
		frame = new JFrame();
	    label = new JLabel();
	    leftMargin = new JPanel();
        frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setUndecorated(true);
        frame.setLayout(new BorderLayout());
		frame.getContentPane().setBackground(Color.BLACK);
		leftMargin.setPreferredSize(new Dimension(60, FRAME_HEIGHT));
		leftMargin.setOpaque(false);
		frame.add(leftMargin, BorderLayout.WEST);
        frame.add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void insideLoop(int counter) throws Exception {
	    for(int r=0; r < newState.getRanges().length; r++) {
			int[] range = newState.getRanges()[r]; int from=range[0]; int to=range[1];
			for(int i=from; i <= to; i++) {
				String fileName = newState.getPrefix()+"."+String.format("%04d", i)+".png";
				BufferedImage img = ImageIO.read(loader.getResourceAsStream(fileName));
				label.setIcon(new ImageIcon(img));
				frame.getContentPane().repaint();
			    if(!isFastForward) Thread.sleep(newState.getFrameDelay());
			    else Thread.sleep(10);
			}
    	    if(!isFastForward) Thread.sleep(newState.getRangeDelay());
		}
		if(nerve != null && nerve instanceof FaceNerve) {
			((FaceNerve)nerve).onEpressionComplete(newState);
		}
		
		synchronized(this) {
     		if(nextState != null) newState = nextState;
	    	nextState = null;
	    	isFastForward = false;
	   }	
	}
	
	@Override
	public void afterLoop() {
		frame.dispose();
	}

	@Override
	public String painfulStuff() {
		return "";
	}

	@Override
	public void insideInterrupted() {
		
	}

}
