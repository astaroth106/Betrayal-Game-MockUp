package Utils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;


import java.io.InputStreamReader;

public class LoadModel {
 
    public static RawModel loadObjModel(String fileName) {
        String line;
        List<Vector3<Float>> vertices = new ArrayList<Vector3<Float>>();
        List<Vector2<Float>> textures = new ArrayList<Vector2<Float>>();
        List<Vector3<Float>> normals = new ArrayList<Vector3<Float>>();
        List<Indices> indices = new ArrayList<Indices>();

        try {
            fileName = "demos/data/models/" + fileName + ".obj";
            BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceRetriever.getResourceAsStream(fileName)));
            line = reader.readLine(); line = reader.readLine(); line = reader.readLine(); line = reader.readLine();
            while (line != null) {
                
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3<Float> vertex = new Vector3<Float>(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2<Float> texture = new Vector2<Float>(Float.parseFloat(currentLine[1]), 1-Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                	Vector3<Float> normal = new Vector3<Float>(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {

                    String[] currentLine1 = line.split(" ");
                    
                    int[] vertexIndex = new int[3], textureIndex = new int[3], normalIndex = new int[3];
                    
                    String[] stV = currentLine1[1].split("/");
                    vertexIndex[0] = Integer.valueOf(stV[0]);
                    textureIndex[0] = Integer.valueOf(stV[1]);
                    normalIndex[0] = Integer.valueOf(stV[2]);
       
                    String[] stUV = currentLine1[2].split("/");
                    vertexIndex[1] = Integer.valueOf(stUV[0]);
                    textureIndex[1] = Integer.valueOf(stUV[1]);
                    normalIndex[1] = Integer.valueOf(stUV[2]);
   
                    String[] stN = currentLine1[3].split("/");
                    vertexIndex[2] = Integer.valueOf(stN[0]);
                    textureIndex[2] = Integer.valueOf(stN[1]);
                    normalIndex[2] = Integer.valueOf(stN[2]);
                    
                    indices.add(new Indices(vertexIndex, textureIndex, normalIndex));
                    
                }
                line = reader.readLine();
            }      
            reader.close();      
        } catch (Exception e) {
        	System.err.println("Couldn't load file!");
            e.printStackTrace();
        }
        return new RawModel(vertices, textures, normals, indices);

    }
}