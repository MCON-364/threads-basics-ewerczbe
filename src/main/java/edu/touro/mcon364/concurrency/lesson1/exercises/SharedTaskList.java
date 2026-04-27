package edu.touro.mcon364.concurrency.lesson1.exercises;

import edu.touro.mcon364.concurrency.common.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedTaskList {

    // ✅ thread-safe wrapper
    private final List<Task> tasks = Collections.synchronizedList(new ArrayList<>());

    /** Adds a task to the shared list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the current number of tasks in the list. */
    public int size() {
        return tasks.size();
    }
}