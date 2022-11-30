package Main;

public class Message {
    public int m;
    public long id;
    
    public Message(int i, long id) {
    	m=i;
    	this.id = id;
    }
    
    @Override
    public String toString() {
    	return "Message "+m+" : Thread "+id;
    }
}
