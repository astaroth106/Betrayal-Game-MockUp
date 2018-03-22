package play;

import Utils.KeyHandling;

public class Main {

	
	public static void main(String[] args) {			
		World3D renderer = new World3D();
		KeyHandling handler = new KeyHandling(renderer);
        Window window = Window.createGLDisplay("Project", renderer);
        
        window.addKeyListener(handler);
        window.addMouseListener(handler);
        window.start();
	}
}