package com.sirma.exam.runners;

import com.sirma.exam.models.Exam;
import com.sirma.exam.repositories.ExamRepository;
import com.sirma.exam.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class AdvanceLogicRunnerForEmployeePairs implements CommandLineRunner {
    private ExamRepository repository;
    // EmployeeId, EmployeeId, Time
    private final TripleMap<Long, Long, Long> pairTimes;
    // EmployeeId, EmployeeId, ProjectId
    private final TripleMap<Long, Long, Set<Long>> projectsPairing;
    private final Set<Long> employeeIds;
    private List<Exam> allRecords;

    @Autowired
    public AdvanceLogicRunnerForEmployeePairs(ExamRepository repository) {
        this.repository = repository;
        this.pairTimes = new TripleMap<>();
        this.projectsPairing = new TripleMap<>();
        this.employeeIds = new HashSet<>();
        this.allRecords = repository.findAll();
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
        List<Exam> tempList = new ArrayList<>(allRecords);
        if (Objects.nonNull(tempList) && !tempList.isEmpty() && tempList.size() >= 2) {
            tempList.forEach(record -> {
                employeeIds.add(record.getEmployeeId());
                tempList.forEach(otherRecord -> {
                    if (!(record.getEmployeeId().equals(otherRecord.getEmployeeId())) && record.getProjectId().equals(otherRecord.getProjectId()) && recordsOverlap(record, otherRecord)) {
                        Long time = calculateDates(record, otherRecord);
                        if (pairTimes.contains(record.getEmployeeId(), otherRecord.getEmployeeId())) {
                            Long temp = pairTimes.get(record.getEmployeeId(), otherRecord.getEmployeeId()) + time;
                            pairTimes.addValue(record.getEmployeeId(), otherRecord.getEmployeeId(), temp);
                        } else {
                            pairTimes.addValue(record.getEmployeeId(), otherRecord.getEmployeeId(), time);
                        }
                    }
                });
            });
            return findMaxTimeTogether();
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

    private Long[] findMaxTimeTogether() {
        long maxTimeTogether = 0L;
        Long employee1 = 0L;
        Long employee2 = 0L;
        if (Objects.nonNull(employeeIds) && !employeeIds.isEmpty() && employeeIds.size() >= 2) {
            for (Long empId : employeeIds) {
                for (Long secondEmpl : employeeIds) {
                    if (!empId.equals(secondEmpl)) {
                        Long currentTime = pairTimes.get(empId, secondEmpl);
                        if (!pairTimes.isEmpty()) {
                            if (maxTimeTogether < currentTime) {
                                maxTimeTogether = currentTime;
                                employee1 = empId;
                                employee2 = secondEmpl;
                            }
                        }
                    }
                }
            }
            if (employee1 != 0L && employee2 !=0L) {
                return new Long[]{employee1, employee2, maxTimeTogether};
            } else return null;
        } else return null;
    }

    private List<Long[]> findProjects(Long[] employeeIds) {
        if (Objects.nonNull(allRecords) && !allRecords.isEmpty() && Objects.nonNull(employeeIds)) {
            List<Long[]> projectTimeSpent = new ArrayList<>();
            Long employee1 = employeeIds[0];
            Long employee2 = employeeIds[1];

            allRecords.forEach(record -> {
                allRecords.forEach(otherRecord -> {
                    if (!(record.getEmployeeId().equals(otherRecord.getEmployeeId())) && recordsOverlap(record, otherRecord)) {
                        if (!projectsPairing.contains(record.getEmployeeId(), otherRecord.getEmployeeId())) {
                            Set<Long> projects = new HashSet<>();
                            projects.add(record.getProjectId());
                            projectsPairing.addValue(record.getEmployeeId(), otherRecord.getEmployeeId(), projects);
                        } else {
                            projectsPairing.get(record.getEmployeeId(), otherRecord.getEmployeeId()).add(record.getProjectId());
                        }
                    }
                });
            });

            Map<Long, Long> timeOnProjects = new HashMap<>();
            for (Long projectId : projectsPairing.get(employee1, employee2)) {
                Exam temp = null;
                for (Exam record : allRecords) {
                    if(record.getEmployeeId().equals(employee1)) {
                        temp = record;
                    }
                    if (record.getProjectId().equals(projectId) && !(employee1.equals(employee2)) && record.getEmployeeId().equals(employee2)) {
                        if (Objects.nonNull(temp)) {
                            if (timeOnProjects.containsKey(projectId)) {
                                timeOnProjects.put(projectId, timeOnProjects.get(projectId) + calculateDates(temp, record));
                            } else {
                                timeOnProjects.put(projectId, calculateDates(temp, record));
                            }
                        }
                    }
                }
            }

            for (Map.Entry<Long, Long> projects : timeOnProjects.entrySet()) {
                projectTimeSpent.add(new Long[]{projects.getKey(), projects.getValue()});
            }
            return projectTimeSpent;
        } else return null;
    }
}
