package play;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import com.jogamp.opengl.GL2;

import Entities.Player;

public class Game {

	private Player player;//we add a player to the game	
	private DrawWorld world;
	
	private GL2 gl;
	
	private final float headSens=0.02f;	
	private final float pitchSens=0.01f;	
	private Point mouseCenter;	
	private Robot robot;	
	private float dx, dy;

	private long lastTime;	
	private boolean ford=false;	
	private boolean back=false;	
	private boolean strafel=false;	
	private boolean strafer=false;	
	private boolean sprint = false;
	private int use=0;
	
	private int[][] mapGroundFloor = {{-1, -1, -1, -1, -1},
							{-1, 0, 0, 0, 0, 0, -1},
							{-1, 0, 0, 0, 0, 0, -1},
							{-1, 0, 0, 1, 0, 0, -1},
							{-1, 0, 0, 1, 0, 0, -1},
							{-1, 0, 0, 1, 0, 0, -1},
							{-1, -1, -1, -1, -1}};
	
	private int[][] mapUpperLanding = {{-1, -1, -1, -1, -1, -1, -1},
										{-1, 0, 0, 0, 0, 0, -1},
										{-1, 0, 0, 0, 0, 0, -1},
										{-1, 0, 0, 1, 0, 0, -1},
										{-1, 0, 0, 0, 0, 0, -1},
										{-1, 0, 0, 0, 0, 0, -1},
										{-1, -1, -1, -1, -1, -1, -1}};
	
	float minX = Float.POSITIVE_INFINITY, maxX  = Float.NEGATIVE_INFINITY, 
    		minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, 
    		minZ = Float.POSITIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
	
	public void setBounds(float minX, float maxX, float minY, float maxY, float minZ, float maxZ){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}
	
	public Game(GL2 gl){	
		this.gl=gl;	
		player=new Player();
		world = new DrawWorld(gl);
	
		Robot r=null;	
		try{	
			r=new Robot();		
		}catch(final AWTException e){System.out.println("Trouble starting Robot");}		
		this.robot=r;		
		if(robot==null)System.out.println("Error setting up robot");		
	}

	private void pollEvents(){
		long now=System.nanoTime();		
		float period=(float)((now-lastTime)*0.000005);		
		lastTime=now;
	
		dx=MouseInfo.getPointerInfo().getLocation().x;	
		dy=MouseInfo.getPointerInfo().getLocation().y;	
		float head=mouseCenter.x-dx;		
		float pit=mouseCenter.y-dy;

		if(head!=0) player.setHeading(head*headSens);		
		if(pit!=0) player.setPitch(pit*pitchSens);		
		if(ford) player.setFord((float)period);		
		if(back) player.setBack((float)period);		
		if(strafel) player.setStrafel((float)period);		
		if(strafer) player.setStrafer((float)period);	
		if(sprint)player.setSprint(1.5f);
		else player.setSprint(1f);
		
		collisions(period, world.getRoom());
		
		player.set();	
	}
		
	public void tick(){		
		pollEvents();		
		if(robot!=null)		
			robot.mouseMove(mouseCenter.x, mouseCenter.y);		
		
		use=0;
		
		player.draw(gl);	
		world.draw(gl);
	}
	
	public void setMouseCenter(Point center){	
		this.mouseCenter=center;	
	}
	
	public void setFord(boolean flag){	
		ford=flag;
	}
	
	public void setback(boolean flag){
	
		back=flag;
	
	}
	
	public void setstrafel(boolean flag){	
		strafel=flag;	
	}
	
	public void setstrafer(boolean flag){	
		strafer=flag;	
	}
	
	public void isSprint(boolean flag) {
		sprint=flag;
	}
	
	public void setUse(){	
		use++;	
	}
	
