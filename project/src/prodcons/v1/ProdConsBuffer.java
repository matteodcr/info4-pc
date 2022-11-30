package prodcons.v1;

import Main.IProdConsBuffer;
import Main.Message;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {
    int nempty;
    int nfull;
    int nb_message_buffer;
    int taille_buffer;
    int flux_msg;
    Semaphore fifo = new Semaphore(1);
    Message[] buffer;

    public ProdConsBuffer(int taille_buffer) {
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
     * following a FIFO order (if M1 was put berofre M2, M1
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
}
