package ru.alfabank.dmpr.statistic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BatchBlockingQueue<T> {

    private ArrayList<T> queue;
    private Semaphore readerLock;
    private Semaphore writerLock;
    private int batchSize;

    public BatchBlockingQueue(int batchSize) {
        this.queue = new ArrayList<>(batchSize);
        this.readerLock = new Semaphore(0);
        this.writerLock = new Semaphore(batchSize);
        this.batchSize = batchSize;
    }

    public synchronized void put(T e) throws InterruptedException {
        writerLock.acquire();
        queue.add(e);
        if (queue.size() == batchSize) {
            readerLock.release();
        }
    }

    public List<T> poll() throws InterruptedException {
        readerLock.acquire();

        List<T> result = queue;
        queue = new ArrayList<>();

        writerLock.release(batchSize);

        return result;
    }

}