package com.agilepm.repository;

import com.agilepm.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByProject_Id(Long projectId);
    List<Sprint> findByStatus(Sprint.SprintStatus status);
}
