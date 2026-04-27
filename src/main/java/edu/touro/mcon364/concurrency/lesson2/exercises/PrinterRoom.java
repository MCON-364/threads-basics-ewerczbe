package edu.touro.mcon364.concurrency.lesson2.exercises;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class PrinterRoom {

    private final int printerCount;

    // Semaphore controls how many threads can print at once
    private final Semaphore semaphore;

    private final AtomicInteger activeCount   = new AtomicInteger(0);
    private final AtomicInteger maxObserved   = new AtomicInteger(0);
    private final AtomicInteger completedJobs = new AtomicInteger(0);

    public PrinterRoom(int printerCount) {
        this.printerCount = printerCount;
        // Allow exactly printerCount concurrent permits
        this.semaphore = new Semaphore(printerCount);
    }

    public void print(String document) throws InterruptedException {
        // Acquire a printer (block if none available)
        semaphore.acquire();

        try {
            // Increment active jobs
            int current = activeCount.incrementAndGet();

            // Update max observed concurrency
            maxObserved.updateAndGet(prev -> Math.max(prev, current));

            // Simulate printing
            Thread.sleep(50);

            // Mark job as completed
            completedJobs.incrementAndGet();

        } finally {
            // Decrement active jobs FIRST
            activeCount.decrementAndGet();

            // Release printer permit
            semaphore.release();
        }
    }

    public int getActiveCount() { return activeCount.get(); }

    public int getMaxObservedConcurrency() { return maxObserved.get(); }

    public int getCompletedJobs() { return completedJobs.get(); }

    public int getPrinterCount() { return printerCount; }
}