package com.sirma.exam.repositories;

import com.sirma.exam.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("select j from Job j where j.employee.id = ?1 order by j.startDate")
    List<Job> findAllByEmployeeId(Long id);

    @Query("select j from Job j where j.project.id = ?1 order by j.startDate")
    List<Job> findAllByProjectId(Long id);

    @Transactional
    @Modifying
    @Query("update Job j set j.description = ?1 where j.id = ?2")
    void updateDescriptionById(String description, Long id);


}
