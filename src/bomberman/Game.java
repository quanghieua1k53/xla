package bomberman;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


import bomberman.exceptions.BombermanException;
import bomberman.graphics.Screen;
import bomberman.gui.Frame;
import bomberman.input.Keyboard;

public class Game extends Canvas {
	
	/*
	|--------------------------------------------------------------------------
	| Options & Configs
	|--------------------------------------------------------------------------
	 */
	
	public static final int TILES_SIZE = 16,
							WIDTH = TILES_SIZE * (31/2),
							HEIGHT = 13 * TILES_SIZE;

	public static int SCALE = 3;
	
	//initial configs
	private static final int BOMBRATE = 1;
	private static final int BOMBRADIUS = 1;
	private static final double PLAYERSPEED = 1.0;

	public static final int LIVES = 1;
	
	protected static int SCREENDELAY = 3;
	
	
	//can be modified with bonus
	protected static int bombRate = BOMBRATE;
	protected static int bombRadius = BOMBRADIUS;
	protected static double playerSpeed = PLAYERSPEED;
	
	
	//Time in the level screen in seconds
	protected int _screenDelay = SCREENDELAY;
	
	private final Keyboard _input;
	private boolean _running = false;
	private boolean _paused = true;
	
	private final Board _board;
	private final Screen screen;
	private final Frame _frame;
	
	//this will be used to render the game, each render is a calculated image saved here
	private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Game(Frame frame) throws BombermanException {
		_frame = frame;
		
		screen = new Screen(WIDTH, HEIGHT);
		_input = new Keyboard();
		
		_board = new Board(this, _input, screen);
		addKeyListener(_input);
	}
	
	
	private void renderGame() { //render will run the maximum times it can per second
		BufferStrategy bs = getBufferStrategy(); //create a buffer to store images using canvas
		if(bs == null) { //if canvas dont have a bufferstrategy, create it
			createBufferStrategy(3); //triple buffer
			return;
		}
		
		screen.clear();
		
		_board.render(screen);
		
		for (int i = 0; i < pixels.length; i++) { //create the image to be rendered
			pixels[i] = screen._pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose(); //release resources
		bs.show(); //make next buffer visible
	}
	
	private void renderScreen() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		
		Graphics g = bs.getDrawGraphics();
		
		_board.drawScreen(g);

		g.dispose();
		bs.show();
	}

	private void update() {
		_input.update();
		_board.update();
	}
	
	public void start() {
		_running = true;
		
		long  lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0; //nanosecond, 60 frames per second
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while(_running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}

			if(_paused) {
				if(_screenDelay <= 0) { //time passed? lets reset status to show the game
					_board.setShow(-1);
					_paused = false;
				}

				renderScreen();
			} else {
				renderGame();
			}
				
			
			frames++;
			if(System.currentTimeMillis() - timer > 1000) { //once per second
				timer += 1000;
				updates = 0;
				frames = 0;
				
				if(_board.getShow() == 2)
					--_screenDelay;
			}
		}
	}
	
	/*
	|--------------------------------------------------------------------------
	| Getters & Setters
	|--------------------------------------------------------------------------
	 */
	public static double getPlayerSpeed() {
		return playerSpeed;
	}
	
	public static int getBombRate() {
		return bombRate;
	}
	
	public static int getBombRadius() {
		return bombRadius;
	}
	
	public static void addPlayerSpeed(double i) {
		playerSpeed += i;
	}
	
	public static void addBombRadius(int i) {
		bombRadius += i;
	}
	
	public static void addBombRate(int i) {
		bombRate += i;
	}
	
	public void resetScreenDelay() {
		_screenDelay = SCREENDELAY;
	}
	
	public Board getBoard() {
		return _board;
	}
	
	public void run() {
		_running = true;
		_paused = false;
	}
	
	public boolean isPaused() {
		return _paused;
	}
	
	public void pause() {
		_paused = true;
	}
	
}
