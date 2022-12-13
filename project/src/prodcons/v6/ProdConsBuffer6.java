package prodcons.v6;

import Main.IProdConsBuffer;
import Main.Message;

import java.util.concurrent.Semaphore;

import javax.print.attribute.standard.JobName;

public class ProdConsBuffer6 implements IProdConsBuffer {
    private int nb_message_buffer=0, taille_buffer=0, flux_msg=0, maxMess=0;
    private int indexIn=0, indexOut=0;
    private Semaphore fifoPut = new Semaphore(1), fifoGet = new Semaphore(1);
    private Message[] buffer;

    public ProdConsBuffer6(int taille_buffer) {
        this.taille_buffer = taille_buffer;
        this.buffer = new Message[taille_buffer];
    }

    /**
     * Put the message m in the buffer
     *
     * @param m
     */
    @Override
    public void put(Message m) throws InterruptedException {
    	put(m, 1);
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
		Message[] messages = new Message[k];
		fifoGet.acquire();
		for(int i=0; i<k; i++) {
			boolean finalMess = true;
			Messages multimessToWait = null;
			synchronized(this){
				//on attend tant que le buffer est vide
				while(isEmpty()) {
					wait();
				}
				
				//le buffer n'est pas vie donc on en recup un
				Message m;
				//on verifie qu'on a le droit de le recuperer sinon on prend le prochain
				int myIndexOut = indexOut;
				boolean valid = true;
				do {
					valid = true;
					m = buffer[myIndexOut];
					if(m==null)
						valid=false;
					//on verifie qu'on recupere pas un message du meme groupe de message
					for(Message mess : messages)
						if(mess!=null && m!=null)
							if(mess.parent==m.parent)
								valid = false;
					//on passe au prochain message
					if(!valid) {
						myIndexOut= (myIndexOut+1)%taille_buffer;
						//il n'y a pas de message valide pour l'instant donc on attend
						while(myIndexOut==indexIn) {
							wait();
						}
					}
				}while(m==null || !valid);
				//on recup le message valide
				messages[i] = m;
				buffer[myIndexOut]=null;
				//on reveille de potentiels producteurs
				notifyAll();
				
				m.parent.index++;
				if(m.parent.finished()) {
					m.parent.n();
					nb_message_buffer-=m.parent.messages.length;
					indexOut = (indexOut+m.parent.messages.length)%taille_buffer;
				}else {
					multimessToWait = m.parent;
					finalMess = false;
				}
			}
			if(!finalMess) {
				fifoGet.release();
				multimessToWait.w();
				fifoGet.acquire();
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
		fifoPut.acquire();
		Messages mess = new Messages(n,m);
		for(int i=0; i<n; i++) {
			synchronized(this) {
				//si le buffer est plein alors on attend
				while(isFull()) {
					wait();
				}
				
				//le buffer n'est pas plein donc on pose un message au prochain emplacement
				buffer[indexIn]=mess.messages[i];
				indexIn = (indexIn+1)%taille_buffer;
				flux_msg++;
				nb_message_buffer++;
				//On réveille de potentiels consommateurs
				notifyAll();
			}
		}
		fifoPut.release();
		//un producteur qui à dépusé un multi message doit attendre jusqu'à ce que ses messages soient consommés
		mess.w();
	}
	
	public boolean isFull() {
		return nb_message_buffer==taille_buffer;
	}
	
	public boolean isEmpty() {
		return nb_message_buffer==0;
	}
}
