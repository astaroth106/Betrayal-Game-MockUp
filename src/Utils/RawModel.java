package Utils;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

public class RawModel {

	private List<Vector3<Float>> vertices = new ArrayList<Vector3<Float>>();
	private List<Vector2<Float>> textures = new ArrayList<Vector2<Float>>();
    private List<Vector3<Float>> normals = new ArrayList<Vector3<Float>>();
    private List<Indices> indices = new ArrayList<Indices>();
    private int texture;
	
	public RawModel(List<Vector3<Float>> vertices, List<Vector2<Float>> textures, List<Vector3<Float>> normals, List<Indices> indices){
		
		this.vertices = vertices;
		this.textures = textures;
		this.normals = normals;
		this.indices = indices;
		
	}

	public List<Vector3<Float>> getVertices() {
		return vertices;
	}

	public void setVertices(List<Vector3<Float>> vertices) {
		this.vertices = vertices;
	}

	public List<Vector2<Float>> getTextures() {
		return textures;
	}

	public void setTextures(List<Vector2<Float>> textures) {
		this.textures = textures;
	}

	public List<Vector3<Float>> getNormals() {
		return normals;
	}

	public void setNormals(List<Vector3<Float>> normals) {
		this.normals = normals;
	}

	public List<Indices> getIndices() {
		return indices;
	}

	public void setIndices(List<Indices> indices) {
		this.indices = indices;
	}
	
	public void setTexture(int texture){
		this.texture = texture;
	}
	
	public void draw(GL2 gl){
		float x = 0,y = 0,z = 0,u = 0,v = 0, n1 = 0, n2 = 0, n3 = 0;
		gl.glPushMatrix();
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
    	for (Indices i : this.getIndices()){
    		gl.glBegin(GL2.GL_TRIANGLES);
    		
    		x = this.getVertices().get(i.vertices[0]-1).x;
            y = this.getVertices().get(i.vertices[0]-1).y;
            z = this.getVertices().get(i.vertices[0]-1).z;
            u = this.getTextures().get(i.textures[0]-1).x;
            v = this.getTextures().get(i.textures[0]-1).y;
            n1 = this.getNormals().get(i.normals[0]-1).x;
            n2 = this.getNormals().get(i.normals[0]-1).y;
            n3 = this.getNormals().get(i.normals[0]-1).z;
            Vector3<Float> vector = new Vector3<Float>(x, y, z);
            vector = Scale(vector, 3);
            gl.glNormal3f(n1, n2, n3);
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(vector.x*100, vector.y*2, vector.z*100);
                   
            x = this.getVertices().get(i.vertices[1]-1).x;
            y = this.getVertices().get(i.vertices[1]-1).y;
            z = this.getVertices().get(i.vertices[1]-1).z;
            u = this.getTextures().get(i.textures[1]-1).x;
            v = this.getTextures().get(i.textures[1]-1).y;
            n1 = this.getNormals().get(i.normals[1]-1).x;
            n2 = this.getNormals().get(i.normals[1]-1).y;
            n3 = this.getNormals().get(i.normals[1]-1).z;
            vector = new Vector3<Float>(x, y, z);
            vector = Scale(vector, 3);
            gl.glNormal3f(n1, n2, n3);
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(vector.x*100, vector.y*2, vector.z*100);
            
            x = this.getVertices().get(i.vertices[2]-1).x;
            y = this.getVertices().get(i.vertices[2]-1).y;
            z = this.getVertices().get(i.vertices[2]-1).z;
            u = this.getTextures().get(i.textures[2]-1).x;
            v = this.getTextures().get(i.textures[2]-1).y;
            n1 = this.getNormals().get(i.normals[2]-1).x;
            n2 = this.getNormals().get(i.normals[2]-1).y;
            n3 = this.getNormals().get(i.normals[2]-1).z;
            vector = new Vector3<Float>(x, y, z);
            vector = Scale(vector, 3);
            gl.glNormal3f(n1, n2, n3);
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(vector.x*100, vector.y*2, vector.z*100);
            gl.glEnd();
    	}       
        gl.glPopMatrix();
	}
	
	public Vector3<Float> Scale(Vector3<Float> vertex, float scale){
		vertex.x *= scale;
		vertex.y *= scale;
		vertex.z *= scale;
		
		return vertex;
	}
	
}