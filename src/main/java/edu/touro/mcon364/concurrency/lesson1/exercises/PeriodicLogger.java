package edu.touro.mcon364.concurrency.lesson1.exercises;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeriodicLogger {

    private final int ticks;
    private final long intervalMs;
    private final List<String> log = new ArrayList<>();
    private Thread worker;

    public PeriodicLogger(int ticks, long intervalMs) {
        if (ticks <= 0) throw new IllegalArgumentException("ticks must be positive");
        if (intervalMs < 0) throw new IllegalArgumentException("intervalMs must be non-negative");
        this.ticks = ticks;
        this.intervalMs = intervalMs;
    }

    /**
     * Create, configure, and start the background thread.
     * Must return before the thread finishes (non-blocking).
     */
    public void start() {

        worker = new Thread(() -> {
            for (int i = 1; i <= ticks; i++) {
                try {
                    Thread.sleep(intervalMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                log.add("tick " + i);
            }
        }, "periodic-logger");

        worker.setDaemon(true); // must be before start()
        worker.start();
    }

    /**
     * Returns true if the background thread is still running.
     */
    public boolean isRunning() {
        return worker != null && worker.isAlive();
    }

    /**
     * Blocks until the background thread finishes.
     */
    public void awaitCompletion() throws InterruptedException {
        if (worker != null) {
            worker.join();
        }
    }

    /** Returns the log messages collected so far. */
    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }
}