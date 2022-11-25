import java.util.concurrent.Semaphore;

public class AllocatorFIFO {
    static Thread[] tabThread = new Thread[10];
    static Allocator a = new Allocator(1, 1);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            tabThread[i] = new Thread(() -> {
                try {
                    System.out.println("Started ");
                    a.get();
                    System.out.println("Allocated ");
                    Thread.sleep(3000);
                    a.put();
                    System.out.println("Finished ");

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            })
            ;
            tabThread[i].start();
        }

        for (int i = 0; i < 10; i++) {
            try {
                tabThread[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Allocator {
        Semaphore fifo;
        int cpt;

        public Allocator(int i, int i1) {
            cpt = i;
            fifo = new Semaphore(i1, true);
        }

        public void get() throws InterruptedException {
            fifo.acquire();
            synchronized (this) {
                while (cpt == 0) wait();
                cpt--;
            }
            fifo.release();
        }

        public synchronized void put() {
            cpt++;
            notifyAll();
        }
    }
}
