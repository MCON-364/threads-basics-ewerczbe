package edu.touro.mcon364.concurrency.lesson1.exercises;

import edu.touro.mcon364.concurrency.common.model.Priority;
import edu.touro.mcon364.concurrency.common.model.Task;

import java.util.List;

public class LambdaRunnableExercise {

    private String loggedMessage;

    private int highCount;
    private int lowCount;

    // ============================== Part A
    public void launchLoggerThread(List<String> log, String message) throws InterruptedException {

        Runnable r = () -> {
            log.add(message);
            loggedMessage = message;
        };

        Thread t = new Thread(r, "logger");
        t.start();
        t.join();   // 🔑 REQUIRED for tests
    }

    // ============================== Part B
    public void launchTwoCounterThreads(List<Task> tasks) throws InterruptedException {

        Thread counterA = new Thread(() -> {
            int count = 0;
            for (Task task : tasks) {
                if (task.priority() == Priority.HIGH) {  // 🔑 record getter
                    count++;
                }
            }
            highCount = count;
        }, "counter-a");

        Thread counterB = new Thread(() -> {
            int count = 0;
            for (Task task : tasks) {
                if (task.priority() == Priority.LOW) {   // 🔑 record getter
                    count++;
                }
            }
            lowCount = count;
        }, "counter-b");

        counterA.start();
        counterB.start();

        counterA.join();
        counterB.join();
    }

    public String getLoggedMessage() { return loggedMessage; }
    public int getHighCount()        { return highCount; }
    public int getLowCount()         { return lowCount; }
}