package Utils;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import play.World3D;

public class KeyHandling implements KeyListener, MouseListener {
		
	private int forward=KeyEvent.VK_W;
	private int backward=KeyEvent.VK_S;
	private int strafel=KeyEvent.VK_A;
	private int strafer=KeyEvent.VK_D;
	private int sprint=KeyEvent.VK_SHIFT;
	private int use=InputEvent.BUTTON1_MASK;
	private World3D shooter;
	
	public KeyHandling(World3D shooter){
		this.shooter = shooter;
	}
	
	@Override			
	public void keyPressed(KeyEvent e){			
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE){			
			new Thread(new Runnable(){			
				public void run(){			
					System.exit(0);			
				}
	
			}).start();
	
		}			
		if(e.getKeyCode()==forward)shooter.game.setFord(true);			
		if(e.getKeyCode()==backward)shooter.game.setback(true);			
		if(e.getKeyCode()==strafel)shooter.game.setstrafel(true);			
		if(e.getKeyCode()==strafer)shooter.game.setstrafer(true);
		if(e.getKeyCode()==sprint)shooter.game.isSprint(true);
	}
	
	@Override			
	public void keyReleased(KeyEvent e){			
		if(e.getKeyCode()==forward)shooter.game.setFord(false);			
		if(e.getKeyCode()==backward)shooter.game.setback(false);			
		if(e.getKeyCode()==strafel)shooter.game.setstrafel(false);			
		if(e.getKeyCode()==strafer)shooter.game.setstrafer(false);	
		if(e.getKeyCode()==sprint)shooter.game.isSprint(false);
	}			
	
	@Override			
	public void mouseClicked(MouseEvent e) {					
		if((e.getModifiers() & use)!=0)			
			shooter.game.setUse();				
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}			
}
