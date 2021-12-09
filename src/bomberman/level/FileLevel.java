package bomberman.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;



import bomberman.Board;
import bomberman.Game;
import bomberman.entities.LayeredEntity;
import bomberman.entities.mob.Player;
import bomberman.entities.mob.enemy.Balloom;
import bomberman.entities.mob.enemy.Doll;
import bomberman.entities.mob.enemy.Kondoria;
import bomberman.entities.mob.enemy.Minvo;
import bomberman.entities.mob.enemy.Oneal;
import bomberman.entities.tile.GrassTile;
import bomberman.entities.tile.PortalTile;
import bomberman.entities.tile.WallTile;
import bomberman.entities.tile.destroyable.BrickTile;
import bomberman.entities.tile.powerup.PowerupBombs;
import bomberman.entities.tile.powerup.PowerupFlames;
import bomberman.entities.tile.powerup.PowerupSpeed;
import bomberman.exceptions.LoadLevelException;
import bomberman.graphics.Screen;
import bomberman.graphics.Sprite;
import bomberman.sounds.Sound;
import javax.sound.sampled.Clip;

public class FileLevel extends Level {
	
	public FileLevel(String path, Board board) throws LoadLevelException {
		super(path, board);
	}
	
	@Override
	public void loadLevel(String path) throws LoadLevelException {
		try {
			URL absPath = FileLevel.class.getResource("/" + path);

			if (absPath == null) {
				Clip clip=Sound.getSound(getClass().getResource("/bomberman/sounds/win.wav"));
				clip.start();
				absPath = new URL("");
			}

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(absPath.openStream()));

			String data = in.readLine();
			StringTokenizer tokens = new StringTokenizer(data);
			
			_level = Integer.parseInt(tokens.nextToken());
			_height = Integer.parseInt(tokens.nextToken());
			_width = Integer.parseInt(tokens.nextToken());

			_lineTiles = new String[_height];
			
			for(int i = 0; i < _height; ++i) {
				_lineTiles[i] = in.readLine().substring(0, _width);
 			}
			
			in.close();
		} catch (IOException e) {
			throw new LoadLevelException("Error loading level " + path, e);

		}
	}
	
	@Override
	public void createEntities() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				addLevelEntity( _lineTiles[y].charAt(x), x, y );
			}
		}
	}
	
	public void addLevelEntity(char c, int x, int y) {
		int pos = x + y * getWidth();
		
		switch(c) { // TODO: minimize this method
			case '#': 
				_board.addEntitie(pos, new WallTile(x, y, Sprite.wall));  
				break;
			case 'b': 
				LayeredEntity layer = new LayeredEntity(x, y, 
						new GrassTile(x ,y, Sprite.grass), 
						new BrickTile(x ,y, Sprite.brick));
				
				if(_board.isPowerupUsed(x, y, _level) == false) {
					layer.addBeforeTop(new PowerupBombs(x, y, _level, Sprite.powerup_bombs));
				}
				
				_board.addEntitie(pos, layer);
				break;
			case 's':
				layer = new LayeredEntity(x, y, 
						new GrassTile(x ,y, Sprite.grass), 
						new BrickTile(x ,y, Sprite.brick));
				
				if(_board.isPowerupUsed(x, y, _level) == false) {
					layer.addBeforeTop(new PowerupSpeed(x, y, _level, Sprite.powerup_speed));
				}
				
				_board.addEntitie(pos, layer);
				break;
			case 'f': 
				layer = new LayeredEntity(x, y, 
						new GrassTile(x ,y, Sprite.grass), 
						new BrickTile(x ,y, Sprite.brick));
				
				if(_board.isPowerupUsed(x, y, _level) == false) {
					layer.addBeforeTop(new PowerupFlames(x, y, _level, Sprite.powerup_flames));
				}
				
				_board.addEntitie(pos, layer);
				break;
			case '*': 
				_board.addEntitie(pos, new LayeredEntity(x, y, 
						new GrassTile(x ,y, Sprite.grass), 
						new BrickTile(x ,y, Sprite.brick)) );
				break;
			case 'x': 
				_board.addEntitie(pos, new LayeredEntity(x, y, 
						new GrassTile(x ,y, Sprite.grass), 
						new PortalTile(x ,y, _board, Sprite.portal), 
						new BrickTile(x ,y, Sprite.brick)) );
				break;
			case ' ': 
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass) );
				break;
			case 'p': 
				_board.addMob( new Player(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board) );
				Screen.setOffset(0, 0);
				
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass) );
				break;
			//Enemies
			case '1':
				_board.addMob( new Balloom(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass) );
				break;
			case '2':
				_board.addMob( new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass) );
				break;
			case '3':
				_board.addMob( new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass) );
				break;
			case '4':
				_board.addMob( new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass) );
				break;
			case '5':
				_board.addMob( new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass) );
				break;
			default: 
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass) );
				break;
			}
	}
	
}
