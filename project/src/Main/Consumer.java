package Main;

public class Consumer extends Thread{
	
	private int consTime;
	private IProdConsBuffer buff;
	private boolean ended = false;
	
	public Consumer(IProdConsBuffer buff, int consTime) {
		this.consTime = consTime;
		this.buff = buff;
	}
	
	@Override
	public void run() {
		while(!ended) {
			try {
				Message m = buff.get();
				if(m!=null)
					System.out.println(m);
				sleep(consTime);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void end() {
		ended = true;
	}
	
}
