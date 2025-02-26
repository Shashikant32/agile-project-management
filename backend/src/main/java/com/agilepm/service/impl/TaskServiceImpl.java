package com.agilepm.service.impl;

import com.agilepm.dto.CommentDTO;
import com.agilepm.dto.TaskDTO;
import com.agilepm.model.Comment;
import com.agilepm.model.Project;
import com.agilepm.model.Task;
import com.agilepm.model.User;
import com.agilepm.repository.CommentRepository;
import com.agilepm.repository.ProjectRepository;
import com.agilepm.repository.TaskRepository;
import com.agilepm.repository.UserRepository;
import com.agilepm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public TaskServiceImpl(
        TaskRepository taskRepository,
        ProjectRepository projectRepository,
        UserRepository userRepository,
        CommentRepository commentRepository
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        // Validate project
        Project project = projectRepository.findById(taskDTO.getProjectId())
            .orElseThrow(() -> new RuntimeException("Project not found"));

        // Create task with optional user assignment
        Task task = new Task(
            taskDTO.getTitle(),
            taskDTO.getDescription(),
            project,
            null, // Initially no user assigned
            taskDTO.getStatus(),
            taskDTO.getPriority(),
            taskDTO.getDueDate()
        );

        // Assign user if provided
        if (taskDTO.getAssignedToId() != null) {
            User assignedUser = userRepository.findById(taskDTO.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            task.setAssignedTo(assignedUser);
        }

        Task savedTask = taskRepository.save(task);
        return new TaskDTO(savedTask);
    }

    @Override
    @Transactional
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        // Update basic task details
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setPriority(taskDTO.getPriority());
        existingTask.setDueDate(taskDTO.getDueDate());

        // Update project if provided
        if (taskDTO.getProjectId() != null) {
            Project project = projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
            existingTask.setProject(project);
        }

        // Update assigned user if provided
        if (taskDTO.getAssignedToId() != null) {
            User assignedUser = userRepository.findById(taskDTO.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            existingTask.setAssignedTo(assignedUser);
        }

        Task updatedTask = taskRepository.save(existingTask);
        return new TaskDTO(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        return new TaskDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(TaskDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByProject(Long projectId) {
        return taskRepository.findByProject_Id(projectId).stream()
            .map(TaskDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByUser(Long userId) {
        return taskRepository.findByAssignedTo_Id(userId).stream()
            .map(TaskDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
            .map(TaskDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByPriority(Task.TaskPriority priority) {
        return taskRepository.findByPriority(priority).stream()
            .map(TaskDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskDTO assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        User assignedUser = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignedTo(assignedUser);
        Task updatedTask = taskRepository.save(task);
        return new TaskDTO(updatedTask);
    }

    @Override
    @Transactional
    public TaskDTO updateTaskStatus(Long taskId, Task.TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(newStatus);
        Task updatedTask = taskRepository.save(task);
        return new TaskDTO(updatedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getTaskComments(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        return task.getComments().stream()
            .map(CommentDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO addCommentToTask(Long taskId, Long userId, String message) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment(task, user, message);
        Comment savedComment = commentRepository.save(comment);
        return new CommentDTO(savedComment);
    }
}
