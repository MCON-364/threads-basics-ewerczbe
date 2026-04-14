package edu.touro.mcon364.concurrency.lesson1.exercises;

public class ThreadSafeTaskCounter {

    private int count;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}