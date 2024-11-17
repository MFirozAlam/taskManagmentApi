package com.example.taskmanagement;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskmanagementApplicationTests {

	@Autowired
	private TaskService taskService;

	@Test
	void contextLoads() {
		// This test checks if the application context loads successfully
		assertNotNull(taskService);
	}

	@Test
	void testCreateTask() {
		// Test case to verify task creation

		// Arrange: Create a new task
		Task newTask = new Task();
		newTask.setTitle("Test Task");
		newTask.setDescription("This is a test task");
		newTask.setDueDate(LocalDateTime.now().plusDays(2));

		// Act: Save the new task
		Task createdTask = taskService.createTask(newTask);

		// Assert: Verify that the task was created successfully
		assertNotNull(createdTask);
		assertNotNull(createdTask.getId());
		assertEquals("Test Task", createdTask.getTitle());
		assertEquals("This is a test task", createdTask.getDescription());
		assertEquals(Task.Status.PENDING, createdTask.getStatus());
	}

	@Test
	void testGetTaskById() {
		// Test case to verify retrieval of a task by ID

		// Arrange: Create and save a new task
		Task newTask = new Task();
		newTask.setTitle("Retrieve Task");
		newTask.setDescription("This task is for retrieval testing");
		newTask.setDueDate(LocalDateTime.now().plusDays(3));
		Task createdTask = taskService.createTask(newTask);

		// Act: Retrieve the task by ID
		Optional<Task> retrievedTask = taskService.getTaskById(createdTask.getId());

		// Assert: Check that the task is retrieved correctly
		assertTrue(retrievedTask.isPresent());
		assertEquals(createdTask.getId(), retrievedTask.get().getId());
		assertEquals("Retrieve Task", retrievedTask.get().getTitle());
	}
}
