/**
 * 
 */
package com.dannypiper.snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

/**
 * @author Danny
 *
 */
public class main extends Canvas {

	/**
	 * @param args
	 */
	public static JFrame frame = new JFrame();
	public static int frameRate = 1000/10;
	public static state status = state.TITLESCREEN;
	public static List<Integer> segmentX = new ArrayList();
	public static List<Integer> segmentY = new ArrayList();
	public static direction facing = direction.RIGHT;
	public static int appleX;
	public static int appleY;
	public static Random rand = new Random();
	public static int score = 0;
	
	public static void initApple() {
		Double ax = (double) (Math.floorDiv(rand.nextInt(780), 20) * 20);
		Double ay = (double) (Math.floorDiv(rand.nextInt(780), 20) * 20);
		appleX = ax.intValue();
		appleY = ay.intValue();
	}
	public static void initSnakeSegments() {
		for (int i = 0; i<segmentX.size() ;i++) {
			segmentX.remove(i);
			segmentY.remove(i);
		}
		segmentX.removeAll(segmentX);
		segmentY.removeAll(segmentY);
		segmentX.add(400);
		segmentY.add(400);
		segmentX.add(420);
		segmentY.add(400);
		segmentX.add(440);
		segmentY.add(400);
		initApple();
	}
	public static void addSegment(int x, int y) {
		segmentX.remove(0);
		segmentY.remove(0);
		segmentX.add(x);
		segmentY.add(y);
	}
	public static void main(String[] args) throws InterruptedException {
		Canvas canvas = new main();
		canvas.setSize(800, 800);
		frame.setBounds(0, 0, 800, 800);
		frame.setVisible(true);
		frame.setName("Snake");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add("Center", canvas);
		canvas.createBufferStrategy(2);
		initSnakeSegments();
		//controls
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent ke) {
            	switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        if (ke.getKeyCode() == KeyEvent.VK_W) {
                            facing = direction.UP;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_S) {
                            facing = direction.DOWN;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_A) {
                            facing = direction.LEFT;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_D) {
                            facing = direction.RIGHT;
                        }
                        break;
            	}
            	return false;
            }
        });
		while(true) {
			canvas.repaint();
			//movement
			long a = System.currentTimeMillis();
			if(status == state.TITLESCREEN) {
				status = state.PLAYING;
				System.out.print("PLAYING");
				Thread.sleep(900);
			}
			if(status == state.PLAYING) {
				for (int i = 0; i<segmentX.size()-1 ; i++) {
					if(segmentX.get(i)==segmentX.get(segmentX.size() - 1)) {
						if(segmentY.get(i)==segmentY.get(segmentY.size() - 1)) {
							status = state.STOPPED;	//Died
							score = segmentX.size();
							canvas.repaint();
							Thread.sleep(500);
							initSnakeSegments();
							initApple();
						}
					}
				}
				if(segmentX.get(segmentX.size() - 1) == appleX 
						&& segmentY.get(segmentY.size() - 1) == appleY) {
					if(facing==direction.RIGHT) {
						segmentX.add(segmentX.get(segmentX.size() - 1) + 20);
						segmentY.add(segmentY.get(segmentY.size() - 1));
					}
					if(facing==direction.LEFT) {
						segmentX.add(segmentX.get(segmentX.size() - 1) - 20);
						segmentY.add(segmentY.get(segmentY.size() - 1));
					}
					if(facing==direction.UP) {
						segmentX.add(segmentX.get(segmentX.size() - 1));
						segmentY.add(segmentY.get(segmentY.size() - 1) - 20);
					}
					if(facing==direction.DOWN) {
						segmentX.add(segmentX.get(segmentX.size() - 1));
						segmentY.add(segmentY.get(segmentY.size() - 1) + 20);
					}
					System.out.println("Apple eaten!");
					initApple();
				}
				if(facing==direction.RIGHT) {
					int x = segmentX.get(segmentX.size()-1) + 20;
					int y = segmentY.get(segmentY.size()-1);
					addSegment(x, y);
				}
				if(facing==direction.LEFT) {
					int x = segmentX.get(segmentX.size()-1) - 20;
					int y = segmentY.get(segmentY.size()-1);
					addSegment(x, y);
				}
				if(facing==direction.UP) {
					int x = segmentX.get(segmentX.size()-1);
					int y = segmentY.get(segmentY.size()-1) - 20;
					addSegment(x, y);
				}
				if(facing==direction.DOWN) {
					int x = segmentX.get(segmentX.size()-1);
					int y = segmentY.get(segmentY.size()-1) + 20;
					addSegment(x, y);
				}
			}
			//wait for next frame
			while(System.currentTimeMillis() - a < frameRate) {
				
			}
		}
	}
	public void paint(Graphics g) {
		g.setColor(new Color(30,180, 30));
		g.fillRect(0, 0, 800, 800);
		if(status == state.TITLESCREEN) {
			g.setColor(new Color(60, 160, 60));
			g.fillRect(0, 0, 800, 800);
			g.setColor(new Color(30, 170, 30));
			g.fillRect(200, 200, 400, 400);
			g.setColor(Color.WHITE);
			g.drawString("INSERT TITLE TEXT HERE LOL", 400, 400);
		}else if(status == state.PLAYING) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 800);
			g.setColor(Color.RED);
			g.drawRect(appleX, appleY, 20, 20);
			for (int i = 0; i<segmentX.size(); i++) {
				g.setColor(Color.WHITE);
				g.drawRect(segmentX.get(i), segmentY.get(i), 20, 20);
				if(segmentX.get(i)>780) {
					segmentX.set(i ,0);
				}
				if(segmentX.get(i)<0) {
					segmentX.set(i, 780);
				}
				if(segmentY.get(i)>780) {
					segmentY.set(i, 0);
				}
				if(segmentY.get(i)<0) {
					segmentY.set(i, 780);
				}
				if(segmentX.get(i)==segmentX.get(segmentX.size()-1)
						&& segmentY.get(i)==segmentY.get(segmentY.size()-1)
						&& segmentX.size()-1 != i) {
					status = state.STOPPED;	//Died
					score = segmentX.size();
					initSnakeSegments();
					initApple();
				}
				int a = segmentX.size();
				if(i==a-1) {
					if(segmentX.get(a-2)==segmentX.get(i)
							&& segmentY.get(a-2)==segmentY.get(i)) {
						status = state.STOPPED;	//Died
						score = segmentX.size();
						initSnakeSegments();
						initApple();
					}
				}
			}
		} else if(status == state.STOPPED) {
			status = state.PLAYING;
			g.setColor(Color.RED);
			g.fillRect(0, 0, 800, 800);
			g.setColor(Color.WHITE);
			g.drawString(String.valueOf(score), 400, 400);
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			score = 0;
		}
	}

}
enum state {
	PLAYING,
	TITLESCREEN,
	STOPPED;
}
enum direction{
	UP,
	DOWN,
	RIGHT,
	LEFT;
}