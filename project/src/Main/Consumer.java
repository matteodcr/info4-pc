package Main;

public class Consumer extends Thread{
	
	int consTime;
	IProdConsBuffer buff;
	
	public Consumer(IProdConsBuffer buff, int consTime) {
		this.consTime = consTime;
		this.buff = buff;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Message m = buff.get();
				if(m!=null)
					System.out.println(m);
				sleep(consTime);
			} catch (InterruptedException e) {
			}
		}
	}
	
}
