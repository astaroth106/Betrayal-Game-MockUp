package Utils;

import java.io.IOException;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;

import Utils.TextureReader.Texture;

public class LoadTextures {
	
	public static int[] loadGLTextures(GL2 gl) throws IOException, GLException {
		String path = "demos/data/textures/";
		String[] text = {path+"mainCorridor.png", path+"catacombs.png", path+"wallpaper.png", path+"box_t.png"};
		ArrayList<TextureReader.Texture> texture = new ArrayList<Texture>(100);
		for(int i = 0; i < text.length; i++){
			texture.add(TextureReader.readTexture(text[i]));
		}
	    int[] textures = new int[4];
	    
	    //Create Nearest Filtered Texture
	    gl.glGenTextures(4, textures, 0);
	    
	    for(int i = 0; i < text.length; i++){
		    gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[i]);
		    gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 4);
		    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_DEPTH_TEXTURE_MODE, GL2.GL_LINEAR); 
	        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
		
		    gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, 3, texture.get(i).getWidth(), texture.get(i).getHeight(), 
		    		0, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, texture.get(i).getPixels());
	    }
		return textures;
	}

}
