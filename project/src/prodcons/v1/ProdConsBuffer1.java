package prodcons.v1;

import Main.IProdConsBuffer;
import Main.Message;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer1 implements IProdConsBuffer {
    private int nempty, nfull, nb_message_buffer, taille_buffer, flux_msg;
    private Semaphore fifo = new Semaphore(1);
    private Message[] buffer;

    public ProdConsBuffer1(int taille_buffer) {
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
        fifo.acquire();
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
        }
        fifo.release();
    }

    /**
     * Retrieve a message from the buffer
     * following a FIFO order (if M1 was put before M2, M1
     * is retrieved before M2)
     */
    @Override
    public synchronized Message get() throws InterruptedException {
    	if(nb_message_buffer==0) {
    		notify();
    		return null;
    	}
    		
        Message m = buffer[nfull];
        nfull = (nfull + 1) % (taille_buffer);
        nb_message_buffer--;
        notify();
        return m;
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
		Message[] array = new Message[k];
		array[0]=get();
		return array;
	}

	@Override
	public void setMaxMess(int n) {
		// Unimplemented before v3
	}

	@Override
	public void put(Message m, int n) throws InterruptedException {
		put(m);
	}
}
