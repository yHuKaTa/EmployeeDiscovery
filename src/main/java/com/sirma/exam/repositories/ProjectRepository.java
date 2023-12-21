package com.sirma.exam.repositories;

import com.sirma.exam.models.Employee;
import com.sirma.exam.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select (count(p) > 0) from Project p where p.projectName <> ?1")
    boolean doesNotExistsByProjectName(String projectName);

    @Transactional
    @Modifying
    @Query("update Project p set p.endDate = ?1 where p.id = ?2")
    void editProject(LocalDate endDate, Long id);
}
