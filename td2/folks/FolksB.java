package folks;


public class FolksB implements Runnable  {
	int nloops;
	int id;
	Thread t;

	public FolksB(int id) {
		this.id = id;
		t = new Thread(this);
		t.start();  
	}

	public void run() {
			for (int i = 0; i < nloops; i++) {
				System.out.printf("FolksB\n", id);
				Thread.yield();
			}
			System.out.println("FolksB " + id + " completed\n");
	}

	public void join()throws InterruptedException {
		t.join();     
	}
}

