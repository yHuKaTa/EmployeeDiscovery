package com.sirma.exam.repositories;

import com.sirma.exam.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("select j from Job j where j.employee.id = ?1 order by j.startDate")
    List<Job> findAllByEmployeeId(Long id);

    @Query("select j from Job j where j.project.id = ?1 order by j.startDate")
    List<Job> findAllByProjectId(Long id);


}
