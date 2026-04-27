package edu.touro.mcon364.concurrency.lesson2.exercises;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTaskCounter {

    // Thread-safe counter
    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * Atomically increments the counter by one.
     */
    public void increment() {
        count.incrementAndGet();
    }

    /**
     * Atomically decrements the counter by one.
     */
    public void decrement() {
        count.decrementAndGet();
    }

    /** Returns the current counter value. */
    public int getCount() {
        return count.get();
    }

    /** Resets the counter to zero. */
    public void reset() {
        count.set(0);
    }
}