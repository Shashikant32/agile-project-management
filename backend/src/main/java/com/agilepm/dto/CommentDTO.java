package com.agilepm.dto;

import com.agilepm.model.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private Long taskId;
    private Long userId;
    private String userName;

    @NotBlank(message = "Comment message is required")
    @Size(max = 1000, message = "Comment must be less than 1000 characters")
    private String message;

    private LocalDateTime timestamp;

    // Constructors
    public CommentDTO() {}

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.taskId = comment.getTask() != null ? comment.getTask().getId() : null;
        this.userId = comment.getUser() != null ? comment.getUser().getId() : null;
        this.userName = comment.getUser() != null ? comment.getUser().getName() : null;
        this.message = comment.getMessage();
        this.timestamp = comment.getTimestamp();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
