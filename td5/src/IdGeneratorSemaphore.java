import java.util.concurrent.Semaphore;

public class IdGeneratorSemaphore {
    static Thread[] tabThread = new Thread[10];
    public static void main(String[] args) {
        Parking prk = new Parking(1);
        for (int i=0; i<10; i++){
            tabThread[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++){
                    try {
                        prk.enter();
                        AllocID.get();
                        prk.leave();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            })
            ;tabThread[i].start();}

        for (int i=0; i<10; i++) {
            try{
                tabThread[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(AllocID.lastId + " = 10000 ?");
    }

    static class AllocID {
        static int lastId = 0;

        public static int get(){
            return lastId++;
        }
    }

    public static class Parking {
        Semaphore park;

        Parking(int nplaces){
            park = new Semaphore(nplaces);
        }

        void enter() throws InterruptedException {
            park.acquire();
        }

        void leave() throws InterruptedException {
            park.release();
        }
    }
}
