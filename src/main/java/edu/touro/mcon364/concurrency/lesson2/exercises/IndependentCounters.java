package edu.touro.mcon364.concurrency.lesson2.exercises;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IndependentCounters {

    private int readCount  = 0;
    private int writeCount = 0;

    // Separate locks for independent data
    private final Lock readLock  = new ReentrantLock();
    private final Lock writeLock = new ReentrantLock();

    /**
     * Record a read operation.
     */
    public void read() {
        readLock.lock();
        try {
            readCount++;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Record a write operation.
     */
    public void write() {
        writeLock.lock();
        try {
            writeCount++;
        } finally {
            writeLock.unlock();
        }
    }

    public int getReadCount()  { return readCount; }
    public int getWriteCount() { return writeCount; }
}