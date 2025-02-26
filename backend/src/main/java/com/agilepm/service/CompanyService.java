package com.agilepm.service;

import com.agilepm.dto.ProjectDTO;
import com.agilepm.dto.UserDTO;
import com.agilepm.model.Company;

import java.util.List;

public interface CompanyService {
    Company createCompany(String name, Long createdById);
    Company updateCompany(Long companyId, String newName);
    void deleteCompany(Long companyId);
    Company getCompanyById(Long companyId);
    List<Company> getAllCompanies();
    List<UserDTO> getCompanyUsers(Long companyId);
    List<ProjectDTO> getCompanyProjects(Long companyId);
    void addUserToCompany(Long companyId, Long userId);
    void removeUserFromCompany(Long companyId, Long userId);
}
