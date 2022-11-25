package folks;

public class Starter {

	public static void main(String args[]) throws InterruptedException {
		FolksA folksA[];
		FolksB folksB[];
		
		if (args.length != 1) {
				System.out.println("Usage: Starter <nb bavards>");
				return;
		}

		int nbfolks = new Integer(args[0]).intValue();

		folksA = new FolksA[nbfolks];
		folksB = new FolksB[nbfolks];

		for (int i = 0; i < nbfolks; i++){
			folksA[i] = new FolksA(i);
			folksB[i] = new FolksB(i+nbfolks);
		}

		for (int i = 0; i < nbfolks; i++){
			folksA[i].join();
			folksB[i].join();
		}
		System.out.println("That's all folks");
	}

	public static void print(String threadType, int threadId) {
		System.out.println("thread:" + threadType + 
				" id:" + threadId + "\n");
	}
}

