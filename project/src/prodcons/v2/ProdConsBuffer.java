package prodcons.v2;

import Main.IProdConsBuffer;
import Main.Message;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {
    Message[] buffer;
    int taille_buffer;
    Semaphore notFull;
    Semaphore notEmpty;
    Semaphore mutex;
    int nempty;
    int nfull;
    int nb_message_buffer;
    int flux_msg;
    Semaphore fifo = new Semaphore(1);

    public ProdConsBuffer(int taille_buffer) {
        this.nempty = 0;
        this.nfull = 0;
        this.taille_buffer = taille_buffer;
        this.buffer = new Message[taille_buffer];
        notFull = new Semaphore(taille_buffer);
        notEmpty = new Semaphore(0);
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
        notFull.acquire();
        mutex.acquire();
        buffer[nempty] = m;
        nempty = nempty + 1 % taille_buffer;
        flux_msg++;
        nb_message_buffer++;
        mutex.release();
        notEmpty.release();
    }

    /**
     * Retrieve a message from the buffer
     * following a FIFO order (if M1 was put berofre M2, M1
     * is retrieved before M2)
     */
    @Override
    public Message get() throws InterruptedException {
        notEmpty.acquire();
        mutex.acquire();
        Message m = buffer[nfull];
        nfull = nfull + 1 % taille_buffer;
        nb_message_buffer--;
        mutex.release();
        notFull.release();
        return m;
    }

    /**
     * Returns the number of messages currently available in
     * the buffer
     */
    @Override
    public int nmsg() {
        return 0;
    }

    /**
     * Returns the total number of messages that have been put
     * in the buffer since it's creation.
     */
    @Override
    public int totmsg() {
        return 0;
    }
}
