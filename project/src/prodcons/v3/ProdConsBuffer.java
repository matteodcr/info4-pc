package prodcons.v3;

import Main.IProdConsBuffer;
import Main.Message;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {
    Message[] buffer;
    int taille_buffer;
    Semaphore waitProd;
    int nbWaitingProd;
    Semaphore waitCons;
    int nbWaitingCons;
    Semaphore mutex;
    int in;
    int out;
    int nb_message_buffer;
    int flux_msg;
    Semaphore fifo = new Semaphore(1);

    public ProdConsBuffer(int taille_buffer) {
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
        mutex.release();
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
