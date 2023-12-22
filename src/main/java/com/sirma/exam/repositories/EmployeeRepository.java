package com.sirma.exam.repositories;

import com.sirma.exam.models.Employee;
import com.sirma.exam.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByPassportId(String passportId);

    @Query("select (count(e) > 0) from Employee e where e.passportId = ?1")
    boolean existsByPassportId(String passportId);

    @Transactional
    @Modifying
    @Query("update Employee e set e.isFired = ?1 where e.id = ?2 and e.passportId = ?3")
    void fireEmployee(boolean isFired, Long id, String passportId);

    @Transactional
    @Modifying
    @Query("update Employee e set e.firstName = ?1, e.lastName = ?2 where e.passportId = ?3")
    void editEmployee(String firstName, String lastName, String passportId);
}
