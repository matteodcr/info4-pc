package prodcons.v5;

import java.util.concurrent.Semaphore;

import Main.IProdConsBuffer;
import Main.Message;

public class ProdConsBuffer5 implements IProdConsBuffer{

	int nempty;
    int nfull;
    int nb_message_buffer;
    int taille_buffer;
    int flux_msg;
    int nbTotalMessToProduce;
    Semaphore fifo = new Semaphore(1);
    Semaphore global = new Semaphore(1);
    Message[] buffer;

    public ProdConsBuffer5(int taille_buffer) {
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
    public synchronized void put(Message m) throws InterruptedException {
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

    /**
     * Retrieve a message from the buffer
     * following a FIFO order (if M1 was put before M2, M1
     * is retrieved before M2)
     */
    @Override
    public synchronized Message get() throws InterruptedException {
    	Message[] res = get(1);
    	return res[0];
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
	public Message[] get(int k) throws InterruptedException {
		global.acquire();
		Message[] array = new Message[k];
		synchronized(this) {
			for(int i=0; i<k; i++) {
				while(nb_message_buffer==0) {
					if(nbTotalMessToProduce!=flux_msg) {
						notifyAll();
						global.release();
						return array;
					}
		    		wait();
		    	}
		    		
		        Message m = buffer[nfull];
		        nfull = (nfull + 1) % (taille_buffer);
		        nb_message_buffer--;
		        array[i]=m;
			}
		}
		
		notifyAll();
		global.release();
		return array;
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
	public void setMaxMess(int n) {
		// Unimplemented before v
	}
}
