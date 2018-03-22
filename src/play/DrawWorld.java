package play;

import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;

import Utils.LoadModel;
import Utils.LoadTextures;
import Utils.RawModel;

public class DrawWorld {
	
	private int[] texture = new int[4];			// Storage For 4 Textures
    private String room = "mainCorridor";

    RawModel mainCorridor;
    RawModel Rooms[] = {null, null, null, null};
	
    public DrawWorld(GL2 gl){
	    try {
			texture = LoadTextures.loadGLTextures(gl);
		} catch (GLException | IOException e) {
			e.printStackTrace();
		} 
	    mainCorridor = LoadModel.loadObjModel("mainCorridor");
	    Rooms[0] = LoadModel.loadObjModel("hallway");
	    Rooms[1] = LoadModel.loadObjModel("catacombs");
	    
	    mainCorridor.setTexture(texture[0]);
	    Rooms[0].setTexture(texture[0]);
	    Rooms[1].setTexture(texture[1]);
    }
    
    public void draw(GL2 gl){
    	
        gl.glTranslatef(0.0f, 0.45f, 0f);
        if(getRoom().equals("mainCorridor")){
        	mainCorridor.draw(gl);
        }
        else if(getRoom().equals("hallway")){
        	Rooms[0].draw(gl);
        }
        else if(getRoom().equals("catacombs")){
        	Rooms[1].draw(gl);
        }
        else if(getRoom().equals("diningRoom")){
        	
        }
    }

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

}
