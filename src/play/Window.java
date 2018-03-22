package play;

import javax.swing.*;


import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class Window {
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;


    private JFrame frame;
    private GLCanvas glCanvas;
    private FPSAnimator animator;
    private int width;
    private int height;

    public static Window createGLDisplay( String title, GLEventListener listener ) {
        return new Window( title, DEFAULT_WIDTH, DEFAULT_HEIGHT, listener );
    }

    private Window( String title, int width, int height, GLEventListener listener) {
    	GLProfile glp = GLProfile.getDefault(); 
        glCanvas = new GLCanvas(new GLCapabilities(glp));
        glCanvas.setSize( width, height );
        glCanvas.setIgnoreRepaint( true );
        glCanvas.addGLEventListener(listener);

        frame = new JFrame( title );
        frame.getContentPane().setLayout( new BorderLayout() );
        frame.getContentPane().add( glCanvas, BorderLayout.CENTER );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        this.setWidth(width);
        this.setHeight(height);

        animator = new FPSAnimator(glCanvas, 60 );
        animator.start();
    }

    public void start() {
        try {
        	Toolkit t = Toolkit.getDefaultToolkit();
        	Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Cursor noCursor = t.createCustomCursor(i, new Point(0, 0), "none"); 
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            
            frame.addWindowListener( new MyWindowAdapter() );

                frame.setSize( frame.getContentPane().getPreferredSize() );
                frame.setLocation(
                        ( screenSize.width - frame.getWidth() ) / 2,
                        ( screenSize.height - frame.getHeight() ) / 2
                );
                frame.setCursor(noCursor);
                
                frame.setVisible( true );
                

            glCanvas.requestFocus();
            
            
            animator.start();
        } catch ( Exception e ) {
           
        }
    }

    public void stop() {
        try {
            animator.stop();
            frame.dispose();
        } catch ( Exception e ) {

        } finally {
            System.exit( 0 );
        }
    }


    public void addKeyListener( KeyListener l ) {
        glCanvas.addKeyListener( l );
    }

    public void addMouseListener( MouseListener l ) {
        glCanvas.addMouseListener( l );
    }

    public String getTitle() {
        return frame.getTitle();
    }

    public void setTitle( String title ) {
        frame.setTitle( title );
    }


    public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

    private class MyWindowAdapter extends WindowAdapter {
        public void windowClosing( WindowEvent e ) {
            stop();
        }
    }

}