	public void collisions(float period, String room){
		if(room.equals("mainCorridor")){
			//Wall collisions
			if(-122 > player.getX()){
				if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
			}
			else if(100 < player.getX()){
				if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);
			}
			else if(-100 > player.getZ()){
	        	if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
	        }
			else if(255 < player.getZ() && 260 > player.getZ()){
	        	if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
	        }
			else if(470 < player.getZ()){
	        	if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
	        }
			if(player.getZ() > 43.88 && player.getZ() < 102.55){
				if(player.getX() > 35){
					if(ford) player.setFord(-(float)period);		
					if(back) player.setBack(-(float)period);		
					if(strafel) player.setStrafel(-(float)period);		
					if(strafer) player.setStrafer(-(float)period);
				}
				else if(player.getX() < -45){
					if(ford) player.setFord(-(float)period);		
					if(back) player.setBack(-(float)period);		
					if(strafel) player.setStrafel(-(float)period);		
					if(strafer) player.setStrafer(-(float)period);
				}
			}
			if(player.getY() < -2 && player.getZ() < 314){
				if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);
			}
			if(player.getZ() > 250 && player.getZ() < 305){
				if(player.getX() > 35){
					if(ford) player.setFord(-(float)period);		
					if(back) player.setBack(-(float)period);		
					if(strafel) player.setStrafel(-(float)period);		
					if(strafer) player.setStrafer(-(float)period);
				}
				else if(player.getX() < -45){
					if(ford) player.setFord(-(float)period);		
					if(back) player.setBack(-(float)period);		
					if(strafel) player.setStrafel(-(float)period);		
					if(strafer) player.setStrafer(-(float)period);
				}
				if(use > 0){			
					if (player.getY() >= -2){
						player.setZ(423);
						player.setY(player.getY() - 2.5f);
					}
				}
			}
			if(player.getX() < 24.5 && player.getX() > -33.5 && player.getY() < -2 && player.getZ() < 423 && player.getZ() > 371.3){
				if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);
				if(use > 0 && player.getZ() < 450 && player.getX() < 24.5 && player.getX() > -33.5){			
						player.setZ(250);
						player.setY(player.getY() + 2.5f);
				}
			}
			if(player.getX() > 88 & use > 0){
				if(player.getZ() > -56.82 && player.getZ() < -20.49){
					world.setRoom("catacombs");
					player.setX(95f);
					player.setZ(-30f);
					player.setHeading(1.5f);
				}
				else if(player.getZ() > 161.44 && player.getZ() < 196.24){
					System.out.println("Opening Door 3");
				}
			}
			else if(player.getX() < -105 & use > 0){
				if(player.getZ() > -56.82 && player.getZ() < -20.49){
					world.setRoom("hallway");
					player.setX(95f);
					player.setZ(-30f);
					player.setHeading(1.5f);
				}
				else if(player.getZ() > 161.44 && player.getZ() < 196.24){
					System.out.println("Opening Door 4");
				}
			}
		}
		else if(room.equals("hallway")){
			//WALLS
			//South
			if(100 < player.getX()){
				if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);
			}
			//North
			else if(-122 > player.getX()){
				if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
			}
			//East
			else if(-100 > player.getZ()){
	        	if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
	        }
			//West
			else if(52 < player.getZ()){
	        	if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
	        }
			//Room specific collisions
			if(player.getX() > 19){
				if(player.getZ() < -48 || player.getZ() > -20){
					if(ford) player.setFord(-(float)period);		
					if(back) player.setBack(-(float)period);		
					if(strafel) player.setStrafel(-(float)period);		
					if(strafer) player.setStrafer(-(float)period);
				}
			}
			else if(player.getX() < -28){
				if(player.getZ() < -48 || player.getZ() > -20){
					if(ford) player.setFord(-(float)period);		
					if(back) player.setBack(-(float)period);		
					if(strafel) player.setStrafel(-(float)period);		
					if(strafer) player.setStrafer(-(float)period);
				}
			}
			//DOORS
			if(use > 0){
				//SOUTH
				if(player.getZ() > -48 && player.getZ() < -20 && player.getX() > 96){
					setMainCorridor();
				}
				//NORTH
				else if(player.getZ() > -48 && player.getZ() < -20 && player.getX() < -118){
					setMainCorridor();
				}
				//EAST
				else if(player.getX() > -28 && player.getX() < 19 && player.getZ() < -96){
					setMainCorridor();
				}
				//WEST
				else if(player.getX() > -28 && player.getX() < 19 && player.getZ() > 22){
					setMainCorridor();
				}
			}
			
		}
		else if(room.equals("catacombs")){
			//WALLS
			//South
			if(100 < player.getX()){
				if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);
			}
			//North
			else if(-122 > player.getX()){
				if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
			}
			//East
			else if(-100 > player.getZ()){
	        	if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
	        }
			//West
			else if(52 < player.getZ()){
	        	if(ford) player.setFord(-(float)period);		
				if(back) player.setBack(-(float)period);		
				if(strafel) player.setStrafel(-(float)period);		
				if(strafer) player.setStrafer(-(float)period);	
	        }
			if(use > 0){
				//SOUTH
				if(player.getZ() > -48 && player.getZ() < -20 && player.getX() > 96){
					setMainCorridor();
				}
				//NORTH
				else if(player.getZ() > -48 && player.getZ() < -20 && player.getX() < -118){
					setMainCorridor();
				}
			}
		}
	}
	
	public void setMainCorridor(){
		world.setRoom("mainCorridor");
		player.setX(95f);
		player.setZ(-30f);
		player.setHeading(1.5f);
	}
}