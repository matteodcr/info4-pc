public class IdGenerator {
    static Thread[] tabThread = new Thread[10];
    public static void main( String[] args) {
        for (int i=0; i<10; i++){
            tabThread[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++){
                    AllocID.get();
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

        public static synchronized int get(){
            return lastId++;
        }
    }
}
