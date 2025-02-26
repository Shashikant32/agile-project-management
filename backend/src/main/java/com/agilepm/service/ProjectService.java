package com.agilepm.service;

import com.agilepm.dto.ProjectDTO;
import com.agilepm.dto.TaskDTO;
import com.agilepm.model.Project;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO);
    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);
    void deleteProject(Long projectId);
    ProjectDTO getProjectById(Long projectId);
    List<ProjectDTO> getAllProjects();
    List<ProjectDTO> getProjectsByCompany(Long companyId);
    List<TaskDTO> getProjectTasks(Long projectId);
    ProjectDTO assignProjectManager(Long projectId, Long userId);
    void addProjectMember(Long projectId, Long userId);
    void removeProjectMember(Long projectId, Long userId);
}
