package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.Task.Status;
import com.example.taskmanagement.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskServiceImpl implements TaskService {

    // In-memory data store using a List for simplicity
    private final List<Task> taskList = new ArrayList<>();

    // Atomic counter to simulate auto-incremented IDs
    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public List<Task> getAllTasks() {
        // Return a copy of the task list to avoid modification of the original list
        return new ArrayList<>(taskList);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        // Find a task by its ID
        return taskList.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    @Override
    public Task createTask(Task task) {
        // Set task ID and timestamps
        task.setId(counter.getAndIncrement());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setStatus(Status.PENDING); // Default status

        // Add the new task to the list
        taskList.add(task);
        return task;
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {
        // Find the existing task by ID
        Optional<Task> existingTaskOpt = getTaskById(id);

        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();

            // Update the task's fields
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setDueDate(updatedTask.getDueDate());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setUpdatedAt(LocalDateTime.now());

            return existingTask;
        } else {
            // Throw custom exception if the task is not found
            throw new TaskNotFoundException(id);
        }
    }

    @Override
    public void deleteTask(Long id) {
        // Remove the task from the list if it exists
        boolean removed = taskList.removeIf(task -> task.getId().equals(id));

        if (!removed) {
            // Throw custom exception if the task to delete is not found
            throw new TaskNotFoundException(id);
        }
    }

    @Override
    public Task markTaskAsComplete(Long id) {
        // Find the existing task by ID
        Optional<Task> existingTaskOpt = getTaskById(id);

        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();

            // Mark task as completed
            existingTask.setStatus(Status.COMPLETED);
            existingTask.setUpdatedAt(LocalDateTime.now());

            return existingTask;
        } else {
            // Throw custom exception if the task is not found
            throw new TaskNotFoundException(id);
        }
    }
}
