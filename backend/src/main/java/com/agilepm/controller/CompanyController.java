package com.agilepm.controller;

import com.agilepm.dto.ProjectDTO;
import com.agilepm.dto.UserDTO;
import com.agilepm.model.Company;
import com.agilepm.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Company Management", description = "Endpoints for managing companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @Operation(summary = "Create a new company", description = "Create a new company with a creator")
    public ResponseEntity<Company> createCompany(
        @RequestParam String name, 
        @RequestParam Long createdById
    ) {
        Company createdCompany = companyService.createCompany(name, createdById);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all companies", description = "Retrieve a list of all companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "Get company by ID", description = "Retrieve a specific company by its ID")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId) {
        Company company = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/{companyId}")
    @Operation(summary = "Update company", description = "Update company details")
    public ResponseEntity<Company> updateCompany(
        @PathVariable Long companyId, 
        @RequestParam String newName
    ) {
        Company updatedCompany = companyService.updateCompany(companyId, newName);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{companyId}")
    @Operation(summary = "Delete company", description = "Delete a company by its ID")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{companyId}/users")
    @Operation(summary = "Get company users", description = "Retrieve all users in a company")
    public ResponseEntity<List<UserDTO>> getCompanyUsers(@PathVariable Long companyId) {
        List<UserDTO> users = companyService.getCompanyUsers(companyId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{companyId}/projects")
    @Operation(summary = "Get company projects", description = "Retrieve all projects in a company")
    public ResponseEntity<List<ProjectDTO>> getCompanyProjects(@PathVariable Long companyId) {
        List<ProjectDTO> projects = companyService.getCompanyProjects(companyId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{companyId}/users")
    @Operation(summary = "Add user to company", description = "Add a user to a company")
    public ResponseEntity<Void> addUserToCompany(
        @PathVariable Long companyId, 
        @RequestParam Long userId
    ) {
        companyService.addUserToCompany(companyId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{companyId}/users/{userId}")
    @Operation(summary = "Remove user from company", description = "Remove a user from a company")
    public ResponseEntity<Void> removeUserFromCompany(
        @PathVariable Long companyId, 
        @PathVariable Long userId
    ) {
        companyService.removeUserFromCompany(companyId, userId);
        return ResponseEntity.noContent().build();
    }
}
