package com.agilepm.service.impl;

import com.agilepm.dto.ProjectDTO;
import com.agilepm.dto.UserDTO;
import com.agilepm.model.Company;
import com.agilepm.model.User;
import com.agilepm.repository.CompanyRepository;
import com.agilepm.repository.UserRepository;
import com.agilepm.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Autowired
    public CompanyServiceImpl(
        CompanyRepository companyRepository, 
        UserRepository userRepository
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Company createCompany(String name, Long createdById) {
        User createdBy = userRepository.findById(createdById)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if company with the same name already exists
        if (companyRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Company with this name already exists");
        }

        Company company = new Company(name, createdBy);
        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public Company updateCompany(Long companyId, String newName) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(newName);
        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public void deleteCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        companyRepository.delete(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getCompanyUsers(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        return company.getUsers().stream()
            .map(UserDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getCompanyProjects(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        return company.getProjects().stream()
            .map(ProjectDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addUserToCompany(Long companyId, Long userId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        company.addUser(user);
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void removeUserFromCompany(Long companyId, Long userId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        company.removeUser(user);
        companyRepository.save(company);
    }
}
