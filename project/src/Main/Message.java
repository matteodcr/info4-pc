package Main;

import prodcons.v6.Messages;

public class Message {
    public int m;
    public long id;
    public Messages parent;
    
    public Message(int i, long id) {
    	m=i;
    	this.id = id;
    }
    
    @Override
    public String toString() {
    	return "Message "+m+" : Thread "+id;
    }
}
