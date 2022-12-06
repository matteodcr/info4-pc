package src.prodcons.v6;

import Main.Message;

import java.util.ArrayList;
import java.util.List;

public class Messages {
    Message[] messages;
    private List<Thread> blacklist = new ArrayList<Thread>();

    public Messages(int n, Message m) {
        this.messages = new Message[n];
        this.blacklist = blacklist;
        for (int i = 0; i < n; i++) {
            messages[i] = m;
        }
    }

    public Messages(int n, Messages m) {
        this.messages = new Message[n];
        this.blacklist = blacklist;
        this.messages = messages;
    }

    void addThreadBlacklist(Thread t) {
        if (!blacklist.contains(t)) blacklist.add(t);
    }

    void clearBlacklist() {
        blacklist.clear();
    }
}