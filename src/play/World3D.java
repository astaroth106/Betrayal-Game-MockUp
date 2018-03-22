package play;

import java.awt.Point;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;


public class World3D implements GLEventListener {
	
	public Game game;

    private GLU glu = new GLU();
    
    private float[] spec = {1.0f, 1.0f, 1.0f, 1.0f};      //sets specular highlight of balls
    private float[] posl = {0.0f, 400f, 0.0f, 1.0f};      //position of ligth source
    private float[] amb2 = {0.1f, 0.1f, 0.1f, 1.0f};      //ambient of lightsource
    private float[] amb = {0.2f, 0.2f, 0.2f, 1.0f};      //global ambient
    
    public World3D(){}
    
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        shader(gl);
        game = new Game(gl);

        float df[] = {100f};
        System.out.println(gl.glGetString(GL2.GL_VERSION));
   
        gl.glEnable(GL2.GL_TEXTURE_2D);							// Enable Texture Mapping
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);					// Set The Blending Function For Translucency
        gl.glShadeModel(GL2.GL_SMOOTH);                            //Enables Smooth Color Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               //This Will Clear The Background Color To Black
        gl.glClearDepth(1.0);                                  //Enables Clearing Of The Depth Buffer
        gl.glEnable(GL2.GL_DEPTH_TEST);                            //Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LESS);                             //The Type Of Depth Test To Do
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);  // Really Nice Perspective Calculations
        
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, df, 0);

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posl, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, amb2, 0);
        gl.glEnable(GL2.GL_LIGHT0);

        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, amb, 0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
        
    }

    public void display(GLAutoDrawable drawable) {   	
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);       //Clear The Screen And The Depth Buffer 
        gl.glLoadIdentity();   	                                     		//Reset The View      
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        game.tick();     
    }
    

	public void reshape(GLAutoDrawable drawable, int xstart, int ystart, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        height = (height == 0) ? 1 : height;
        
        game.setMouseCenter(new Point(width*2, height));
       
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        glu.gluPerspective(75, (float) width / height, 0.1, 25);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

	public void shader(GL2 gl){
	    // Create a blank shader program, and grab its OpenGL-generated numeric ID
	    int shaderProgramID = gl.glCreateProgram();

	    // Create a new FRAGMENT SHADER from scratch
	    int fragmentShaderID = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);

	    // Compile this shader
	    //gl.glGetShaderiv(fragmentShaderID, 1, "demos/data/shaders/shader.glsl", 0);
	    gl.glCompileShader(fragmentShaderID);

	    // Our new fragmentShader is stored in numeric handle fragmentShaderID
	    // It is now available for use, so let's attach it to our shaderProgram
	    gl.glAttachShader(shaderProgramID, fragmentShaderID);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		
	}
}

