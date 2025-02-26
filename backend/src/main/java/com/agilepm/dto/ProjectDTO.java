package com.agilepm.dto;

import com.agilepm.model.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ProjectDTO {
    private Long id;

    @NotBlank(message = "Project name is required")
    @Size(max = 100, message = "Project name must be less than 100 characters")
    private String name;

    @Size(max = 500, message = "Project description must be less than 500 characters")
    private String description;

    private Long companyId;
    private Long projectManagerId;
    private LocalDateTime createdAt;
    private Project.ProjectWorkflow workflow;

    // Constructors
    public ProjectDTO() {}

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.companyId = project.getCompany() != null ? project.getCompany().getId() : null;
        this.projectManagerId = project.getProjectManager() != null ? project.getProjectManager().getId() : null;
        this.createdAt = project.getCreatedAt();
        this.workflow = project.getWorkflow();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Project.ProjectWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Project.ProjectWorkflow workflow) {
        this.workflow = workflow;
    }
}
