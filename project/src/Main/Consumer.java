package Main;

public class Consumer extends Thread{
	
	private int consTime, multiCons;
	private IProdConsBuffer buff;
	private boolean ended = false;
	
	public Consumer(IProdConsBuffer buff, int consTime, int multiCons) {
		this.consTime = consTime;
		this.buff = buff;
		this.multiCons = multiCons;
	}
	
	@Override
	public void run() {
		while(!ended) {
			try {
				Message[] m = buff.get(multiCons);
				boolean empty = true;
				if(m!=null)
					for(Message mess : m)
						if(mess!=null)
							empty=false;
				if(!empty) {
					String s = new String();
					s+="[";
					for(int i=0; i<multiCons-1; i++)
						s+=m[i]+",";
					s+=m[multiCons-1]+"]";
					System.out.println(s);
				}
				sleep(consTime);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void end() {
		ended = true;
	}
	
}
