package Utils;

public class Room {
	
	private boolean open = false;
	private int roomId;
	private Door[] doors = {null, null, null, null};
	private String roomName;
	
	Room(int roomId, String roomName, int doors){
		this.open = true;
		this.roomId = roomId;
		this.roomName = roomName;
		for(int i=0; i<doors; i++)
			this.doors[i] = new Door(i);
	}

}
