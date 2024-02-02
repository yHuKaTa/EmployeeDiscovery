package com.sirma.exam.repositories;

import com.sirma.exam.models.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PairRepository extends JpaRepository<Pair, Long> {
    @Query("""
            select (count(p) = 0) from Pair p
            where (p.employeeId = ?1 and p.otherEmployeeId = ?2 and p.projectId = ?3 and p.startDate = ?4 and p.endDate = ?5) or
            (p.employeeId = ?2 and p.otherEmployeeId = ?1 and p.projectId = ?3 and p.startDate = ?4 and p.endDate = ?5)""")
    boolean notExistsInDB(Long employeeId, Long otherEmployeeId, Long projectId, LocalDate startDate, LocalDate endDate);

    @Query("select p from Pair p where (p.employeeId = ?1 and p.otherEmployeeId = ?2) order by p.projectId")
    List<Pair> findByEmployeesId(Long employeeId, Long otherEmployeeId);
}