package Entities;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;

public class Player {
	
	private static final float _90=(float)Math.toRadians(90);
	private static final float _maxPitch=(float)Math.toRadians(60);	
	float heading=0.0f;	
	private float pitch=0.0f;	
	private float cosa, cosb, cosz, sina, sinb, sinz;	
	private float cosc=1.0f;	
	private float sinc=0.0f;
	private float sprint=1;
	private float x, y, z;
	
	private float light1_ambient[] = { 0.2f, 0.2f, 0.2f, 1.0f };
	private float light1_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float light1_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float light1_position[] = { -2.0f, 2.0f, 1.0f, 1.0f };
	private float spot_direction[] = { -1.0f, -1.0f, 0.0f };
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	private float[] mat={ 1,0,0,0,							
						  0,1,0,0,							
						  0,0,1,0,
						  0,0,0,1};
	
	private FloatBuffer matrix ;
	
	public Player(){		
		matrix=Buffers.newDirectFloatBuffer(mat.length);		
		matrix.put(mat);		
		x=z=0.0f;		
		y=-1.2f;	
	}
	
	public void setHeading(float amount){		
		heading-=amount;		
		cosb=(float)Math.toRadians(Math.cos(heading));		
		sinb=(float)Math.toRadians(Math.sin(heading));		
		cosz=(float)Math.toRadians(Math.cos(heading+_90));		
		sinz=(float)Math.toRadians(Math.sin(heading+_90));	
	}
	
	public void setPitch(float amount){	
		pitch-=amount;		
		if(pitch>_maxPitch)pitch=_maxPitch;		
		if(pitch<-_maxPitch)pitch=-_maxPitch;		
		cosa=(float)Math.cos(pitch);		
		sina=(float)Math.sin(pitch);	
	}
	
	public void setFord(float amount){		
		x+=cosz*amount*sprint;		
		z+=sinz*amount*sprint;	
	}
	
	public void setBack(float amount){		
		x-=cosz*amount;	
		z-=sinz*amount;
	}
	
	public void setStrafel(float amount){	
		x+=cosb*amount;		
		z+=sinb*amount;	
	}
	
	public void setStrafer(float amount){		
		x-=cosb*amount;		
		z-=sinb*amount;	
	}
	

	public void setSprint(float period) {
		sprint = period;
	}
	
	public void set(){		
		matrix.put(0, cosc*cosb-sinc*sina*sinb);		
		matrix.put(1, sinc*cosb+cosc*sina*sinb);	
		matrix.put(2, -cosa*sinb);		
		matrix.put(4, -sinc*cosa);		
		matrix.put(5, cosc*cosa);		
		matrix.put(6, sina);		
		matrix.put(8, cosc*sinb+sinc*sina*cosb);		
		matrix.put(9, sinc*sinb-cosc*sina*cosb);		
		matrix.put(10, cosa*cosb);		
		matrix.put(12, matrix.get(0)*x+matrix.get(4)*y+matrix.get(8)*z);		
		matrix.put(13, matrix.get(1)*x+matrix.get(5)*y+matrix.get(9)*z);		
		matrix.put(14, matrix.get(2)*x+matrix.get(6)*y+matrix.get(10)*z);	
	}
	
	public void draw(GL2 gl){		
		gl.glLoadIdentity();
		
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, light1_ambient, 1);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, light1_diffuse, 1);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, light1_specular, 1);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, light1_position, 1);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_CONSTANT_ATTENUATION, 1.5f);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_LINEAR_ATTENUATION, 0.5f);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_QUADRATIC_ATTENUATION, 0.2f);

		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 45.0f);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, spot_direction, 1);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 2.0f);
		//System.out.println(x + " " + y + " " + z + " " + heading);
		
		matrix.rewind();		
		gl.glLoadMatrixf(matrix);	
	}

}