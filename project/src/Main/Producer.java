package Main;

import java.util.Random;

public class Producer extends Thread {
	
	int prodTime, minProd, maxProd;
	IProdConsBuffer buff;
	
	public Producer(IProdConsBuffer buff, int prodTime, int minProd, int maxProd) {
		this.prodTime = prodTime;
		this.buff = buff;
		this.minProd = minProd;
		this.maxProd = maxProd;
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		int nMess = rand.nextInt(minProd, maxProd);
		for(int i=0; i<nMess; i++) {
			try {
				sleep(prodTime);
				buff.put(new Message(i, this.getId()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
