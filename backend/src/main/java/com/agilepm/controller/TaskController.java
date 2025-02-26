package com.agilepm.controller;

import com.agilepm.dto.CommentDTO;
import com.agilepm.dto.TaskDTO;
import com.agilepm.model.Task;
import com.agilepm.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task")
    public ResponseEntity<TaskDTO> createTask(
        @Valid @RequestBody TaskDTO taskDTO
    ) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update task", description = "Update task details")
    public ResponseEntity<TaskDTO> updateTask(
        @PathVariable Long taskId, 
        @Valid @RequestBody TaskDTO taskDTO
    ) {
        TaskDTO updatedTask = taskService.updateTask(taskId, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete task", description = "Delete a task by its ID")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get tasks by project", description = "Retrieve all tasks for a specific project")
    public ResponseEntity<List<TaskDTO>> getTasksByProject(@PathVariable Long projectId) {
        List<TaskDTO> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get tasks by user", description = "Retrieve all tasks assigned to a specific user")
    public ResponseEntity<List<TaskDTO>> getTasksByUser(@PathVariable Long userId) {
        List<TaskDTO> tasks = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tasks by status", description = "Retrieve tasks with a specific status")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(@PathVariable Task.TaskStatus status) {
        List<TaskDTO> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get tasks by priority", description = "Retrieve tasks with a specific priority")
    public ResponseEntity<List<TaskDTO>> getTasksByPriority(@PathVariable Task.TaskPriority priority) {
        List<TaskDTO> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{taskId}/assign")
    @Operation(summary = "Assign task", description = "Assign a task to a user")
    public ResponseEntity<TaskDTO> assignTask(
        @PathVariable Long taskId, 
        @RequestParam Long userId
    ) {
        TaskDTO updatedTask = taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{taskId}/status")
    @Operation(summary = "Update task status", description = "Update the status of a task")
    public ResponseEntity<TaskDTO> updateTaskStatus(
        @PathVariable Long taskId, 
        @RequestParam Task.TaskStatus newStatus
    ) {
        TaskDTO updatedTask = taskService.updateTaskStatus(taskId, newStatus);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/{taskId}/comments")
    @Operation(summary = "Get task comments", description = "Retrieve all comments for a specific task")
    public ResponseEntity<List<CommentDTO>> getTaskComments(@PathVariable Long taskId) {
        List<CommentDTO> comments = taskService.getTaskComments(taskId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{taskId}/comments")
    @Operation(summary = "Add task comment", description = "Add a comment to a task")
    public ResponseEntity<CommentDTO> addTaskComment(
        @PathVariable Long taskId, 
        @RequestParam Long userId, 
        @RequestParam String message
    ) {
        CommentDTO comment = taskService.addCommentToTask(taskId, userId, message);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
}
