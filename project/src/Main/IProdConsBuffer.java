package Main;

public interface IProdConsBuffer {
    /**
     * Put the message m in the buffer
     */
    public void put(Message m) throws InterruptedException;

    /**
     * Retrieve a message from the buffer
     * following a FIFO order (if M1 was put berofre M2, M1
     * is retrieved before M2)
     */
    public Message get() throws InterruptedException;

    /**
     * Returns the number of messages currently available in
     * the buffer
     */
    public int nmsg();

    /**
     * Returns the total number of messages that have been put
     * in the buffer since it's creation.
     */
    public int totmsg();
    
    public void setMaxMess(int n);
    
    public Message[] get(int k) throws InterruptedException;
}
