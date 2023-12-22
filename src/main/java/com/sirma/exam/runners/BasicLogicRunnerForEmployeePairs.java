package com.sirma.exam.runners;

import com.sirma.exam.models.Exam;
import com.sirma.exam.repositories.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class BasicLogicRunnerForEmployeePairs implements CommandLineRunner {
    private ExamRepository repository;

    // EmployeeId, EmployeeId, ProjectId, Time
    private final Map<Long, Map<Long, Map<Long, Long>>> timeSpentTogether;

    @Autowired
    public BasicLogicRunnerForEmployeePairs(ExamRepository repository) {
        this.repository = repository;
        this.timeSpentTogether = new HashMap<>();
    }

    @Override
    public void run(String... args) throws Exception {
        Long[] twoEmployees = findEmployees();
        List<Long[]> projects = findProjects(twoEmployees);
        if (Objects.nonNull(twoEmployees) && Objects.nonNull(projects)) {
            System.out.println("Top team workers are: " + twoEmployees[0] + " and " + twoEmployees[1] + ". They work together for: " + twoEmployees[2] + " days.");
            System.out.println("They work on projects:");
            for (Long[] project : projects) {
                System.out.println("Project ID: " + project[0] + " for time: " + project[1] + " days");
            }
        }
    }

    private Long[] findEmployees() {
        List<Exam> allRecords = repository.findAll();
        if (Objects.nonNull(allRecords) && !allRecords.isEmpty() && allRecords.size() >= 2) {
            for (Exam record : allRecords) {
                for (Exam otherRecord : allRecords) {
                    if (!timeSpentTogether.containsKey(record.getEmployeeId())) {
                        timeSpentTogether.put(record.getEmployeeId(), new HashMap<>());
                    }
                    if (!record.getEmployeeId().equals(otherRecord.getEmployeeId()) &&
                            record.getProjectId().equals(otherRecord.getProjectId()) &&
                            recordsOverlap(record, otherRecord)) {
                        if (timeSpentTogether.get(record.getEmployeeId()).containsKey(otherRecord.getEmployeeId())) {
                            if (timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).containsKey(record.getProjectId())) {
                                timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).put(record.getProjectId(),
                                        timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).get(record.getProjectId()) + calculateDates(record, otherRecord));
                            } else {
                                timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).put(record.getProjectId(), calculateDates(record, otherRecord));
                            }
                        } else {
                            timeSpentTogether.get(record.getEmployeeId()).put(otherRecord.getEmployeeId(), new HashMap<>());
                            timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).put(record.getProjectId(), calculateDates(record, otherRecord));
                        }
                    }
                }
            }

            if (!timeSpentTogether.isEmpty()) {
                Long maxTimeTogether = 0L;
                Long employee1 = 0L;
                Long employee2 = 0L;

                for (Map.Entry<Long, Map<Long, Map<Long, Long>>> entry : timeSpentTogether.entrySet()) {
                    Long currentEmployee1 = entry.getKey();
                    Map<Long, Map<Long, Long>> innerMap = entry.getValue();

                    for (Map.Entry<Long, Map<Long, Long>> innerEntry : innerMap.entrySet()) {
                        Long currentEmployee2 = innerEntry.getKey();
                        Map<Long, Long> projectMap = innerEntry.getValue();

                        for (Map.Entry<Long, Long> projectEntry : projectMap.entrySet()) {
                            Long timeTogether = projectEntry.getValue();

                            if (timeTogether > maxTimeTogether) {
                                maxTimeTogether = timeTogether;
                                employee1 = currentEmployee1;
                                employee2 = currentEmployee2;
                            }
                        }
                    }
                }

                if (employee1 != 0L && employee2 != 0L) {
                    return new Long[]{employee1, employee2, maxTimeTogether};
                } else return null;
            } else return null;
        } else return null;
    }

    private Long calculateDates(Exam record1, Exam record2) {
        if (isPeriodGraterThen(record1, record2)) {
            return ChronoUnit.DAYS.between(record2.getStartDate(), record1.getEndDate());
        } else {
            return ChronoUnit.DAYS.between(record1.getStartDate(), record2.getEndDate());
        }
    }

    private boolean isPeriodGraterThen(Exam record1, Exam record2) {
        long time1 = ChronoUnit.DAYS.between(record1.getStartDate(), record1.getEndDate());
        long time2 = ChronoUnit.DAYS.between(record2.getStartDate(), record2.getEndDate());
        return  (time1 >= time2);
    }
    private boolean recordsOverlap(Exam record1, Exam record2) {
        if (record1.getEndDate().isBefore(record2.getStartDate())) {
            return false;
        }
        if (record2.getEndDate().isBefore(record1.getStartDate())) {
            return false;
        }
        if (isPeriodGraterThen(record1, record2)) {
            return (record1.getStartDate().isBefore(record2.getStartDate()) ||
                    record1.getStartDate().isEqual(record2.getStartDate()));
        } else {
            return (record2.getStartDate().isBefore(record1.getStartDate()) ||
                    record2.getStartDate().isEqual(record1.getStartDate()));
        }
    }

    private List<Long[]> findProjects(Long[] employeeIds) {
        if (Objects.nonNull(employeeIds) && !timeSpentTogether.isEmpty()) {
            List<Long[]> projectTimeSpent = new ArrayList<>();
            Long employee1 = employeeIds[0];
            Long employee2 = employeeIds[1];

            Map<Long, Long> otherMap = timeSpentTogether.get(employee1).get(employee2);

            for (Map.Entry<Long, Long> projects : otherMap.entrySet()) {
                projectTimeSpent.add(new Long[]{projects.getKey(), projects.getValue()});
            }
            return projectTimeSpent;
        } else return null;
    }
}
