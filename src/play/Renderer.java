package play;


import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;


@SuppressWarnings("serial")
public class Renderer extends GLCanvas implements GLEventListener, KeyListener, MouseListener {
	
	private static final int BUFSIZE = 512;
	private Point pickPoint = new Point();
	float rtri=0.2f, rquad=0.15f;	
	float[] LightAmbient=		{ 0f, 0f, 0.5f, 1.0f };
	float[] DiffuseAmbient=		{ 1f, 0.5f, 1f, 1.0f };
	float[] coords = {0f, 0f, 0f};
	float[][] coordsShape = {{0f, 0f, 0f}, {0f, 0f, 0f}, {0f, 0f, 0f}, {0f, 0f, 0f}, {0f, 0f, 0f}};
	float[][] color = {{0f, 0f, 0f}, {0f, 0f, 0f}, {0f, 0f, 0f}, {0f, 0f, 0f}, {0f, 0f, 0f}};
	boolean[] flags = {false, false, false, false, false};
	float[] mquad = {0.8f, 0.0f};
	String shape = " ";

	
	PopupMenu popup; 
	PopupMenu[] menuItem = {new PopupMenu(), new PopupMenu(), new PopupMenu(), new PopupMenu(), new PopupMenu()};
	GLU glu = new GLU();
	GLUT glut = new GLUT();

	public Renderer(){
		this.addGLEventListener(this);
		this.addKeyListener(this);
		this.addMouseListener(this);

		this.setFocusable(true);
		this.requestFocus();
		
	}
	
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	    gl.glDepthFunc(GL2.GL_LESS);
	    gl.glEnable(GL2.GL_DEPTH_TEST);
	    gl.glShadeModel(GL2.GL_FLAT);
	    gl.glDepthRange(0.0, 1.0);
		
		setCamera(gl, glu, 1);
		setMenuItems();
		this.add(popup);
		
		for(int i = 0; i<5; i++){
			//shapeList[i] = new Shape(coords, color, shape);
		}
		
		try {
			setSavedValues();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setSavedValues() throws IOException {
		BufferedReader bfr;
        String line;
        File file=new File("src/savedSetting.txt");       
        if(!file.exists()){
            file.createNewFile();
        }    
        try{
        	int i = 0;
	        bfr=new BufferedReader(new FileReader(file));
	        while((line=bfr.readLine())!=null){
	        	flags[i] = Boolean.valueOf(line);
	        	for(int j = 0; j < 3; j ++){
	        		line=bfr.readLine();
	        		color[i][j] = Float.valueOf(line);
	        		line=bfr.readLine();
	        		coordsShape[i][j] = Float.valueOf(line);
	        	}
	        	i++;
	        }
	        bfr.close();
        } catch (IOException e) {
			e.printStackTrace();
		}
        
	}

	public void dispose(GLAutoDrawable drawable) {
		//final GL2 gl2 = drawable.getGL().getGL2();
		this.dispose(drawable);
		this.disposeGLEventListener(this, true);
	}

	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
		gl.glClearColor(0.6f, 0.7f, 0.5f, 1f);
		
		if (pickPoint != null) pickRects(gl);
		drawRects(gl, GL2.GL_RENDER, glut);	
        
		//rtri += 0.4f;
		//rquad += 0.5f;

       gl.glFlush();
        
		
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = (float) this.getWidth() / (float) this.getHeight();
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, 0, 4, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
	}
	
