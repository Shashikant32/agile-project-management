package com.agilepm.controller;

import com.agilepm.dto.ProjectDTO;
import com.agilepm.dto.TaskDTO;
import com.agilepm.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project Management", description = "Endpoints for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Create a new project")
    public ResponseEntity<ProjectDTO> createProject(
        @Valid @RequestBody ProjectDTO projectDTO
    ) {
        ProjectDTO createdProject = projectService.createProject(projectDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project by ID", description = "Retrieve a specific project by its ID")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long projectId) {
        ProjectDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Update project", description = "Update project details")
    public ResponseEntity<ProjectDTO> updateProject(
        @PathVariable Long projectId, 
        @Valid @RequestBody ProjectDTO projectDTO
    ) {
        ProjectDTO updatedProject = projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Delete project", description = "Delete a project by its ID")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get projects by company", description = "Retrieve all projects for a specific company")
    public ResponseEntity<List<ProjectDTO>> getProjectsByCompany(@PathVariable Long companyId) {
        List<ProjectDTO> projects = projectService.getProjectsByCompany(companyId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}/tasks")
    @Operation(summary = "Get project tasks", description = "Retrieve all tasks for a specific project")
    public ResponseEntity<List<TaskDTO>> getProjectTasks(@PathVariable Long projectId) {
        List<TaskDTO> tasks = projectService.getProjectTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{projectId}/manager")
    @Operation(summary = "Assign project manager", description = "Assign a user as the project manager")
    public ResponseEntity<ProjectDTO> assignProjectManager(
        @PathVariable Long projectId, 
        @RequestParam Long userId
    ) {
        ProjectDTO updatedProject = projectService.assignProjectManager(projectId, userId);
        return ResponseEntity.ok(updatedProject);
    }

    @PostMapping("/{projectId}/members")
    @Operation(summary = "Add project member", description = "Add a user to the project")
    public ResponseEntity<Void> addProjectMember(
        @PathVariable Long projectId, 
        @RequestParam Long userId
    ) {
        projectService.addProjectMember(projectId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    @Operation(summary = "Remove project member", description = "Remove a user from the project")
    public ResponseEntity<Void> removeProjectMember(
        @PathVariable Long projectId, 
        @PathVariable Long userId
    ) {
        projectService.removeProjectMember(projectId, userId);
        return ResponseEntity.noContent().build();
    }
}
