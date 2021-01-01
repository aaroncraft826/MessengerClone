import java.util.LinkedList;

public class Room {
	final private LinkedList<String> chat = new LinkedList<String>();
	final private int roomId;
	
	public Room(int roomId) {
		this.roomId = roomId;
		System.out.println("Room " + roomId + " created.");
	}
	
	public void addMessage(String name, String msg) {
		chat.addFirst(name + "|" + msg);
		System.out.println("In Room " + roomId + ", " + name + " said: " + msg);
	}
}