    private void setCamera(GL2 gl, GLU glu, float distance) {
        // Change to projection matrix.
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = (float) this.getHeight() / (float) this.getWidth();
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
  
    @Override
    public void keyPressed(KeyEvent e) {
       int keyCode = e.getKeyCode();
       switch (keyCode) {
          case KeyEvent.VK_ESCAPE:
        	  File file=new File("src/savedSetting.txt");       
              try {
				FileWriter fw=new FileWriter(file,false);
				for(int i = 0; i < 5; i++){
					fw.append(String.valueOf(flags[i])+"\n");
					
					for(int j = 0; j < 3; j++){
						fw.append(String.valueOf(color[i][j])+"\n");
						fw.append(String.valueOf(coordsShape[i][j])+"\n");
					}
				}
				fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				
			} 
            new Thread() {
                @Override
                public void run() {
                	System.out.println("Closing Animation");
                   GLAnimatorControl animator = getAnimator();
                   if (animator.isStarted()) animator.stop();
                   System.exit(0);
                }
             }.start();
             break;
          case KeyEvent.VK_UP:
        	 mquad[1] += 0.01f;
        	  break;
          case KeyEvent.VK_DOWN:
         	 mquad[1] -= 0.01f;
         	  break;
          case KeyEvent.VK_LEFT:
         	 mquad[0] -= 0.01f;
         	  break;
          case KeyEvent.VK_RIGHT:
         	 mquad[0] += 0.01f;
         	  break;
       }
    }
  
    @Override
    public void keyReleased(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int mouse = e.getButton();
		switch (mouse){
		case MouseEvent.BUTTON3:
			coords[0] = xCon(e.getX());
			coords[1] = yCon(e.getY());
			
			popup.show(this, e.getX(), e.getY());
			break;
		case MouseEvent.BUTTON1:
			pickPoint = e.getPoint();
			break;
		}
		
	}
	
	public void setMenuItems(){
		popup = new PopupMenu();
		
		MenuItem clearScreen = new MenuItem();
		
		MenuItem[][] subMenuItem = {{new MenuItem(), new MenuItem(), new MenuItem(), new MenuItem()}, 
						{new MenuItem(), new MenuItem(), new MenuItem(), new MenuItem()},
						{new MenuItem(), new MenuItem(), new MenuItem(), new MenuItem()}, 
						{new MenuItem(), new MenuItem(), new MenuItem(), new MenuItem()}, 
						{new MenuItem(), new MenuItem(), new MenuItem(), new MenuItem()}};
		
		final String[] colors = {"Red", "Green", "Blue", "Orange"}, shapes = {"Pyramid", "Cube", "Square", "Teapot", "Triangle"};
		
		for(int i = 0; i < 5; i++){
			menuItem[i].setLabel(shapes[i]);
		}
		
		for (int i = 0; i < 5; i++){
			for(int j=0; j < 4; j++){
			subMenuItem[i][j].setLabel(colors[j]);
			}
		}
		
		clearScreen.setLabel("Start Over");
		for (int i = 0; i < 4; i++){
			subMenuItem[0][i].addActionListener(new Pyramid());
			subMenuItem[1][i].addActionListener(new Cube());
			subMenuItem[2][i].addActionListener(new Square());
			subMenuItem[3][i].addActionListener(new Teapot());
			subMenuItem[4][i].addActionListener(new Triangle());
		}
		
		for (int i = 0; i < 5; i++){
			subMenuItem[i][0].addActionListener(new Red());
			subMenuItem[i][1].addActionListener(new Green());
			subMenuItem[i][2].addActionListener(new Blue());
			subMenuItem[i][3].addActionListener(new Orange());
		}
			
		clearScreen.addActionListener(new ClearScreen());
		
		for(int i = 0; i <5; i++){
			popup.add(menuItem[i]);	
			for(int j = 0; j < 4; j++){
				menuItem[i].add(subMenuItem[i][j]);
			}
		}

		popup.addSeparator();
		popup.add(clearScreen);
		
	}

	
	private class Pyramid implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			flags[0] = true;
			coordsShape[0][0] = coords[0];
			coordsShape[0][1] = coords[1]; 
			shape = "Pyramid";
		}
		
	}
	private class Cube implements ActionListener{
			
		@Override
		public void actionPerformed(ActionEvent e) {
			flags[1] = true;
			coordsShape[1][0] = coords[0]; 
			coordsShape[1][1] = coords[1]; 
			shape = "Cube";
		}
		
		}
	private class Square implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			flags[2] = true;
			coordsShape[2][0] = coords[0];
			coordsShape[2][1] = coords[1]; 
			shape = "Square";
		}
		
	}
	private class Teapot implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			flags[3] = true;
			coordsShape[3][0] = coords[0]; 
			coordsShape[3][1] = coords[1]; 
			shape = "Teapot";
		}
		
	}
	private class Triangle implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			flags[4] = true;
			coordsShape[4][0] = coords[0]; 
			coordsShape[4][1] = coords[1]; 
			shape = "Triangle";
		}
		
	}
	
	private class Orange implements ActionListener{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(shape.equals("Pyramid")){
					color[0][0] = 1.0f;
					color[0][1] = 0.65f; 
					color[0][2] = 0.0f;
				}
				else if(shape.equals("Cube")){
					color[1][0] = 1.0f;
					color[1][1] = 0.65f; 
					color[1][2] = 0.0f;
				}
				else if(shape.equals("Square")){
					color[2][0] = 1.0f;
					color[2][1] = 0.65f; 
					color[2][2] = 0.0f;
				}
				else if(shape.equals("Teapot")){
					color[3][0] = 1.0f;
					color[3][1] = 0.65f; 
					color[3][2] = 0.0f;
				}
				if(shape.equals("Triangle")){
					color[4][0] = 1.0f;
					color[4][1] = 0.65f; 
					color[4][2] = 0.0f;
				}
			}
			
		}

	private class Red implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(shape.equals("Pyramid")){
				color[0][0] = 1.0f;
				color[0][1] = 0.0f; 
				color[0][2] = 0.0f;
			}
			else if(shape.equals("Cube")){
				color[1][0] = 1.0f;
				color[1][1] = 0.0f; 
				color[1][2] = 0.0f;
			}
			else if(shape.equals("Square")){
				color[2][0] = 1.0f;
				color[2][1] = 0.0f; 
				color[2][2] = 0.0f;
			}
			else if(shape.equals("Teapot")){
				color[3][0] = 1.0f;
				color[3][1] = 0.0f; 
				color[3][2] = 0.0f;
			}
			if(shape.equals("Triangle")){
				color[4][0] = 1.0f;
				color[4][1] = 0.0f; 
				color[4][2] = 0.0f;
			}
		}
		
	}
	
	private class Green implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(shape.equals("Pyramid")){
				color[0][0] = 0.0f;
				color[0][1] = 1.0f; 
				color[0][2] = 0.0f;
			}
			else if(shape.equals("Cube")){
				color[1][0] = 0.0f;
				color[1][1] = 1.0f; 
				color[1][2] = 0.0f;
			}
			else if(shape.equals("Square")){
				color[2][0] = 0.0f;
				color[2][1] = 1.0f;  
				color[2][2] = 0.0f;
			}
			else if(shape.equals("Teapot")){
				color[3][0] = 0.0f;
				color[3][1] = 1.0f;  
				color[3][2] = 0.0f;
			}
			if(shape.equals("Triangle")){
				color[4][0] = 0.0f;
				color[4][1] = 1.0f;  
				color[4][2] = 0.0f;
			}
		}
		
	}
	
	private class Blue implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(shape.equals("Pyramid")){
				color[0][0] = 0.0f;
				color[0][1] = 0.0f; 
				color[0][2] = 1.0f;
			}
			else if(shape.equals("Cube")){
				color[1][0] = 0.0f;
				color[1][1] = 0.0f; 
				color[1][2] = 1.0f;
			}
			else if(shape.equals("Square")){
				color[2][0] = 0.0f;
				color[2][1] = 0.0f; 
				color[2][2] = 1.0f;
			}
			else if(shape.equals("Teapot")){
				color[3][0] = 0.0f;
				color[3][1] = 0.0f; 
				color[3][2] = 1.0f;
			}
			if(shape.equals("Triangle")){
				color[4][0] = 0.0f;
				color[4][1] = 0.0f; 
				color[4][2] = 1.0f;
			}
		}
		
	}
	
	private class ClearScreen implements ActionListener{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				for (int i=0; i < flags.length; i++){
					flags[i] = false;
				}
			}
			
	}
	
	//Returns and x position
	public float xCon(float x)
	{
		float xNew;
		xNew=x/(this.getWidth()/2.0f);
		xNew=xNew-1.0f;

		return xNew;
	}
	//Returns a Y position
	public float yCon(float y)
	{
		float yNew;
		yNew=y/(this.getHeight()/2.0f);
		yNew=yNew-1.0f;
		yNew=-yNew;

		return yNew;
	}
	
	private void drawRects(GL2 gl, int mode, GLUT glut)
	  {
	    if (mode == GL2.GL_SELECT) gl.glLoadName(5);

		if(flags[0]){
			
			gl.glLoadIdentity();                                       
	        gl.glTranslatef(coordsShape[0][0],coordsShape[0][1], 0f);						
	        gl.glRotatef(rtri,0f,0.1f,0.0f);						
	        gl.glBegin(GL2.GL_TRIANGLES);								
	        gl.glColor3f(color[0][0],color[0][1],color[0][2]);						
	        gl.glVertex3f( 0.0f, 0.1f, 0.0f);					
	        gl.glVertex3f(-0.1f,-0.1f, 0.1f);					
	        gl.glVertex3f( 0.1f,-0.1f, 0.1f);					
	        //						
	        gl.glVertex3f( 0.0f, 0.1f, 0.0f);					
	        gl.glVertex3f( 0.1f,-0.1f, 0.1f);					
	        gl.glVertex3f( 0.1f,-0.1f, -0.1f);					
	        //					
	        gl.glVertex3f( 0.0f, 0.1f, 0.0f);					
	        gl.glVertex3f( 0.1f,-0.1f, -0.1f);					
	        gl.glVertex3f(-0.1f,-0.1f, -0.1f);					
	        //					
	        gl.glVertex3f( 0.0f, 0.1f, 0.0f);					
	        gl.glVertex3f(-0.1f,-0.1f,-0.1f);					
	        gl.glVertex3f(-0.1f,-0.1f, 0.1f);					
	        gl.glEnd();											
		}
		if(flags[1]){
			
			gl.glLoadIdentity();						 
			gl.glTranslatef(coordsShape[1][0],coordsShape[1][1],0f);						
	        gl.glRotatef(rquad,0.1f,0.1f,0.1f);					
	        gl.glBegin(GL2.GL_QUADS);									
	        gl.glColor3f(color[1][0],color[1][1],color[1][2]);							
	        gl.glVertex3f( 0.1f, 0.1f,-0.1f);					
	        gl.glVertex3f(-0.1f, 0.1f,-0.1f);					
	        gl.glVertex3f(-0.1f, 0.1f, 0.1f);					
	        gl.glVertex3f( 0.1f, 0.1f, 0.1f);					
	        //						
	        gl.glVertex3f( 0.1f,-0.1f, 0.1f);					
	        gl.glVertex3f(-0.1f,-0.1f, 0.1f);					
	        gl.glVertex3f(-0.1f,-0.1f,-0.1f);					
	        gl.glVertex3f( 0.1f,-0.1f,-0.1f);					
	        //						
	        gl.glVertex3f( 0.1f, 0.1f, 0.1f);					
	        gl.glVertex3f(-0.1f, 0.1f, 0.1f);					
	        gl.glVertex3f(-0.1f,-0.1f, 0.1f);					
	        gl.glVertex3f( 0.1f,-0.1f, 0.1f);					
	        //						
	        gl.glVertex3f( 0.1f,-0.1f,-0.1f);					
	        gl.glVertex3f(-0.1f,-0.1f,-0.1f);					
	        gl.glVertex3f(-0.1f, 0.1f,-0.1f);					
	        gl.glVertex3f( 0.1f, 0.1f,-0.1f);					
	        //						
	        gl.glVertex3f(-0.1f, 0.1f, 0.1f);					
	        gl.glVertex3f(-0.1f, 0.1f,-0.1f);					
	        gl.glVertex3f(-0.1f,-0.1f,-0.1f);					
	        gl.glVertex3f(-0.1f,-0.1f, 0.1f);					
	        //					
	        gl.glVertex3f( 0.1f, 0.1f,-0.1f);					
	        gl.glVertex3f( 0.1f, 0.1f, 0.1f);					
	        gl.glVertex3f( 0.1f,-0.1f, 0.1f);					
	        gl.glVertex3f( 0.1f,-0.1f,-0.1f);					
	        gl.glEnd();		
		}
		if(flags[2]){
			gl.glLoadIdentity();	
	        gl.glTranslatef(coordsShape[2][0],coordsShape[2][1],0f);						
	        gl.glRotatef(rquad, 1f,0f,0f);					
	        gl.glBegin(GL2.GL_QUADS);									
	        gl.glColor3f(color[2][0],color[2][1],color[2][2]);						
	        gl.glVertex3f( 0.1f, 0.1f,0f);					
	        gl.glVertex3f(-0.1f, 0.1f,0f);					
	        gl.glVertex3f(-0.1f, -0.1f, 0f);					
	        gl.glVertex3f( 0.1f, -0.1f, 0f);		
	     	gl.glEnd();
		}
		if(flags[3]){
			gl.glLoadIdentity();
			gl.glTranslatef(coordsShape[3][0], coordsShape[3][1], 0.0f);	
			gl.glColor3f(color[3][0],color[3][1],color[3][2]);
			glut.glutSolidTeapot(0.2);
		}
		if(flags[4]){
			gl.glLoadIdentity();
			gl.glTranslatef(coordsShape[4][0], coordsShape[4][1], 0.0f);
			gl.glBegin(GL2.GL_TRIANGLES); 
			gl.glColor3f(color[4][0],color[4][1],color[4][2]);
			gl.glVertex3f(0.0f,0.2f,0.0f);
			gl.glVertex3f(0.2f,0.2f,0.0f);
			gl.glVertex3f(0.2f,0.0f,0.0f);
			gl.glEnd();
		}
	  }
	 
	  /*
	   * prints out the contents of the selection array.
	   */
	  private void processHits(int hits, int buffer[])
	  {
	    int names, ptr = 0;
	 
	    System.out.println("hits = " + hits);
	    // ptr = (GLuint *) buffer;
	    for (int i = 0; i < hits; i++)
	    { /* for each hit */
	      names = buffer[ptr];
	      //System.out.println(" number of names for hit = " + names);
	      ptr++;
	      //System.out.println("  z1 is " + buffer[ptr]);
	      ptr++;
	      //System.out.println(" z2 is " + buffer[ptr]);
	      ptr++;
	      //System.out.print("\n   the name is ");
	      for (int j = 0; j < names; j++)
	      { /* for each name */
	        System.out.println("" + buffer[ptr]);
	        ptr++;
	      }
	      System.out.println();
	    }
	  }
	 
	  /*
	   * sets up selection mode, name stack, and projection matrix for picking. Then
	   * the objects are drawn.
	   */
	  private void pickRects(GL2 gl)
	  {
	    int[] selectBuf = new int[BUFSIZE];
	    IntBuffer selectBuffer = Buffers.newDirectIntBuffer(BUFSIZE);
	    int hits;
	    int[] viewport = new int[5];
	    // int x, y;
	 
	    gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
	 
	    gl.glSelectBuffer(BUFSIZE, selectBuffer);
	    gl.glRenderMode(GL2.GL_SELECT);
	 
	    gl.glInitNames();
	    gl.glPushName(-1);
	 
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glPushMatrix();
	    gl.glLoadIdentity();
	    /* create 5x5 pixel picking region near cursor location */
	    glu.gluPickMatrix((double) pickPoint.x,
	        (double) (viewport[3] - pickPoint.y), //
	        5.0, 5.0, viewport, 0);
	    gl.glOrtho(0.0, 8.0, 0.0, 8.0, -0.5, 2.5);
	    drawRects(gl, GL2.GL_SELECT, glut);
	    gl.glPopMatrix();
	    gl.glFlush();
	 
	    hits = gl.glRenderMode(GL2.GL_RENDER);
	    selectBuffer.get(selectBuf);
	    processHits(hits, selectBuf);
	  }

}

