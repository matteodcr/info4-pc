public interface IProdConsBuffer {
    /**
     * Put the message m in the buffer
     */
    public void put(Message m) throws InterruptedException;
}
