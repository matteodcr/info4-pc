package folks;

public  class FolksA extends Thread {
	int nloops;
	int id;

	public FolksA(int id) {
		this.id = id;
		this.start();
	}
	public void run() {
		System.out.printf("FolksA\n", id);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		System.out.println("FolksA " + id + " completed\n");
	}
}
