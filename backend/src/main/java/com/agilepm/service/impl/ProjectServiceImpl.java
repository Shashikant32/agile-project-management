package com.agilepm.service.impl;

import com.agilepm.dto.ProjectDTO;
import com.agilepm.dto.TaskDTO;
import com.agilepm.model.Company;
import com.agilepm.model.Project;
import com.agilepm.model.User;
import com.agilepm.repository.CompanyRepository;
import com.agilepm.repository.ProjectRepository;
import com.agilepm.repository.UserRepository;
import com.agilepm.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        CompanyRepository companyRepository,
        UserRepository userRepository
    ) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        // Validate company
        Company company = companyRepository.findById(projectDTO.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found"));

        // Validate project manager
        User projectManager = userRepository.findById(projectDTO.getProjectManagerId())
            .orElseThrow(() -> new RuntimeException("Project Manager not found"));

        // Create new project
        Project project = new Project(
            projectDTO.getName(), 
            projectDTO.getDescription(), 
            company, 
            projectManager, 
            projectDTO.getWorkflow()
        );

        Project savedProject = projectRepository.save(project);
        return new ProjectDTO(savedProject);
    }

    @Override
    @Transactional
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        Project existingProject = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

        // Update basic project details
        existingProject.setName(projectDTO.getName());
        existingProject.setDescription(projectDTO.getDescription());
        existingProject.setWorkflow(projectDTO.getWorkflow());

        // Update company if provided
        if (projectDTO.getCompanyId() != null) {
            Company company = companyRepository.findById(projectDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
            existingProject.setCompany(company);
        }

        // Update project manager if provided
        if (projectDTO.getProjectManagerId() != null) {
            User projectManager = userRepository.findById(projectDTO.getProjectManagerId())
                .orElseThrow(() -> new RuntimeException("Project Manager not found"));
            existingProject.setProjectManager(projectManager);
        }

        Project updatedProject = projectRepository.save(existingProject);
        return new ProjectDTO(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
        
        projectRepository.delete(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
        
        return new ProjectDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
            .map(ProjectDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByCompany(Long companyId) {
        return projectRepository.findByCompany_Id(companyId).stream()
            .map(ProjectDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getProjectTasks(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
        
        return project.getTasks().stream()
            .map(TaskDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectDTO assignProjectManager(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

        User projectManager = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        project.setProjectManager(projectManager);
        Project updatedProject = projectRepository.save(project);
        return new ProjectDTO(updatedProject);
    }

    @Override
    @Transactional
    public void addProjectMember(Long projectId, Long userId) {
        // Note: This method might need more complex implementation 
        // depending on how you want to track project members
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public void removeProjectMember(Long projectId, Long userId) {
        // Note: This method might need more complex implementation 
        // depending on how you want to track project members
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
