import static java.lang.Thread.sleep;

public class Allocator {
    public int nfree;

    public Allocator(int ressources) {
        this.nfree = ressources;
    }

    public static void main(String[] args) throws InterruptedException {
        int nb_threads = 4;
        Allocator alloc = new Allocator(5);
        Thread[] threads = new Thread[nb_threads];
        int i;
        for (i = 0; i < nb_threads; i++) {
            threads[i] = new Thread((new Runnable() {
                public void run() {
                    try {
                        alloc.get(3);
                        sleep(2000);
                        alloc.put(3);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
            threads[i].start();
        }
        for (i = 0; i < nb_threads; i++) {
            threads[i].join();
        }
        System.out.println("Fin !");
    }

    public synchronized void get(int n) throws InterruptedException {
        while (nfree < 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        nfree = nfree - n;
        notify();
    }

    public synchronized void put(int n) {
        nfree = nfree + n;
        notify();
    }
}