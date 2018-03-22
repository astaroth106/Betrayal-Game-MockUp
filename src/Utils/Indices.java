package Utils;

public class Indices {

	public int[] vertices = new int[3];
    public int[] textures = new int[2];
    public int[] normals = new int[3];
	
	public Indices(int[] vertices, int[] textures, int[] normals){
		
		this.vertices = vertices;
		this.textures = textures;
		this.normals = normals;
	}

	public int[] getVertices() {
		return vertices;
	}

	public void setVertices(int[] vertices) {
		this.vertices = vertices;
	}

	public int[] getTextures() {
		return textures;
	}

	public void setTextures(int[] textures) {
		this.textures = textures;
	}

	public int[] getNormals() {
		return normals;
	}

	public void setNormals(int[] normals) {
		this.normals = normals;
	}
	
}