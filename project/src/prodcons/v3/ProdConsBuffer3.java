package prodcons.v3;

import Main.IProdConsBuffer;
import Main.Message;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer3 implements IProdConsBuffer {
    private Message[] buffer;
    private int taille_buffer, nbWaitingProd, nbWaitingCons, in, out, nb_message_buffer, flux_msg, maxMess;
    private Semaphore waitProd, waitCons, mutex;

    public ProdConsBuffer3(int taille_buffer) {
        this.in = 0;
        this.out = 0;
        this.taille_buffer = taille_buffer;
        this.buffer = new Message[taille_buffer];
        waitProd = new Semaphore(0);
        waitCons = new Semaphore(0);
        mutex = new Semaphore(1);
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
        mutex.acquire(); // protéger les accès aux variables
        if (nb_message_buffer == taille_buffer) { // pour faire patienter les producteurs si le tampon est plein
            nbWaitingProd += 1;
            mutex.release();
            waitProd.acquire();
        }

        // Opérations critiques
        buffer[in] = m;
        in = (in + 1) % taille_buffer;
        nb_message_buffer++;
        flux_msg++;

        if (nbWaitingCons > 0) { // on réveille un consomnateur si il y en a un en attente
            nbWaitingCons -= 1;
            waitCons.release();
        } else {
            mutex.release();
        }
    }

    /**
     * Retrieve a message from the buffer
     * following a FIFO order (if M1 was put berofre M2, M1
     * is retrieved before M2)
     */
    @Override
    public Message get() throws InterruptedException {
        mutex.acquire(); // protéger les accès aux variables
        if (nb_message_buffer == 0) { // faire patienter les consommateurs si le tampon est vide
            if(maxMess==flux_msg) {
            	waitCons.release();
            	mutex.release();
            	return null;
            }
        	nbWaitingCons += 1;
            mutex.release();
            waitCons.acquire();
        }

        // Opérations critiques
        Message m = buffer[out];
        out = (out + 1) % taille_buffer;
        nb_message_buffer--;

        if (nbWaitingProd > 0) { // on réveille un producteur si il y en a un en attente
            nbWaitingProd -= 1;
            waitProd.release();
        } else {
            mutex.release();
        }
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
		// Unimplemented before v5
		return null;
	}

	@Override
	public void setMaxMess(int n) {
		maxMess = n;
	}
}
