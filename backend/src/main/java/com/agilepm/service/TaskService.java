package com.agilepm.service;

import com.agilepm.dto.CommentDTO;
import com.agilepm.dto.TaskDTO;
import com.agilepm.model.Task;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);
    void deleteTask(Long taskId);
    TaskDTO getTaskById(Long taskId);
    List<TaskDTO> getAllTasks();
    List<TaskDTO> getTasksByProject(Long projectId);
    List<TaskDTO> getTasksByUser(Long userId);
    List<TaskDTO> getTasksByStatus(Task.TaskStatus status);
    List<TaskDTO> getTasksByPriority(Task.TaskPriority priority);
    TaskDTO assignTask(Long taskId, Long userId);
    TaskDTO updateTaskStatus(Long taskId, Task.TaskStatus newStatus);
    List<CommentDTO> getTaskComments(Long taskId);
    CommentDTO addCommentToTask(Long taskId, Long userId, String message);
}
