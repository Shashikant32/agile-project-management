package com.agilepm.repository;

import com.agilepm.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCompany_Id(Long companyId);
    List<Project> findByProjectManager_Id(Long managerId);
}
