package prodcons.v2;

import java.util.Properties;

import Main.Consumer;
import Main.IProdConsBuffer;
import Main.Producer;

public class ProdConsSync extends Thread{
	
	private int nProd = 15, nCons = 10, prodTime = 10, consTime = 10, minProd = 5, maxProd = 10;
	private IProdConsBuffer buff;
	
	private Producer[] producers;
	private Consumer[] consumers;
	
	public ProdConsSync(Properties properties, IProdConsBuffer buff) {
		nProd = Integer.parseInt(properties.getProperty("nProd"));
        nCons = Integer.parseInt(properties.getProperty("nCons"));
    	prodTime = Integer.parseInt(properties.getProperty("prodTime"));
    	consTime = Integer.parseInt(properties.getProperty("consTime"));
    	minProd = Integer.parseInt(properties.getProperty("minProd"));
    	maxProd = Integer.parseInt(properties.getProperty("maxProd"));
    	this.buff = buff;
	}
	
	public ProdConsSync(int nProd, int nCons, int prodTime, int consTime, int minProd, int maxProd, IProdConsBuffer buff) {
		this.nProd = nProd;
		this.nCons = nCons;
		this.prodTime = prodTime;
		this.consTime = consTime;
		this.minProd = minProd;
    	this.maxProd = maxProd;
    	this.buff = buff;
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		producers = new Producer[nProd];
		consumers = new Consumer[nCons];
		int nTotalMessToProduce = 0;
		for(int i=0; i<nProd; i++) {
			producers[i]=new Producer(buff, prodTime, minProd, maxProd);
			nTotalMessToProduce+= producers[i].nMess;
		}
		for(int i=0; i<nCons; i++)
			consumers[i]=new Consumer(buff, consTime);
		
		for(int i=0; i<nProd; i++)
			producers[i].start();
		for(int i=0; i<nCons; i++)
			consumers[i].start();
		
		while(buff.totmsg()<nTotalMessToProduce) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(int i=0; i<nCons; i++)
			consumers[i].end();
		
	}
	
	

}
