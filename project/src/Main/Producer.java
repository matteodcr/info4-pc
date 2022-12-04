package Main;

import java.util.Random;

public class Producer extends Thread {
	
	private int prodTime;
	public int nMess;
	private IProdConsBuffer buff;
	
	public Producer(IProdConsBuffer buff, int prodTime, int minProd, int maxProd) {
		this.prodTime = prodTime;
		this.buff = buff;
		Random rand = new Random();
		nMess = rand.nextInt(minProd, maxProd);
	}
	
	@Override
	public void run() {
		for(int i=0; i<nMess; i++) {
			try {
				sleep(prodTime);
				buff.put(new Message(i, this.getId()));
			} catch (InterruptedException e) {
			}
		}
	}
	
}
