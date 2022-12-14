package Main;

import java.util.Random;

public class Producer extends Thread {
	
	private int prodTime, multiProd;
	public int nMess;
	private IProdConsBuffer buff;
	
	public Producer(IProdConsBuffer buff, int prodTime, int minProd, int maxProd, int multiProd) {
		this.prodTime = prodTime;
		this.multiProd = multiProd;
		this.buff = buff;
		Random rand = new Random();
		nMess = rand.nextInt(minProd, maxProd);
	}
	
	@Override
	public void run() {
		for(int i=0; i<nMess; i++) {
			try {
				sleep(prodTime);
				buff.put(new Message(i, this.getId()), multiProd);
			} catch (InterruptedException e) {
			}
		}
	}
	
}
