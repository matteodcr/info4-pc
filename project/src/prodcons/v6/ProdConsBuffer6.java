package prodcons.v6;

import Main.IProdConsBuffer;
import Main.Message;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer6 implements IProdConsBuffer {
    private int nempty, nfull, nb_message_buffer, taille_buffer, flux_msg, maxMess;
    private Semaphore fifoPut = new Semaphore(1), fifoGet = new Semaphore(1);
    private Message[] buffer;

    public ProdConsBuffer6(int taille_buffer) {
        this.nempty = 0;
        this.nfull = 0;
        this.taille_buffer = taille_buffer;
        this.buffer = new Message[taille_buffer];
        this.nb_message_buffer = 0;
        this.flux_msg = 0;
    }

    /**
     * Put the message m in the buffer
     *
     * @param m
     */
    @Override
    public void put(Message m) throws InterruptedException {
    	fifoPut.acquire();
        synchronized (this) {
            while (nb_message_buffer == taille_buffer)
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            buffer[nempty] = m;
            nempty = (nempty + 1) % (taille_buffer);
            nb_message_buffer++;
            flux_msg++;
            notifyAll();
        }
        fifoPut.release();
    }

    /**
     * Retrieve a message from the buffer
     * following a FIFO order (if M1 was put before M2, M1
     * is retrieved before M2)
     */
    @Override
    public Message get() throws InterruptedException {
    	return get(1)[0];
    }

    /**
     * Returns the number of messages currently available in
     * the buffer
     */
    @Override
    public int nmsg() {
        return nb_message_buffer;
    }

    /**
     * Returns the total number of messages that have been put
     * in the buffer since it's creation.
     */
    @Override
    public int totmsg() {
        return flux_msg;
    }
    
    @Override
	public String toString() {
		String s = new String();
		s+="[";
		for(Message m : this.buffer) {
			s+=" "+m+" ,";
		}
		s+="]";
		return s;
	}

	@Override
	public Message[] get(int k) throws InterruptedException {
		fifoGet.acquire();
		Message[] messages = new Message[k];
		
		for(int i=0; i<k; i++) {
			synchronized(this) {
				while(nb_message_buffer==0) {
					if(maxMess==flux_msg) {
						notifyAll();
						fifoGet.release();
						return messages;
					}
		    		notifyAll();
		    		wait();
		    	}
		    		
		        Message m = buffer[nfull];
		        nfull = (nfull + 1) % (taille_buffer);
		        nb_message_buffer--;
		        notify();
		        messages[i] = m;
			}
		}
		fifoGet.release();
        return messages;
	}

	@Override
	public void setMaxMess(int n) {
		maxMess = n;
	}
	
	@Override
	public void put(Message m, int n) throws InterruptedException {
		put(m);
	}
}
