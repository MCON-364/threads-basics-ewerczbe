package edu.touro.mcon364.concurrency.lesson2.homework;

import edu.touro.mcon364.concurrency.common.model.Priority;
import edu.touro.mcon364.concurrency.common.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ExecutorTaskManager {

    /* ── SYNCHRONIZER CHOICE ────────────────────────────────────────────────
     * I would use a CountDownLatch to wait for a batch of tasks to complete.
     * Each task would call countDown() when finished, and the main thread
     * would call await() to block until all tasks in the batch are done.
     * ──────────────────────────────────────────────────────────────────────*/

    private static final int POOL_SIZE = 4;

    // Fixed-size thread pool
    private final ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);

    // Atomic ID generator (starts at 0 → first ID will be 1)
    private final AtomicInteger idCounter = new AtomicInteger(0);

    // Shared list written by multiple threads
    private final List<Task> completedTasks = new ArrayList<>();

    // Lock to protect completedTasks
    private final ReentrantLock lock = new ReentrantLock();

    // ── ID generation ────────────────────────────────────────────────────────

    public int nextId() {
        return idCounter.incrementAndGet();
    }

    // ── task submission ──────────────────────────────────────────────────────

    public Future<Task> submit(String description, Priority priority) {
        int id = nextId();

        Task task = new Task(id, description, priority);

        return pool.submit(() -> {
            // Simulate work
            Thread.sleep(10);

            // Record completion
            recordCompleted(task);

            return task;
        });
    }

    // ── recording completion ─────────────────────────────────────────────────

    private void recordCompleted(Task task) {
        // Multiple worker threads may try to add to the list at the same time.
        // Without a lock, this can corrupt the internal structure of ArrayList
        // (e.g., lost updates or inconsistent state), so we must protect it.
        lock.lock();
        try {
            completedTasks.add(task);
        } finally {
            lock.unlock();
        }
    }

    // ── collecting results ───────────────────────────────────────────────────

    public List<Task> awaitAll(List<Future<Task>> futures) {
        List<Task> results = new ArrayList<>();

        for (Future<Task> f : futures) {
            try {
                results.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return results;
    }

    // ── lifecycle ────────────────────────────────────────────────────────────

    public void shutdown() throws InterruptedException {
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
    }

    // ── observability ────────────────────────────────────────────────────────

    public List<Task> getCompletedTasks() {
        lock.lock();
        try {
            return new ArrayList<>(completedTasks);
        } finally {
            lock.unlock();
        }
    }

    public int getLastIssuedId() {
        return idCounter.get();
    }
}