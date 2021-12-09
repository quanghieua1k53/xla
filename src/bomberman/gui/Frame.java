package bomberman.gui;

import java.awt.BorderLayout;

import bomberman.sounds.Sound;

import javax.sound.sampled.Clip;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bomberman.Game;

public class Frame extends JFrame {
	
	public GamePanel _gamepane;
	private JPanel _containerpane;
	private Clip clip;
	
	private Game _game;

	public Frame() {

		//clip= Sound.getSound(getClass().getResource("/bomberman/sounds/background.wav"));
		//clip.start();
		//clip.loop(100);

		_containerpane = new JPanel(new BorderLayout());
		_gamepane = new GamePanel(this);

		_containerpane.add(_gamepane, BorderLayout.PAGE_END);
		
		_game = _gamepane.getGame();
		
		add(_containerpane);
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		_game.start();
	}
	
}
