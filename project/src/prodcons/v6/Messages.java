package prodcons.v6;

import Main.Message;

public class Messages {
    public Message[] messages;
    int index = 0;

    public Messages(int n, Message m) {
        this.messages = new Message[n];
        for (int i = 0; i < n; i++) {
            messages[i] = m;
            messages[i].parent = this;
        }
    }

    public Messages(int n, Message[] m, Message[] messages) {
        this.messages = m;
        for(Message mess : m)
        	mess.parent = this;
        this.messages = messages;
    }
    
    /*public synchronized void get() {
    	if(index == messages.length-1) {
    		// on est au dernier
    		notifyAll();
    	}else {
    		try {
    			index++;
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }*/
    
    public synchronized void w() {
    	//on ne libère pas tant que tous les messages ne sont pas consommés
    	while(!finished()) {
    		try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public synchronized void n() {
    	notifyAll();
    }
    
    public boolean finished() {
    	return index==messages.length;
    }
}